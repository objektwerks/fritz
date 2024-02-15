package objektwerks

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*

import kotlinx.coroutines.runBlocking

import org.junit.Test

class IntegrationTest {
    @Test fun integration() {
        val store = Store()
        val handler = Handler(store)
        val server = Server().run(7676, handler)
        val client = HttpClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val url = "http://localhost:7676/command"

        val register = Register("my@email.com")
        val registered = runBlocking {
            client.post(url) {
                contentType(ContentType.Application.Json)
                setBody(register)
            }.body<Registered>()
        }
        val account = registered.account
        assert( account.id > 0 )

        val login = Login(account.email, account.pin)
        val loggedIn = runBlocking {
            client.post(url) {
                contentType(ContentType.Application.Json)
                setBody(login)
            }.body<LoggedIn>()
        }
        assert( loggedIn.account.id > 0 )

        server.stop(1000, 1000)
    }
}