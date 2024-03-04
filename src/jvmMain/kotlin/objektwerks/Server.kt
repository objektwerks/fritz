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
            val emailer = Emailer( EmailerConfig.load("/emailer.yaml") )
            val handler = Handler(store, emailer)
            val port = args[0].toIntOrNull() ?: 7979
            Server().run(port, handler, store)
        }
    }

    fun run (port: Int, handler: Handler, store: Store): ApplicationEngine =
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

                    val event = handler.handle(command)
                    call.application.environment.log.info(event.toString())

                    val eventIsValid = event.isValid()
                    if (eventIsValid && event is Fault) store.addFault(event)

                    if (eventIsValid)
                        call.respond<Event>(event)
                    else {
                        val fault = Fault.build("Invalid event", event)
                        store.addFault(fault)
                        call.respond<Event>(fault)
                    }
                }
            }
        }.start(wait = true)
}