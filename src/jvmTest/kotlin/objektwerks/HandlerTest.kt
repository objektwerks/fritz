package objektwerks

import org.junit.Test

class HandlerTest {
    @Test
    fun handle() {
        val store = Store( StoreConfig.load("/store.yaml") )
        val handler = Handler(store)

        val register = Register("my@email.com")
        val registered = handler.handle(register) as Registered
        assert( registered.account.isAccount() )

        val account = registered.account
        val license = account.license

        val login = Login(account.email, account.pin)
        val loggedIn = handler.handle(login) as LoggedIn
        assert( loggedIn.account.isAccount() )

        val addPool = AddPool(license, Pool(license = license, name = "pool-a"))
        val poolAdded = handler.handle(addPool) as PoolAdded
        assert( poolAdded.isPoolAdded() )

        val pool = poolAdded.pool

        val cleaning = Cleaning(poolId = pool.id)
        val addCleaning = AddCleaning(license, cleaning)
        val cleaningAdded = handler.handle(addCleaning) as CleaningAdded
        assert( cleaningAdded.isCleaingAdded() )

        val measurement = Measurement(poolId = pool.id)
        val addMeasurement = AddMeasurement(license, measurement)
        val measurementAdded = handler.handle(addMeasurement) as MeasurementAdded
        assert( measurementAdded.isMeasurementAdded() )

        val chemical = Chemical(poolId = pool.id)
        val addChemical = AddChemical(license, chemical)
        val chemicalAdded = handler.handle(addChemical) as ChemicalAdded
        assert( chemicalAdded.isChemicalAdded() )
    }
}