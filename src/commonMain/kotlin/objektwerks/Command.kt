package objektwerks

interface Command

data class Register(val email: String): Command

data class Login(val email: String, val pin: String): Command