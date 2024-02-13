package objektwerks

import io.ktor.server.application.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

import java.time.Instant

fun main() {
    embeddedServer(Netty, port = 7979) {
        install(ContentNegotiation) {
            json()
        }
        routing {
            get ("/now") {
                call.respondText("Datetime: ${Instant.now()}")
            }
            post ("/command") {
                val command = call.receive<Command>()

            }
        }
    }.start(wait = true)
}