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
                is ListCleanings -> listCleanings(command.poolId)
                is AddCleaning -> TODO()
                is UpdateCleaning -> TODO()
            }
        else Fault.build("Invalid Command", command)

    private fun nonFatal(throwable: Throwable): Boolean =
        when(throwable) {
            is Error -> false
            is InterruptedException -> false
            else -> true
        }

    private fun register(register: Register): Event =
        runCatching {
            store.register(register.email)
        }.fold(
            { Registered(it) },
            { if( nonFatal(it) ) Fault(it.message ?: "Register failed!") else throw it }
        )

    private fun login(login: Login): Event =
        runCatching {
            store.login(login.email, login.pin)
        }.fold(
            { LoggedIn(it) },
            { if( nonFatal(it) )  Fault(it.message ?: "Login failed!") else throw it }
        )

    private fun listPools(): Event =
        runCatching {
            store.listPools()
        }.fold(
            { PoolsListed(it) },
            { if( nonFatal(it) )  Fault(it.message ?: "List pools failed!") else throw it }
        )

    private fun addPool(pool: Pool): Event =
        runCatching {
            store.addPool(pool)
        }.fold(
            { PoolAdded(it) },
            { if( nonFatal(it) )  Fault(it.message ?: "Add pool failed!") else throw it }
        )

    private fun updatePool(pool: Pool): Event =
        runCatching {
            store.updatePool(pool)
        }.fold(
            { Updated(it) },
            { if( nonFatal(it) )  Fault(it.message ?: "Update pool failed!") else throw it }
        )

    private fun listCleanings(poolId: Id): Event =
        runCatching {
            store.listCleanings(poolId)
        }.fold(
            { CleaningsListed(it) },
            { if( nonFatal(it) )  Fault(it.message ?: "List cleanings failed!") else throw it }
        )
}