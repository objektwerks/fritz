package objektwerks

import dev.fritz2.core.Lenses

import kotlinx.serialization.Serializable

sealed interface Entity {
    val id: Id
}

typealias Id = Long
typealias License = String
typealias Pin = String
typealias Email = String

@Serializable
@Lenses
data class Account(override val id: Id,
                   val license: License,
                   val pin: Pin,
                   val email: Email) : Entity