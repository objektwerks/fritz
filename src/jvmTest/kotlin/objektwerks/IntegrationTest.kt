package objektwerks

import org.junit.Test

class IntegrationTest {
    @Test fun integration() {
        val store = Store()
        val handler = Handler(store)
        Server().run(7676, handler)
    }
}