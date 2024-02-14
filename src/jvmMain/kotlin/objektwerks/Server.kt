package objektwerks

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

import java.time.Instant

fun main() {
    val store = Store()
    val handler = Handler(store)
    val instance = server(7979, handler)
}

fun server(port: Int, handler: Handler): NettyApplicationEngine =
    embeddedServer(Netty, port = port) {
        install(ContentNegotiation) {
            json()
        }
        routing {
            get ("/now") {
                call.respondText("Datetime: ${Instant.now()}")
            }
            post ("/command") {
                val command = call.receive<Command>()
                val event = handler.handle(command)
                call.respond(event)
            }
        }
    }.start(wait = true)