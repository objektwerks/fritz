package objektwerks

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.java.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*

import kotlinx.coroutines.runBlocking

import org.junit.Test

class IntegrationTest {
    @Test fun integration() {
        println("*** init starting")
        val store = Store()
        println("*** store started")
        val handler = Handler(store)
        println("*** handler started")
        val server = Server().run(7676, handler, false)
        println("*** server started")
        val client = HttpClient(Java) {
            install(ContentNegotiation) {
                json()
            }
        }
        println("*** client started")
        val url = "http://localhost:7676/command"
        println("*** init complete")

        val register = Register("my@email.com")
        val registered = runBlocking {
            client.post(url) {
                contentType(ContentType.Application.Json)
                setBody(register)
            }.body<Registered>()
        }
        val account = registered.account
        assert( account.id > 0 )
        println("*** register pass")

        val login = Login(account.email, account.pin)
        val loggedIn = runBlocking {
            client.post(url) {
                contentType(ContentType.Application.Json)
                setBody(login)
            }.body<LoggedIn>()
        }
        assert( loggedIn.account.id > 0 )
        println("*** login pass")

        server.stop(1000, 1000)
        println("*** server stopped")
    }
}