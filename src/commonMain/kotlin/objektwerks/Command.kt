package objektwerks

import kotlinx.serialization.Serializable

sealed interface Command

sealed interface Licensed {
    val license: String
}

@Serializable
data class Register(val email: Email) : Command

@Serializable
data class Login(val email: Email, val pin: Pin) : Command