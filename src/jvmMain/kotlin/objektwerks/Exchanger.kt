package objektwerks

import org.slf4j.LoggerFactory

class Exchanger {
    private val logger = LoggerFactory.getLogger("Exchange")
    private val store = Store( StoreConfig.load("/store.yaml") )
    private val emailer = Emailer( EmailerConfig.load("/emailer.yaml") )
    private val handler = Handler(store, emailer)

    private suspend fun Command.isLicensed(): Boolean =
        when(this) {
            is Licensed -> store.isLicensed(this.license)
            is Register -> true
            is Login    -> true
        }

    private fun logAndStoreFault(fault: Fault): Fault {
        logger.error( fault.toString() )
        store.addFault(fault)
        return fault
    }

    private fun validateCommand(command: Command): Fault? {
        logger.info( command.toString() )
        return if ( command.isValid() ) null
        else logAndStoreFault( Fault.build("Invalid command", command) )
    }

    private suspend fun validateLicense(command: Command): Fault? =
        if ( command.isLicensed() ) null
        else logAndStoreFault( Fault.build("Invalid license", command) )

    private fun validateEvent(event: Event): Fault? {
        logger.info( event.toString() )
        val eventIsValid = event.isValid()
        if (eventIsValid && event is Fault) store.addFault(event)
        return if (eventIsValid) null
        else logAndStoreFault( Fault.build("Invalid event", event) )
    }

    suspend fun exchange(command: Command): Event {
        validateCommand(command)?.let { return it }
        validateLicense(command)?.let { return it }
        val event = handler.handle(command)
        validateEvent(event)?.let { return it } ?: return event
    }
}