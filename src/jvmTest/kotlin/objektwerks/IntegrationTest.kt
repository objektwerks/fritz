package objektwerks

import org.junit.Test

class IntegrationTest {
    @Test fun integration() {
        val store = Store()
        val handler = Handler(store)

        val register = Register("my@email.com")
        val registered = handler.handle(register) as Registered
        println(registered)
        assert( registered.account.id > 0 )

        val account = registered.account
        val login = Login(account.email, account.pin)
        val loggedIn = handler.handle(login) as LoggedIn
        assert( loggedIn.account == registered.account )
    }
}