package objektwerks

fun Id.isId(): Boolean =
    this > 0

fun License.isLicense(): Boolean =
    length == 36

fun Pin.isPin(): Boolean =
    length == 7

fun Email.isEmail(): Boolean =
    contains("@")

fun Volume.isVolume(): Boolean =
    this >= 100

fun UoM.isUoM(): Boolean =
    this.isNotEmpty()

fun EpochSeconds.isEpochSeconds(): Boolean =
    this > 0

fun Account.isValid(): Boolean =
    id.isId() &&
    license.isLicense() &&
    pin.isPin() &&
    email.isEmail() &&
    created.isEpochSeconds()

fun Pool.isValid(): Boolean =
    id.isId() &&
    license.isLicense() &&
    name.isNotEmpty() &&
    volume.isVolume() &&
    uom.isUoM()

fun Command.isValid(): Boolean =
    when(this) {
        is Register -> this.isValid()
        is Login -> this.isValid()
        is ListPools -> TODO()
        is AddPool -> TODO()
        is UpdatePool -> TODO()
    }

fun Register.isValid(): Boolean =
    email.isEmail()

fun Login.isValid(): Boolean =
    email.isEmail() &&
    pin.isPin()

fun Event.isValid(): Boolean =
    when(this) {
        is Registered -> this.isValid()
        is LoggedIn -> this.isValid()
        is Fault -> this.isValid()
        is PoolsListed -> TODO()
        is PoolAdded -> TODO()
        is Updated -> TODO()
    }

fun Registered.isValid(): Boolean =
    account.isValid()

fun LoggedIn.isValid(): Boolean =
    account.isValid()

fun Fault.isValid(): Boolean =
    (error.isNotEmpty())