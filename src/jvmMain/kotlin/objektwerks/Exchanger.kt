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

    private fun validateCommand(command: Command): Fault? {
        logger.info(command.toString())
        if (command.isValid())
            return null
        else {
            val fault = Fault.build("Invalid command", command)
            store.addFault(fault)
            logger.error(fault.toString())
            return fault
        }
    }

    private suspend fun validateLicense(command: Command): Fault? {
        if (command.isLicensed())
            return null
        else {
            val fault = Fault.build("Invalid license", command)
            store.addFault(fault)
            logger.error(fault.toString())
            return fault
        }
    }

    private fun validateEvent(event: Event): Fault? {
        logger.info(event.toString())
        val eventIsValid = event.isValid()
        if (eventIsValid && event is Fault) store.addFault(event)
        if (eventIsValid)
            return null
        else {
            val fault = Fault.build("Invalid event", event)
            logger.error(fault.toString())
            store.addFault(fault)
            return fault
        }
    }

    suspend fun exchange(command: Command): Event {
        validateCommand(command)?.let { return it }
        validateLicense(command)?.let { return it }
        val event = handler.handle(command)
        validateEvent(event)?.let { return it }
        return event
    }
}