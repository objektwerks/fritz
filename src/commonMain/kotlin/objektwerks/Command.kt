package objektwerks

interface Command

data class Register(val email: Email): Command

data class Login(val email: Email, val pin: Pin): Command