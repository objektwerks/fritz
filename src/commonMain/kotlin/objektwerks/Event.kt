package objektwerks

sealed interface Event

data class Registered(val account: Account) : Event

data class LoggedIn(val account: Account) : Event