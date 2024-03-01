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
            val store = Store( StoreConfig.load("/store.yaml") )
            val handler = Handler(store)
            val port = args[0].toIntOrNull() ?: 7979
            Server().run(port, handler)
        }
    }

    fun run (port: Int, handler: Handler): ApplicationEngine =
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
                    val event = handler.handle(command)
                    if (event.isValid()) call.respond<Event>(event)
                    else call.respond<Event>( Fault.build("Invalid Event", event) )
                }
            }
        }.start(wait = true)
}