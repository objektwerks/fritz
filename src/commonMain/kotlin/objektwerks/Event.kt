package objektwerks

import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable

sealed interface Event

@Serializable
data class Registered(val account: Account) : Event

@Serializable
data class LoggedIn(val account: Account) : Event

@Serializable
data class Fault(val error: String, val datetime: Long = Clock.System.now().epochSeconds) : Event {
    companion object {
        fun fault(error: String, command: Command): Fault = Fault("$error: $command")
        fun fault(error: String, event: Event): Fault = Fault("$error: $event")
    }
}