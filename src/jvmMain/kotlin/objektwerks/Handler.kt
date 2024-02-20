package objektwerks

class Handler(private val store: Store) {
    fun handle(command: Command): Event =
        if (command.isValid())
            when(command) {
                is Register -> register(command)
                is Login -> login(command)
                is ListPools -> listPools()
                is AddPool -> addPool(command.pool)
                is UpdatePool -> updatePool(command.pool)
            }
        else Fault.build("Invalid Command", command)

    fun nonFatal(throwable: Throwable): Boolean =
        when(throwable) {
            is VirtualMachineError -> false
            is InterruptedException -> false
            is LinkageError -> false
            else -> true
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

    private fun listPools(): Event =
        runCatching {
            store.listPools()
        }.fold(
            { PoolsListed(it) },
            { Fault(it.message ?: "List pools failed!") }
        )

    private fun addPool(pool: Pool): Event =
        runCatching {
            store.addPool(pool)
        }.fold(
            { PoolAdded(it) },
            { Fault(it.message ?: "Add pool failed!") }
        )

    private fun updatePool(pool: Pool): Event =
        runCatching {
            store.updatePool(pool)
        }.fold(
            { Updated(it) },
            { Fault(it.message ?: "Update pool failed!") }
        )
}