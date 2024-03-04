package objektwerks

import com.sksamuel.aedile.core.cacheBuilder
import com.sksamuel.hoplite.ConfigLoader
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

import java.util.UUID

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Accounts : Table("accounts") {
    val id: Column<Id> = long("id").autoIncrement()
    val license: Column<License> = varchar("license", 36).uniqueIndex("license_idx")
    val pin: Column<Pin> = varchar("pin", 7)
    val email: Column<Email> = varchar("email", 128)
    val created: Column<EpochSeconds> = long("created")
    override val primaryKey = PrimaryKey(id, name = "account_pk")
}

object Pools : Table("pools") {
    val id: Column<Id> = long("id").autoIncrement()
    val license: Column<License> = varchar("license", 36)
    val name: Column<Name> = varchar("name", 128)
    val volume: Column<Volume> = integer("volume")
    val uom: Column<UoM> = varchar("uom", 6)
    override val primaryKey = PrimaryKey(id, name = "pool_pk")
}

object Cleanings : Table("cleanings") {
    val id: Column<Id> = long("id").autoIncrement()
    val poolId: Column<Id> = long("pool_id").references(Pools.id)
    val brush: Column<Boolean> = bool("brush")
    val net: Column<Boolean> = bool("net")
    val skimmerBasket: Column<Boolean> = bool("skimmer_basket")
    val pumpBasket: Column<Boolean> = bool("pump_basket")
    val pumpFilter: Column<Boolean> = bool("pump_filter")
    val vacuum: Column<Boolean> = bool("vacuum")
    val cleaned: Column<EpochSeconds> = long("cleaned")
    override val primaryKey = PrimaryKey(id, name = "cleaning_pk")
}

object Measurements : Table("measurements") {
    val id: Column<Id> = long("id").autoIncrement()
    val poolId: Column<Id> = long("pool_id").references(Pools.id)
    val totalChlorine: Column<TotalChlorine> = integer("total_chlorine")
    val freeChlorine: Column<FreeChlorine> = integer("free_chlorine")
    val combinedChlorine: Column<CombinedChlorine> = double("combined_chlorine")
    val ph: Column<Ph> = double("ph")
    val calciumHardness: Column<CalciumHardness> = integer("calcium_hardness")
    val totalAlkalinity: Column<TotalAlkalinity> = integer("total_alkalinity")
    val cyanuricAcid: Column<CyanuricAcid> = integer("cyanuric_acid")
    val totalBromine: Column<TotalBromine> = integer("total_bromine")
    val salt: Column<Salt> = integer("salt")
    val temperature: Column<Temperature> = integer("temperature")
    val measured: Column<EpochSeconds> = long("measured")
    override val primaryKey = PrimaryKey(id, name = "measurement_pk")
}

object Chemicals : Table("chemicals") {
    val id: Column<Id> = long("id").autoIncrement()
    val poolId: Column<Id> = long("pool_id").references(Pools.id)
    val additive: Column<Additive> = varchar("additive", 16)
    val amount: Column<Amount> = double( "amount")
    val uom: Column<UoM> = varchar("uom", 6)
    val added: Column<EpochSeconds> = long("added")
    override val primaryKey = PrimaryKey(id, name = "chemical_pk")
}

object Faults : Table("faults") {
    val cause: Column<Cause> = varchar("cause", 256)
    val occurred: Column<EpochSeconds> = long("occurred")
}

data class StoreConfig(val url: String,
                       val driver: String,
                       val user: String,
                       val password: String,
                       val maximumSize: Long,
                       val initialCapacity: Int) {
    companion object {
        fun load(resource: String): StoreConfig = ConfigLoader().loadConfigOrThrow<StoreConfig>(resource)
    }
}

class Store(config: StoreConfig) {
    private val licenseCache = cacheBuilder<License, License> {
        maximumSize = config.maximumSize
        initialCapacity = config.initialCapacity
    }.build()

    companion object {
        fun newLicense(): String = UUID.randomUUID().toString()
        fun newPin(): String = UUID.randomUUID().toString().substring(0, 7).lowercase()
    }

