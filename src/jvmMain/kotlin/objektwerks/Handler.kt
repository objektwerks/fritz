package objektwerks

class Handler(store: Store) {
    fun handle(command: Command): Event =
        when( command ) {
            is Login -> TODO()
            is Register -> TODO()
        }
}