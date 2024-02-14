package objektwerks

class Handler(private val store: Store) {
    fun handle(command: Command): Event =
        when( command ) {
            is Register -> register(command)
            is Login -> login(command)
        }

    private fun register(register: Register): Event =
        runCatching {
            store.register(register.email)
        }.fold(
            { Registered(it) },
            { Fault(it.message ?: "Register failed!") }
        )

    private fun login(login: Login): Event =
        runCatching {
            store.login(login.email, login.pin)
        }.fold(
            { LoggedIn(it) },
            { Fault(it.message ?: "Login failed!") }
        )
}