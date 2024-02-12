package objektwerks

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

import java.time.Instant

class Server {
    companion object {
        @JvmStatic fun main(args: Array<String>): Unit = Server().run()
    }

    fun run() {
        val server = embeddedServer(Netty, port = 7979) {
            routing {
                get("/now") {
                    call.respondText("Datetime: ${Instant.now()}")
                }
            }
        }
        server.start(wait = true)
    }
}