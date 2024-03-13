package objektwerks

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.cio.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

import java.time.Instant

class Server {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val port = args[0].toIntOrNull() ?: 7979
            Server().run( port, Exchanger() )
        }
    }

    fun run (port: Int, exchanger: Exchanger): ApplicationEngine =
        embeddedServer(CIO, port = port) {
            install(ContentNegotiation) {
                json()
            }
            install(CORS) {
                allowHost("0.0.0.0:$port")
            }
            routing {
                get ("/now") {
                    call.respondText("Datetime: ${Instant.now()}")
                }
                post ("/command") {
                    val command = call.receive<Command>()
                    val event = exchanger.exchange(command)
                    call.respond<Event>(event)
                }
            }
        }.start(wait = true)
}