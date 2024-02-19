package objektwerks

import kotlinx.datetime.Clock
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
}

typealias Id = Long
typealias License = String
typealias Pin = String
typealias Email = String
typealias Name = String
typealias Volume = Int
typealias UoM = String
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
        fun empty(): Account = Account(0, "", "", "")
    }
}

@Serializable
data class Pool(override val id: Id,
                val license: License,
                val name: Name,
                val volume: Volume,
                val uom: UoM = UnitOfMeasure.gl.toString()) : Entity {
    override fun display() = name
    companion object {
        fun empty(): Pool = Pool(0, "", "", 100)
        val comparator = compareBy<Pool> { it.name }
    }
}