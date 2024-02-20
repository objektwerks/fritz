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
        is Register -> isRegister()
        is Login -> isLogin()
        is ListPools -> isListPools()
        is AddPool -> isAddPool()
        is UpdatePool -> TODO()
    }

fun Register.isRegister(): Boolean =
    email.isEmail()

fun Login.isLogin(): Boolean =
    email.isEmail() &&
    pin.isPin()

fun ListPools.isListPools(): Boolean =
    license.isLicense()

fun AddPool.isAddPool(): Boolean =
    license.isLicense() &&
    pool.id == 0L

fun Event.isValid(): Boolean =
    when(this) {
        is Registered -> isRegistered()
        is LoggedIn -> isLoggedIn()
        is Updated -> isUpdated()
        is PoolsListed -> isPoolsListed()
        is PoolAdded -> isPoolAdded()
        is Fault -> isFault()
    }

fun Registered.isRegistered(): Boolean =
    account.isAccount()

fun LoggedIn.isLoggedIn(): Boolean =
    account.isAccount()

fun Updated.isUpdated(): Boolean =
    count == 1

fun PoolsListed.isPoolsListed(): Boolean =
    pools.size >= 0

fun PoolAdded.isPoolAdded(): Boolean =
    pool.isPool()

fun Fault.isFault(): Boolean =
    error.isNotEmpty()