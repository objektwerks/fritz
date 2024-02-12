package objektwerks

sealed interface Entity {
    val id: Id
}

typealias Id = Long
typealias License = String
typealias Pin = String
typealias Email = String

data class Account(override val id: Id = 0,
                   val license: License = "",
                   val pin: Pin = "",
                   val email: Email = ""): Entity