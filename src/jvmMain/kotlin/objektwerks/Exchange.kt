package objektwerks

class Exchange {
    private val store = Store( StoreConfig.load("/store.yaml") )
    private val emailer = Emailer( EmailerConfig.load("/emailer.yaml") )
    private val handler = Handler(store, emailer)

    private suspend fun Command.isLicensed(): Boolean =
        when(this) {
            is Licensed -> store.isLicensed(this.license)
            is Register -> true
            is Login    -> true
        }

    suspend fun exchange(command: Command): Event {
        if (!command.isValid()) return Fault.build("Invalid command", command)
        if (!command.isLicensed()) return Fault.build("Invalid license", command)

        val event = handler.handle(command)

        val eventIsValid = event.isValid()
        if (eventIsValid && event is Fault) store.addFault(event)

        if (eventIsValid)
            return event
        else {
            val fault = Fault.build("Invalid event", event)
            store.addFault(fault)
            return fault
        }
    }
}