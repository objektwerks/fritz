package objektwerks

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

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
    val name: Column<Name> = varchar("name", 128).index("name_idx", false)
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
    val cleaned: Column<EpochSeconds> = long("cleaned").index("cleaned_idx", false)
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
    val measured: Column<EpochSeconds> = long("measured").index("measured_idx", false)
    override val primaryKey = PrimaryKey(id, name = "measurement_pk")
}

object Chemicals : Table("chemicals") {
    val id: Column<Id> = long("id").autoIncrement()
    val poolId: Column<Id> = long("pool_id").references(Pools.id)
    val additive: Column<Additive> = varchar("additive", 16)
    val amount: Column<Amount> = double( "amount")
    val uom: Column<UoM> = varchar("uom", 6)
    val added: Column<EpochSeconds> = long("added").index("added_idx", false)
    override val primaryKey = PrimaryKey(id, name = "chemical_pk")
}

object Faults : Table("faults") {
    val cause: Column<Cause> = varchar("cause", 256)
    val occurred: Column<EpochSeconds> = long("occurred").index("occurred_idx", false)
}