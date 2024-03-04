package objektwerks

import kotlinx.datetime.*
import kotlinx.serialization.Serializable

enum class UnitOfMeasure {
    gl, l, lb, kg, tablet;
    companion object {
        fun toList(): List<String> = entries.map { uom -> uom.toString() }
        fun toPoolList(): List<String> = listOf( gl.toString(), l.toString() )
        fun gallonsToLiters(gallons: Double): Double = gallons * 3.785
        fun litersToGallons(liters: Double): Double = liters * 0.264
        fun poundsToKilograms(pounds: Double): Double = pounds * 0.454
        fun kilogramsToPounds(kilograms: Double): Double = kilograms * 2.205
    }
}

enum class TypeOfMeasurement(val display: String) {
    TotalChlorine("Total Chlorine"),
    FreeChlorine("Free Chlorine"),
    CombinedChlorine("Combined Chlorine"),
    Ph("Ph"),
    CalciumHardness("Calcium Hardness"),
    TotalAlkalinity("Total Alkalinity"),
    CyanuricAcid("Cyanuric Acid"),
    TotalBromine("Total Bromine"),
    Salt("Salt"),
    Temperature("Temperature");
    companion object {
        fun toEnum(display: String): TypeOfMeasurement = TypeOfMeasurement.valueOf( display.filterNot { it.isWhitespace() } )
        fun toList(): List<String> = entries.map { tom -> tom.display }
    }
}

object RangeOfMeasurement {
    val totalChlorineRange = IntRange(1, 5)
    val freeChlorineRange = IntRange(1, 5)
    val combinedChlorineRange = setOf(0.0, 0.1, 0.2, 0.3, 0.4, 0.5)
    val phRange = setOf(6.2, 6.3, 6.4, 6.5, 6.6, 6.7, 6.8, 6.9, 7.0, 7.1, 7.2, 7.3, 7.4, 7.5, 7.6, 7.7, 7.8, 7.9, 8.0, 8.1, 8.2, 8.3, 8.4)
    val calciumHardnessRange = IntRange(250, 500)
    val totalAlkalinityRange = IntRange(80, 120)
    val cyanuricAcidRange = IntRange(30, 100)
    val totalBromineRange = IntRange(2, 10)
    val saltRange = IntRange(2700, 3400)
    val temperatureRange = IntRange(50, 100)
}

enum class TypeOfChemical(val display: String) {
    LiquidChlorine("Liquid Chlorine"),
    Trichlor("Trichlor"),
    Dichlor("Dichlor"),
    CalciumHypochlorite("Calcium Hypochlorite"),
    Stabilizer("Stabilizer"),
    Algaecide("Algaecide"),
    MuriaticAcid("Muriatic Acid"),
    Salt("Salt");
    companion object {
        fun toEnum(display: String): TypeOfChemical = TypeOfChemical.valueOf( display.filterNot { it.isWhitespace() } )
        fun toList(): List<String> = entries.map { toc -> toc.display }
    }
}

sealed interface Entity {
    val id: Id
    fun display(): String
    fun toLocalDateTime(epochSeconds: EpochSeconds): String =
        Instant.fromEpochSeconds(epochSeconds).toLocalDateTime(TimeZone.currentSystemDefault()).toString()
}

typealias Id = Long
typealias License = String
typealias Pin = String
typealias Email = String
typealias Name = String
typealias Volume = Int
typealias UoM = String
typealias TotalChlorine = Int
typealias FreeChlorine = Int
typealias CombinedChlorine = Double
typealias Ph = Double
typealias CalciumHardness = Int
typealias TotalAlkalinity = Int
typealias CyanuricAcid = Int
typealias TotalBromine = Int
typealias Salt = Int
typealias Temperature = Int
typealias Additive = String
typealias Amount = Double
typealias EpochSeconds = Long

@Serializable
data class Account(override val id: Id,
                   val license: License,
                   val pin: Pin,
                   val email: Email,
                   val created: EpochSeconds = Clock.System.now().epochSeconds) : Entity {
    override fun display() = email
    companion object {
        val comparator = compareBy<Account> { it.created }
    }
}

@Serializable
data class Pool(override val id: Id = 0,
                val license: License = "",
                val name: Name = "",
                val volume: Volume = 100,
                val uom: UoM = UnitOfMeasure.gl.toString()) : Entity {
    override fun display() = name
    companion object {
        val comparator = compareBy<Pool> { it.name }
    }
}

@Serializable
data class Cleaning(override val id: Id = 0,
                    val poolId: Long = 0,
                    val brush: Boolean = true,
                    val net: Boolean = true,
                    val skimmerBasket: Boolean = true,
                    val pumpBasket: Boolean = false,
                    val pumpFilter: Boolean = false,
                    val vacuum: Boolean = false,
                    val cleaned: EpochSeconds = Clock.System.now().epochSeconds) : Entity {
    override fun display() = toLocalDateTime(cleaned)
    companion object {
        val comparator = compareBy<Cleaning> { it.cleaned }.reversed()
    }
}

@Serializable
data class Measurement(override val id: Id = 0,
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
                       val measured: EpochSeconds = Clock.System.now().epochSeconds) : Entity {
    override fun display() = toLocalDateTime(measured)
    companion object {
        val comparator = compareBy<Measurement> { it.measured }.reversed()
    }
}

@Serializable
data class Chemical(override val id: Id = 0,
                    val poolId: Id = 0,
                    val additive: Additive = TypeOfChemical.LiquidChlorine.toString(),
                    val amount: Amount = 1.0,
                    val uom: UoM = UnitOfMeasure.gl.toString(),
                    val added: EpochSeconds = Clock.System.now().epochSeconds) : Entity {
    override fun display() = toLocalDateTime(added)
    companion object {
        val comparator = compareBy<Chemical> { it.added }.reversed()
    }
}