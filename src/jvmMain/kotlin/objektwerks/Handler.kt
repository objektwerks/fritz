package objektwerks

class Handler(private val store: Store) {
    fun handle(command: Command): Event =
        when( command ) {
            is Register -> Registered( store.register(command.email) )
            is Login -> LoggedIn( store.login(command.email, command.pin) )
        }

    fun register(register: Register): Event =
        runCatching {
            store.register(register.email)
        }.fold(
            { Registered(it) },
            { Fault(it.message ?: "Register failed!") }
        )

    fun login(login: Login): Event = TODO()
}