    init {
        val hikariConfig = HikariConfig().apply {
            jdbcUrl = config.url
            driverClassName = config.driver
            username = config.user
            password = config.password
        }
        val dataSource = HikariDataSource(hikariConfig)
        Database.connect(dataSource)

        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create( Accounts, Pools, Cleanings, Measurements, Chemicals, Faults )
        }
    }

    suspend fun isLicensed(license: License): Boolean =
        if ( licenseCache.contains(license) ) true
        else if ( hasLicense(license) == 1L ) licenseCache.put(license, license).let { true }
        else false

    private fun hasLicense(license: License): Long =
        transaction {
            Accounts
                .select(Accounts.license)
                .where { Accounts.license eq license }
                .count()
        }

    private fun registerAccount(account: Account): Account =
        transaction {
            account.copy(id =
                Accounts.insert {
                    it[license] = account.license
                    it[pin] = account.pin
                    it[email] = account.email
                    it[created] = account.created
                } get Accounts.id
            )
        }

    fun register(email: String): Account =
        registerAccount(
            Account(
                id = 0,
                license = newLicense(),
                pin = newPin(),
                email = email
            )
        )

    fun login(email: Email, pin: Pin): Account =
        transaction {
            Accounts
                .selectAll()
                .where { (Accounts.email eq email) and (Accounts.pin eq pin) }
                .map { row ->
                    Account(
                        id = row[Accounts.id],
                        license = row[Accounts.license],
                        pin = row[Accounts.pin],
                        email = row[Accounts.email],
                        created = row[Accounts.created]
                    )
                }
                .single()
        }

    fun listPools(): List<Pool> =
        transaction {
            Pools
                .selectAll()
                .orderBy(Pools.name to SortOrder.ASC)
                .map { row ->
                    Pool(
                        id = row[Pools.id],
                        license = row[Pools.license],
                        name = row[Pools.name],
                        volume = row[Pools.volume],
                        uom = row[Pools.uom]
                    )
                }
        }

    fun addPool(pool: Pool): Pool =
        transaction {
            pool.copy(id =
                Pools.insert {
                    it[license] = pool.license
                    it[name] = pool.name
                    it[volume] = pool.volume
                    it[uom] = pool.uom
                } get Pools.id
            )
        }

    fun updatePool(pool: Pool): Int =
        transaction {
            Pools.update( { Pools.id eq pool.id } ) {
                it[name] = pool.name
                it[volume] = pool.volume
                it[uom] = pool.uom
            }
        }

    fun listCleanings(poolId: Id): List<Cleaning> =
        transaction {
            Cleanings
                .selectAll()
                .where { Cleanings.poolId eq poolId }
                .orderBy(Cleanings.cleaned to SortOrder.DESC)
                .map { row ->
                    Cleaning(
                        id = row[Cleanings.id],
                        poolId = row[Cleanings.poolId],
                        brush = row[Cleanings.brush],
                        net = row[Cleanings.net],
                        skimmerBasket = row[Cleanings.skimmerBasket],
                        pumpBasket = row[Cleanings.pumpBasket],
                        pumpFilter = row[Cleanings.pumpFilter],
                        vacuum = row[Cleanings.vacuum],
                        cleaned = row[Cleanings.cleaned]
                    )
                }
        }

    fun addCleaning(cleaning: Cleaning): Cleaning =
        transaction {
            cleaning.copy(id =
                Cleanings.insert {
                    it[poolId] = cleaning.poolId
                    it[brush] = cleaning.brush
                    it[net] = cleaning.net
                    it[skimmerBasket] = cleaning.skimmerBasket
                    it[pumpBasket] = cleaning.pumpBasket
                    it[pumpFilter] = cleaning.pumpFilter
                    it[vacuum] = cleaning.vacuum
                    it[cleaned] = cleaning.cleaned
                } get Cleanings.id
            )
        }

    fun updateCleaning(cleaning: Cleaning): Int =
        transaction {
            Cleanings.update( { Cleanings.poolId eq cleaning.poolId } ) {
                it[brush] = cleaning.brush
                it[net] = cleaning.net
                it[skimmerBasket] = cleaning.skimmerBasket
                it[pumpBasket] = cleaning.pumpBasket
                it[pumpFilter] = cleaning.pumpFilter
                it[vacuum] = cleaning.vacuum
                it[cleaned] = cleaning.cleaned
            }
        }

    fun listMeasurements(poolId: Id): List<Measurement> =
        transaction {
            Measurements
                .selectAll()
                .where { Measurements.poolId eq poolId }
                .orderBy(Measurements.measured to SortOrder.DESC)
                .map { row ->
                    Measurement(
                        id = row[Measurements.id],
                        poolId = row[Measurements.poolId],
                        totalChlorine = row[Measurements.totalChlorine],
                        freeChlorine = row[Measurements.freeChlorine],
                        combinedChlorine = row[Measurements.combinedChlorine],
                        ph = row[Measurements.ph],
                        calciumHardness = row[Measurements.calciumHardness],
                        totalAlkalinity = row[Measurements.totalAlkalinity],
                        cyanuricAcid = row[Measurements.cyanuricAcid],
                        totalBromine = row[Measurements.totalBromine],
                        salt = row[Measurements.salt],
                        temperature = row[Measurements.temperature],
                        measured = row[Measurements.measured]
                    )
                }
        }

    fun addMeasurement(measurement: Measurement): Measurement =
        transaction {
            measurement.copy(id =
                Measurements.insert {
                    it[poolId] = measurement.poolId
                    it[totalChlorine] = measurement.totalChlorine
                    it[freeChlorine] = measurement.freeChlorine
                    it[combinedChlorine] = measurement.combinedChlorine
                    it[ph] = measurement.ph
                    it[calciumHardness] = measurement.calciumHardness
                    it[totalAlkalinity] = measurement.totalAlkalinity
                    it[cyanuricAcid] = measurement.cyanuricAcid
                    it[totalBromine] = measurement.totalBromine
                    it[salt] = measurement.salt
                    it[temperature] = measurement.temperature
                    it[measured] = measurement.measured
                } get Measurements.id
            )
        }

    fun updateMeasurement(measurement: Measurement): Int =
        transaction {
            Measurements.update( { Measurements.poolId eq measurement.poolId } ) {
                it[totalChlorine] = measurement.totalChlorine
                it[freeChlorine] = measurement.freeChlorine
                it[combinedChlorine] = measurement.combinedChlorine
                it[ph] = measurement.ph
                it[calciumHardness] = measurement.calciumHardness
                it[totalAlkalinity] = measurement.totalAlkalinity
                it[cyanuricAcid] = measurement.cyanuricAcid
                it[totalBromine] = measurement.totalBromine
                it[salt] = measurement.salt
                it[temperature] = measurement.temperature
                it[measured] = measurement.measured
            }
        }

    fun listChemicals(poolId: Id): List<Chemical> =
        transaction {
            Chemicals
                .selectAll()
                .where { Chemicals.poolId eq poolId }
                .orderBy(Chemicals.added to SortOrder.DESC)
                .map { row ->
                    Chemical(
                        id = row[Chemicals.id],
                        poolId = row[Chemicals.poolId],
                        additive = row[Chemicals.additive],
                        amount = row[Chemicals.amount],
                        uom = row[Chemicals.uom],
                        added = row[Chemicals.added]
                    )
                }
        }

    fun addChemical(chemical: Chemical): Chemical =
        transaction {
            chemical.copy(id =
                Chemicals.insert {
                    it[poolId] = chemical.poolId
                    it[additive] = chemical.additive
                    it[amount] = chemical.amount
                    it[uom] = chemical.uom
                    it[added] = chemical.added
                } get Chemicals.id
            )
        }

    fun updateChemical(chemical: Chemical): Int =
        transaction {
            Chemicals.update( { Chemicals.poolId eq chemical.poolId } ) {
                it[additive] = chemical.additive
                it[amount] = chemical.amount
                it[uom] = chemical.uom
                it[added] = chemical.added
            }
        }

    fun listFaults(): List<Fault> =
        transaction {
            Faults
                .selectAll()
                .orderBy(Faults.occurred to SortOrder.DESC)
                .map { row ->
                    Fault(
                        cause = row[Faults.cause],
                        occurred = row[Faults.occurred]
                    )
                }
        }

    fun addFault(fault: Fault): EpochSeconds =
        transaction {
            Faults.insert {
                it[cause] = fault.cause
                it[occurred] = fault.occurred
            } get Faults.occurred
        }
}