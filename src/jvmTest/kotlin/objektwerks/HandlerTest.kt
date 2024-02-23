package objektwerks

import org.junit.Test

class HandlerTest {
    @Test
    fun handle() {
        val store = Store( StoreConfig.load("/store.yaml") )
        val handler = Handler(store)

        val register = Register("my@email.com")
        val registered = handler.handle(register) as Registered
        println(registered)
        assert( registered.account.isAccount() )

        val account = registered.account
        val login = Login(account.email, account.pin)
        val loggedIn = handler.handle(login) as LoggedIn
        assert( loggedIn.account.isAccount() )
        assert( loggedIn.account == registered.account )

        val license = account.license
        val addPool = AddPool(license, Pool(license = license, name = "pool-a"))
        val poolAdded = handler.handle(addPool) as PoolAdded
        assert( poolAdded.isValid() )

        val pool = poolAdded.pool
        val cleaning = Cleaning(poolId = pool.id)
        val addCleaning = AddCleaning(license, cleaning)
        val cleaningAdded = handler.handle(addCleaning) as CleaningAdded
        assert( cleaningAdded.isValid() )
    }
}