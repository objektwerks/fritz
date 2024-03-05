package objektwerks

import kotlinx.coroutines.runBlocking

import org.junit.Test

class HandlerTest {
    @Test
    fun handle() {
        runBlocking {
            ProcessBuilder("psql", "-d", "fritz", "-f", "ddl.sql")
                .start()
                .inputStream.bufferedReader().readText()
                .also { println(it) }

            val store = Store( StoreConfig.load("/store.yaml") )
            val emailer = Emailer( EmailerConfig.load("/emailer.yaml") )
            val handler = Handler(store, emailer)

            test(handler)
            test(store)
        }
    }

    private suspend fun test(handler: Handler) {
        val register = Register(EmailerConfig.load("/emailer.yaml").sender)
        assert( register.isRegister() )
        val registered = handler.handle(register) as Registered
        assert( registered.isRegistered() )

        val account = registered.account
        assert( account.isAccount() )
        val license = account.license
        assert( license.isLicense() )

        val login = Login(account.email, account.pin)
        assert( login.isLogin() )
        val loggedIn = handler.handle(login) as LoggedIn
        assert( loggedIn.isLoggedIn() )
        assert( loggedIn.account == account )

        val addPool = AddPool(license, Pool(license = license, name = "pool-a"))
        assert( addPool.isAddPool() )
        val poolAdded = handler.handle(addPool) as PoolAdded
        assert( poolAdded.isPoolAdded() )

        val pool = poolAdded.pool
        assert( pool.isPool() )
        val updatePool = UpdatePool(license, pool.copy(name = "pool-z"))
        assert( updatePool.isUpdatePool() )
        val updatedPool = handler.handle(updatePool) as Updated
        assert( updatedPool.isUpdated() )

        val listPools = ListPools(license)
        assert( listPools.isListPools() )
        val poolsListed = handler.handle(listPools)as PoolsListed
        assert( poolsListed.isPoolsListed() )
        assert(poolsListed.pools.isNotEmpty())

        val addCleaning = AddCleaning(license, Cleaning(poolId = pool.id))
        assert( addCleaning.isAddCleaning() )
        val cleaningAdded = handler.handle(addCleaning) as CleaningAdded
        assert( cleaningAdded.isCleaingAdded() )

        val cleaning = cleaningAdded.cleaning
        assert( cleaning.isCleaning() )
        val updateCleaning = UpdateCleaning(license, cleaning.copy(vacuum = true))
        assert( updateCleaning.isUpdateCleaning() )
        val updatedCleaning = handler.handle(updateCleaning) as Updated
        assert( updatedCleaning.isUpdated() )

        val listCleanings = ListCleanings(license, pool.id)
        assert( listCleanings.isListCleanings() )
        val cleaningsListed = handler.handle(listCleanings) as CleaningsListed
        assert( cleaningsListed.isCleaningsListed() )
        assert(cleaningsListed.cleanings.isNotEmpty())

        val addMeasurement = AddMeasurement(license, Measurement(poolId = pool.id))
        assert( addMeasurement.isAddMeasurement() )
        val measurementAdded = handler.handle(addMeasurement) as MeasurementAdded
        assert( measurementAdded.isMeasurementAdded() )

        val measurement = measurementAdded.measurement
        assert( measurement.isMeasurement() )
        val updateMeasurement = UpdateMeasurement(license, measurement.copy(ph = 7.6))
        assert( updateMeasurement.isUpdateMeasurement() )
        val updatedMeasurement = handler.handle(updateMeasurement) as Updated
        assert( updatedMeasurement.isUpdated() )

        val listMeasurements = ListMeasurements(license, pool.id)
        assert( listMeasurements.isListMeasurements() )
        val measurementsListed = handler.handle(listMeasurements) as MeasurementsListed
        assert( measurementsListed.isMeasurementsListed() )
        assert(measurementsListed.measurements.isNotEmpty())

        val addChemical = AddChemical(license, Chemical(poolId = pool.id))
        assert( addChemical.isAddChemical() )
        val chemicalAdded = handler.handle(addChemical) as ChemicalAdded
        assert( chemicalAdded.isChemicalAdded() )

        val chemical = chemicalAdded.chemical
        assert( chemical.isChemical() )
        val updateChemical = UpdateChemical(license, chemical.copy(amount = 2.0))
        assert( updateChemical.isUpdateChemical() )
        val updatedChemical = handler.handle(updateChemical) as Updated
        assert( updatedChemical.isUpdated() )

        val listChemicals = ListChemicals(license, pool.id)
        assert( listChemicals.isListChemicals() )
        val chemicalsListed = handler.handle(listChemicals) as ChemicalsListed
        assert( chemicalsListed.isChemicalsListed() )
        assert(chemicalsListed.chemicals.isNotEmpty())
    }

    private fun test(store: Store) {
        val fault = Fault("test")
        assert( fault.isFault() )
        assert( store.addFault(fault) > 0 )
        assert( store.listFaults().size == 1 )
    }
}