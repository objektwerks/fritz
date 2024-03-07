package objektwerks

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.cio.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

import java.time.Instant

class Server {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val exchange = Exchange()
            val port = args[0].toIntOrNull() ?: 7979
            Server().run(port, exchange)
        }
    }

    fun run (port: Int, exchange: Exchange): ApplicationEngine =
        embeddedServer(CIO, port = port) {
            install(ContentNegotiation) {
                json()
            }
            routing {
                get ("/now") {
                    call.respondText("Datetime: ${Instant.now()}")
                }
                post ("/command") {
                    val command = call.receive<Command>()
                    call.application.environment.log.info(command.toString())

                    val event = exchange.exchange(command)
                    call.application.environment.log.info(event.toString())
                    call.respond<Event>(event)
                }
            }
        }.start(wait = true)
}