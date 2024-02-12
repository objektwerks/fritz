package objektwerks

interface Entity

typealias License = String
typealias Pin = String
typealias Email = String

data class Account(val license: License, val pin: Pin, val email: Email): Entity