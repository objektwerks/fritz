package objektwerks

import java.time.Instant

import kotlinx.serialization.Serializable

sealed interface Event

@Serializable
data class Registered(val account: Account) : Event

@Serializable
data class LoggedIn(val account: Account) : Event

@Serializable
data class Fault(val error: String, val datetime: Long = Instant.now().getEpochSecond()) : Event