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

        val cleaning = Cleaning(poolId = pool.id)
        val addCleaning = AddCleaning(license, cleaning)
        val cleaningAdded = handler.handle(addCleaning) as CleaningAdded
        assert( addCleaning.isAddCleaning() )
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