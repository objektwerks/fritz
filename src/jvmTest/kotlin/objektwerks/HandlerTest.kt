package objektwerks

import kotlinx.coroutines.runBlocking
import org.junit.Test

class HandlerTest {
    @Test
    fun handle() {
        runBlocking {
            val store = Store( StoreConfig.load("/store.yaml") )
            val handler = Handler(store)

            test(handler)

            store.ddl().map { it.lowercase() }.forEach { println(it) } // lowercase fails! Why?
        }
    }

    private suspend fun test(handler: Handler) {
        val register = Register("my@email.com")
        val registered = handler.handle(register) as Registered
        assert( register.isRegister() )
        assert( registered.isRegistered() )

        val account = registered.account
        val license = account.license
        assert( account.isAccount() )
        assert( license.isLicense() )

        val login = Login(account.email, account.pin)
        val loggedIn = handler.handle(login) as LoggedIn
        assert( login.isLogin() )
        assert( loggedIn.isLoggedIn() )
        assert( loggedIn.account == account )

        val addPool = AddPool(license, Pool(license = license, name = "pool-a"))
        val poolAdded = handler.handle(addPool) as PoolAdded
        assert( addPool.isAddPool() )
        assert( poolAdded.isPoolAdded() )

        val pool = poolAdded.pool
        val updatePool = UpdatePool(license, pool.copy(name = "pool-z"))
        val updatedPool = handler.handle(updatePool) as Updated
        assert( updatedPool.isUpdated() )

        val listPools = ListPools(license)
        val poolsListed = handler.handle(listPools)as PoolsListed
        assert( poolsListed.isPoolsListed() )
        assert(poolsListed.pools.isNotEmpty())

        val addCleaning = AddCleaning(license, Cleaning(poolId = pool.id))
        val cleaningAdded = handler.handle(addCleaning) as CleaningAdded
        assert( addCleaning.isAddCleaning() )
        assert( cleaningAdded.isCleaingAdded() )

        val cleaning = cleaningAdded.cleaning
        val updateCleaning = UpdateCleaning(license, cleaning.copy(vacuum = true))
        val updatedCleaning = handler.handle(updateCleaning) as Updated
        assert( updatedCleaning.isUpdated() )

        val listCleanings = ListCleanings(license, pool.id)
        val cleaningsListed = handler.handle(listCleanings) as CleaningsListed
        assert( cleaningsListed.isCleaningsListed() )
        assert(cleaningsListed.cleanings.isNotEmpty())

        val addMeasurement = AddMeasurement(license, Measurement(poolId = pool.id))
        val measurementAdded = handler.handle(addMeasurement) as MeasurementAdded
        assert( addMeasurement.isAddMeasurement() )
        assert( measurementAdded.isMeasurementAdded() )

        val measurement = measurementAdded.measurement
        val updateMeasurement = UpdateMeasurement(license, measurement.copy(ph = 7.6))
        val updatedMeasurement = handler.handle(updateMeasurement) as Updated
        assert( updatedMeasurement.isUpdated() )

        val listMeasurements = ListMeasurements(license, pool.id)
        val measurementsListed = handler.handle(listMeasurements) as MeasurementsListed
        assert( measurementsListed.isMeasurementsListed() )
        assert(measurementsListed.measurements.isNotEmpty())

        val addChemical = AddChemical(license, Chemical(poolId = pool.id))
        val chemicalAdded = handler.handle(addChemical) as ChemicalAdded
        assert( addChemical.isAddChemical() )
        assert( chemicalAdded.isChemicalAdded() )

        val chemical = chemicalAdded.chemical
        val updateChemical = UpdateChemical(license, chemical.copy(amount = 2.0))
        val updatedChemical = handler.handle(updateChemical) as Updated
        assert( updatedChemical.isUpdated() )

        val listChemicals = ListChemicals(license, pool.id)
        val chemicalsListed = handler.handle(listChemicals) as ChemicalsListed
        assert( chemicalsListed.isChemicalsListed() )
        assert(chemicalsListed.chemicals.isNotEmpty())
    }
}