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

    private fun validate(command: Command): Fault? =
        if (!command.isValid()) {
            val fault = Fault.build("Invalid command", command)
            logger.error(fault.toString())
            fault
        } else null

    private fun validate(event: Event): Fault? {
        val eventIsValid = event.isValid()
        if (eventIsValid && event is Fault) store.addFault(event)
        if (!eventIsValid) {
            val fault = Fault.build("Invalid event", event)
            logger.error(fault.toString())
            store.addFault(fault)
            return fault
        } else return null
    }


    suspend fun exchange(command: Command): Event {
        logger.info(command.toString())

        if (!command.isValid()) {
            val fault = Fault.build("Invalid command", command)
            logger.error(fault.toString())
            return fault
        }

        if (!command.isLicensed()) {
            val fault = Fault.build("Invalid license", command)
            logger.error(fault.toString())
            return fault
        }

        val event = handler.handle(command)
        logger.info(event.toString())

        val eventIsValid = event.isValid()
        if (eventIsValid && event is Fault) store.addFault(event)

        if (eventIsValid)
            return event
        else {
            val fault = Fault.build("Invalid event", event)
            logger.error(fault.toString())
            store.addFault(fault)
            return fault
        }
    }
}