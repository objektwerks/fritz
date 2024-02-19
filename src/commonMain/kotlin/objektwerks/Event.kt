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
data class Fault(val error: String, val datetime: Long = Clock.System.now().epochSeconds) : Event {
    companion object {
        fun build(error: String, command: Command): Fault = Fault("$error: $command")
        fun build(error: String, event: Event): Fault = Fault("$error: $event")
    }
}