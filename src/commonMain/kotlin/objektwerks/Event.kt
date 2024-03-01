package objektwerks

import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable

sealed interface Event

@Serializable
data class Registered(val account: Account) : Event

@Serializable
data class LoggedIn(val account: Account) : Event

@Serializable
data class Updated(val count: Int) : Event

@Serializable
data class PoolsListed(val pools: List<Pool>) : Event

@Serializable
data class PoolAdded(val pool: Pool) : Event

@Serializable
data class CleaningsListed(val cleanings: List<Cleaning>) : Event

@Serializable
data class CleaningAdded(val cleaning: Cleaning) : Event

@Serializable
data class MeasurementsListed(val measurements: List<Measurement>) : Event

@Serializable
data class MeasurementAdded(val measurement: Measurement) : Event

@Serializable
data class ChemicalsListed(val chemicals: List<Chemical>) : Event

@Serializable
data class ChemicalAdded(val chemical: Chemical) : Event

@Serializable
data class Fault(val error: String, val datetime: Long = Clock.System.now().epochSeconds) : Event {
    companion object {
        fun build(error: Throwable, default: String): Fault = Fault(error.message ?: default)
        fun build(error: Throwable, default: String, command: Command): Fault = Fault("${error.message ?: default} : $command")
        fun build(error: String, command: Command): Fault = Fault("$error : $command")
        fun build(error: String, event: Event): Fault = Fault("$error : $event")
    }
}