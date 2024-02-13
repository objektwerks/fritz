package objektwerks

class Handler(store: Store) {
    fun handle(command: Command): Event =
        when( command) {
            is Register -> Registered(Account(0, "", "", ""))
            is Login -> LoggedIn(Account(0, "", "", ""))
        }
}