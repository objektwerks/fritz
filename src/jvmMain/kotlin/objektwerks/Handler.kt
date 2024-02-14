package objektwerks

class Handler(private val store: Store) {
    fun handle(command: Command): Event =
        when( command ) {
            is Register -> register(command)
            is Login -> LoggedIn( store.login(command.email, command.pin) )
        }

    fun register(register: Register): Event =
        runCatching {
            store.register(register.email)
        }.fold(
            { Registered(it) },
            { Fault(it.message ?: "Register failed!") }
        )

    fun login(login: Login): Event =
        runCatching {
            store.login(login.email, login.pin)
        }.fold(
            { LoggedIn(it) },
            { Fault(it.message ?: "Login failed!") }
        )
}