package objektwerks

import org.junit.Test

class HandlerTest {
    @Test
    fun handle() {
        val store = Store( StoreConfig.load("/store.yaml") )
        val handler = Handler(store)

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
        val pool = poolAdded.pool
        assert( addPool.isAddPool() )
        assert( poolAdded.isPoolAdded() )
        assert( pool.isPool() )

        val updatePool = UpdatePool(license, pool.copy(name = "pool-z"))
        val updatedPool = handler.handle(updatePool) as Updated
        assert( updatedPool.isUpdated() )

        val listPools = ListPools(license)
        val poolsListed = handler.handle(listPools)as PoolsListed
        assert( poolsListed.isPoolsListed() )
        assert( poolsListed.pools.size == 1 )

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
        assert( cleaningsListed.cleanings.size == 1 )

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
        assert( measurementsListed.measurements.size == 1 )

        val chemical = Chemical(poolId = pool.id)
        val addChemical = AddChemical(license, chemical)
        val chemicalAdded = handler.handle(addChemical) as ChemicalAdded
        assert( addChemical.isAddChemical() )
        assert( chemicalAdded.isChemicalAdded() )
    }
}