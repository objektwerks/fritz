package objektwerks

import kotlinx.serialization.Serializable

sealed interface Command

@Serializable
data class Register(val email: Email) : Command

@Serializable
data class Login(val email: Email, val pin: Pin) : Command