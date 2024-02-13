package objektwerks

class Handler(val store: Store) {
    fun handle(command: Command): Event =
        when( command ) {
            is Register -> Registered( store.register(command.email) )
            is Login -> LoggedIn( store.login(command.email, command.pin) )
        }
}