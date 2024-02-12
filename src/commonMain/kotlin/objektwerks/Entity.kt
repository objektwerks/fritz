package objektwerks

interface Entity

data class Account(val license: String, val pin: String, val email: String): Entity