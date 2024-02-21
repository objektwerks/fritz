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
                is AddCleaning -> addCleaning(command.cleaning)
                is UpdateCleaning -> updateCleaning(command.cleaning)
                is ListChemicals -> listChemicals(command.poolId)
                is AddChemical -> addChemical(command.chemical)
                is UpdateChemical -> updateChemical(command.chemical)
                is ListMeasurements -> TODO()
                is AddMeasurement -> TODO()
                is UpdateMeasurement -> TODO()
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

    private fun addCleaning(cleaning: Cleaning): Event =
        runCatching {
            store.addCleaning(cleaning)
        }.fold(
            { CleaningAdded(it) },
            { if( nonFatal(it) )  Fault(it.message ?: "Add cleaning failed!") else throw it }
        )

    private fun updateCleaning(cleaning: Cleaning): Event =
        runCatching {
            store.updateCleaning(cleaning)
        }.fold(
            { Updated(it) },
            { if( nonFatal(it) )  Fault(it.message ?: "Update cleaning failed!") else throw it }
        )

    private fun listMeasurements(poolId: Id): Event =
        runCatching {
            store.listMeasurements(poolId)
        }.fold(
            { MeasurementsListed(it) },
            { if( nonFatal(it) )  Fault(it.message ?: "List measurements failed!") else throw it }
        )

    private fun listChemicals(poolId: Id): Event =
        runCatching {
            store.listChemicals(poolId)
        }.fold(
            { ChemicalsListed(it) },
            { if( nonFatal(it) )  Fault(it.message ?: "List chemicals failed!") else throw it }
        )

    private fun addChemical(chemical: Chemical): Event =
        runCatching {
            store.addChemical(chemical)
        }.fold(
            { ChemicalAdded(it) },
            { if( nonFatal(it) )  Fault(it.message ?: "Add cleaning failed!") else throw it }
        )

    private fun updateChemical(chemical: Chemical): Event =
        runCatching {
            store.updateChemical(chemical)
        }.fold(
            { Updated(it) },
            { if( nonFatal(it) )  Fault(it.message ?: "Update chemical failed!") else throw it }
        )
}