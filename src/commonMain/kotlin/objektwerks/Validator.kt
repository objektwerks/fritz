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

fun Account.isAccount(): Boolean =
    id.isId() &&
    license.isLicense() &&
    pin.isPin() &&
    email.isEmail() &&
    created.isEpochSeconds()

fun Pool.isPool(): Boolean =
    id.isId() &&
    license.isLicense() &&
    name.isNotEmpty() &&
    volume.isVolume() &&
    uom.isUoM()

fun Command.isValid(): Boolean =
    when(this) {
        is Register -> this.isRegister()
        is Login -> this.isLogin()
        is ListPools -> TODO()
        is AddPool -> TODO()
        is UpdatePool -> TODO()
    }

fun Register.isRegister(): Boolean =
    email.isEmail()

fun Login.isLogin(): Boolean =
    email.isEmail() &&
    pin.isPin()

fun Event.isValid(): Boolean =
    when(this) {
        is Registered -> this.isRegistered()
        is LoggedIn -> this.isLoggedIn()
        is Updated -> this.isUpdated()
        is PoolsListed -> TODO()
        is PoolAdded -> TODO()
        is Fault -> this.isFault()
    }

fun Registered.isRegistered(): Boolean =
    account.isAccount()

fun LoggedIn.isLoggedIn(): Boolean =
    account.isAccount()

fun Updated.isUpdated(): Boolean =
    count == 1

fun Fault.isFault(): Boolean =
    error.isNotEmpty()