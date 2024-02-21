package objektwerks

import com.sksamuel.hoplite.ConfigLoader

import java.util.UUID

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Accounts : Table() {
    val id: Column<Id> = long("id").autoIncrement()
    val license: Column<License> = varchar("license", 36)
    val pin: Column<Pin> = varchar("pin", 7)
    val email: Column<Email> = varchar("email", 128)
    val created: Column<EpochSeconds> = long("created")
    override val primaryKey = PrimaryKey(id, name = "accountPk")
}

object Pools : Table() {
    val id: Column<Id> = long("id").autoIncrement()
    val license: Column<License> = varchar("license", 36)
    val name: Column<Name> = varchar("name", 128)
    val volume: Column<Volume> = integer("volume")
    val uom: Column<UoM> = varchar("uom", 6)
    override val primaryKey = PrimaryKey(id, name = "poolPk")
}

object Cleanings : Table() {
    val id: Column<Id> = long("id").autoIncrement()
    val poolId: Column<Id> = long("poolId").references(Pools.id)
    val brush: Column<Boolean> = bool("brush")
    val net: Column<Boolean> = bool("net")
    val skimmerBasket: Column<Boolean> = bool("skimmerBasket")
    val pumpBasket: Column<Boolean> = bool("pumpBasket")
    val pumpFilter: Column<Boolean> = bool("pumpFilter")
    val vacuum: Column<Boolean> = bool("vacuum")
    val cleaned: Column<EpochSeconds> = long("cleaned")
    override val primaryKey = PrimaryKey(id, name = "cleaningPk")
}

object Chemicals : Table() {
    val id: Column<Id> = long("id").autoIncrement()
    val poolId: Column<Id> = long("poolId").references(Pools.id)
    val additive: Column<Additive> = varchar("additive", 16)
    val amount: Column<Amount> = double( "amount")
    val uom: Column<UoM> = varchar("uom", 6)
    val added: Column<EpochSeconds> = long("added")
}

data class StoreConfig(val url: String,
                       val driver: String,
                       val user: String,
                       val password: String) {
    companion object {
        fun load(resource: String): StoreConfig = ConfigLoader().loadConfigOrThrow<StoreConfig>(resource)
    }
}

class Store(config: StoreConfig,
            cache: Set<License> = emptySet()) {
    init {
        Database.connect(
            url = config.url,
            driver = config.driver,
            user = config.user,
            password = config.password
        )
        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create( Accounts, Pools, Cleanings, Chemicals )
        }
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
                license = UUID.randomUUID().toString(),
                pin = UUID.randomUUID().toString().substring(0, 7),
                email = email
            )
        )

    fun login(email: String, pin: String): Account =
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


    fun listAccounts(): List<Account> =
        transaction {
            Accounts
                .selectAll()
                .map { row ->
                    Account(
                        id = row[Accounts.id],
                        license = row[Accounts.license],
                        pin = row[Accounts.pin],
                        email = row[Accounts.email],
                        created = row[Accounts.created]
                    )
                }
        }

    fun updateAccount(account: Account): Int =
        transaction {
            Accounts.update( { Accounts.id eq account.id } ) {
                it[email] = account.email
            }
        }

    fun listPools(): List<Pool> =
        transaction {
            Pools
                .selectAll()
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
                .where { Pools.id eq poolId }
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
            Cleanings.update( { Pools.id eq cleaning.poolId } ) {
                it[brush] = cleaning.brush
                it[net] = cleaning.net
                it[skimmerBasket] = cleaning.skimmerBasket
                it[pumpBasket] = cleaning.pumpBasket
                it[pumpFilter] = cleaning.pumpFilter
                it[vacuum] = cleaning.vacuum
                it[cleaned] = cleaning.cleaned
            }
        }

    /* Measurement
       override val id: Id = 0,
       val poolId: Id = 0,
       val totalChlorine: TotalChlorine = 3,
       val freeChlorine: FreeChlorine = 3,
       val combinedChlorine: CombinedChlorine = 0.0,
       val ph: Ph = 7.4,
       val calciumHardness: CalciumHardness = 375,
       val totalAlkalinity: TotalAlkalinity = 100,
       val cyanuricAcid: CyanuricAcid = 50,
       val totalBromine: TotalBromine = 5,
       val salt: Salt = 3200,
       val temperature: Temperature = 85,
       val measured: EpochSeconds
     */

    fun listChemicals(poolId: Id): List<Chemical> =
        transaction {
            Chemicals
                .selectAll()
                .where { Pools.id eq poolId }
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
            Chemicals.update( { Pools.id eq chemical.poolId } ) {
                it[additive] = chemical.additive
                it[amount] = chemical.amount
                it[uom] = chemical.uom
                it[added] = chemical.added
            }
        }
}