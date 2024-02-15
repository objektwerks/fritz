package objektwerks

import kotlinx.serialization.Serializable

sealed interface Entity {
    val id: Id
}

typealias Id = Long
typealias License = String
typealias Pin = String
typealias Email = String

@Serializable
data class Account(override val id: Id,
                   val license: License,
                   val pin: Pin,
                   val email: Email) : Entity {
    fun isValid(): Boolean =
        (id > 0) &&
        (license.length == 36) &&
        (pin.length == 7) &&
        (email.contains("@"))
}