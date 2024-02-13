package objektwerks

class Handler(val store: Store) {
    fun handle(command: Command): Event =
        when( command ) {
            is Login -> LoggedIn( store.login(command.email, command.pin) )
            is Register -> TODO()
        }
}