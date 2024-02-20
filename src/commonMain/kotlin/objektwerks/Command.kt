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

@Serializable
data class ListPools(override val license: String) : Command, Licensed

@Serializable
data class AddPool(override val license: String, val pool: Pool) : Command, Licensed

@Serializable
data class UpdatePool(override val license: String, val pool: Pool) : Command, Licensed

@Serializable
data class ListCleanings(override val license: String) : Command, Licensed

@Serializable
data class AddCleaning(override val license: String, val cleaning: Cleaning) : Command, Licensed

@Serializable
data class UpdateCleaning(override val license: String, val cleaning: Cleaning) : Command, Licensed