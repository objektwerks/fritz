package objektwerks

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Handler(private val store: Store) {
    suspend fun handle(command: Command): Event =
        if (command.isValid() && command.isLicensed())
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
                is ListMeasurements -> listMeasurements(command.poolId)
                is AddMeasurement -> addMeasurement(command.measurement)
                is UpdateMeasurement -> updateMeasurement(command.measurement)
            }
        else Fault.build("Invalid Command or License", command)

    private fun Throwable.nonFatal(): Boolean =
        when(this) {
            is Error -> false
            is InterruptedException -> false
            else -> true
        }

    private fun Command.isLicensed(): Boolean =
        when(this) {
            is Licensed -> store.isLicensed(this.license)
            is Register -> true
            is Login -> true
        }

    private suspend fun register(register: Register): Event =
        runCatching {
            withContext(Dispatchers.IO) {
                store.register(register.email)
            }
        }.fold(
            { Registered(it) },
            { if( it.nonFatal() ) Fault(it.message ?: "Register failed!") else throw it }
        )

    private suspend fun login(login: Login): Event =
        runCatching {
            withContext(Dispatchers.IO) {
                store.login(login.email, login.pin)
            }
        }.fold(
            { LoggedIn(it) },
            { if( it.nonFatal() )  Fault(it.message ?: "Login failed!") else throw it }
        )

    private suspend fun listPools(): Event =
        runCatching {
            withContext(Dispatchers.IO) {
                store.listPools()
            }
        }.fold(
            { PoolsListed(it) },
            { if( it.nonFatal() )  Fault(it.message ?: "List pools failed!") else throw it }
        )

    private suspend fun addPool(pool: Pool): Event =
        runCatching {
            withContext(Dispatchers.IO) {
                store.addPool(pool)
            }
        }.fold(
            { PoolAdded(it) },
            { if( it.nonFatal() )  Fault(it.message ?: "Add pool failed!") else throw it }
        )

    private suspend fun updatePool(pool: Pool): Event =
        runCatching {
            withContext(Dispatchers.IO) {
                store.updatePool(pool)
            }
        }.fold(
            { Updated(it) },
            { if( it.nonFatal() )  Fault(it.message ?: "Update pool failed!") else throw it }
        )

    private suspend fun listCleanings(poolId: Id): Event =
        runCatching {
            withContext(Dispatchers.IO) {
                store.listCleanings(poolId)
            }
        }.fold(
            { CleaningsListed(it) },
            { if( it.nonFatal() )  Fault(it.message ?: "List cleanings failed!") else throw it }
        )

    private suspend fun addCleaning(cleaning: Cleaning): Event =
        runCatching {
            withContext(Dispatchers.IO) {
                store.addCleaning(cleaning)
            }
        }.fold(
            { CleaningAdded(it) },
            { if( it.nonFatal() )  Fault(it.message ?: "Add cleaning failed!") else throw it }
        )

    private suspend fun updateCleaning(cleaning: Cleaning): Event =
        runCatching {
            withContext(Dispatchers.IO) {
                store.updateCleaning(cleaning)
            }
        }.fold(
            { Updated(it) },
            { if( it.nonFatal() )  Fault(it.message ?: "Update cleaning failed!") else throw it }
        )

    private suspend fun listMeasurements(poolId: Id): Event =
        runCatching {
            withContext(Dispatchers.IO) {
                store.listMeasurements(poolId)
            }
        }.fold(
            { MeasurementsListed(it) },
            { if( it.nonFatal() )  Fault(it.message ?: "List measurements failed!") else throw it }
        )

    private fun addMeasurement(measurement: Measurement): Event =
        runCatching {
            store.addMeasurement(measurement)
        }.fold(
            { MeasurementAdded(it) },
            { if( it.nonFatal() )  Fault(it.message ?: "Add measurement failed!") else throw it }
        )

    private fun updateMeasurement(measurement: Measurement): Event =
        runCatching {
            store.updateMeasurement(measurement)
        }.fold(
            { Updated(it) },
            { if( it.nonFatal() )  Fault(it.message ?: "Update measurement failed!") else throw it }
        )

    private fun listChemicals(poolId: Id): Event =
        runCatching {
            store.listChemicals(poolId)
        }.fold(
            { ChemicalsListed(it) },
            { if( it.nonFatal() )  Fault(it.message ?: "List chemicals failed!") else throw it }
        )

    private fun addChemical(chemical: Chemical): Event =
        runCatching {
            store.addChemical(chemical)
        }.fold(
            { ChemicalAdded(it) },
            { if( it.nonFatal() )  Fault(it.message ?: "Add cleaning failed!") else throw it }
        )

    private fun updateChemical(chemical: Chemical): Event =
        runCatching {
            store.updateChemical(chemical)
        }.fold(
            { Updated(it) },
            { if( it.nonFatal() )  Fault(it.message ?: "Update chemical failed!") else throw it }
        )
}