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
    isNotEmpty()

fun Additive.isAdditive(): Boolean =
    isNotEmpty()

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

fun Cleaning.isCleaning(): Boolean =
    id.isId() &&
    cleaned.isEpochSeconds()

fun Chemical.isChemical(): Boolean =
    id.isId() &&
    poolId > 0 &&
    chemical.isAdditive() &&
    amount > 0.00 &&
    uom.isUoM() &&
    added.isEpochSeconds()

fun Command.isValid(): Boolean =
    when(this) {
        is Register -> isRegister()
        is Login -> isLogin()
        is ListPools -> isListPools()
        is AddPool -> isAddPool()
        is UpdatePool -> isUpdatePool()
        is ListCleanings -> isListCleanings()
        is AddCleaning -> isAddCleaning()
        is UpdateCleaning -> isUpdateCleaning()
        is ListChemicals -> isListChemicals()
        is AddChemical -> isAddChemical()
        is UpdateChemical -> isUpdateChemical()
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

fun UpdatePool.isUpdatePool(): Boolean =
    license.isLicense() &&
    pool.id > 0

fun ListCleanings.isListCleanings(): Boolean =
    license.isLicense()

fun AddCleaning.isAddCleaning(): Boolean =
    license.isLicense() &&
    cleaning.id == 0L &&
    cleaning.poolId > 0

fun UpdateCleaning.isUpdateCleaning(): Boolean =
    license.isLicense() &&
    cleaning.id > 0 &&
    cleaning.poolId > 0

fun ListChemicals.isListChemicals(): Boolean =
    license.isLicense()

fun AddChemical.isAddChemical(): Boolean =
    license.isLicense() &&
    chemical.id == 0L &&
    chemical.poolId > 0

fun UpdateChemical.isUpdateChemical(): Boolean =
    license.isLicense() &&
    chemical.id > 0 &&
    chemical.poolId > 0

fun Event.isValid(): Boolean =
    when(this) {
        is Registered -> isRegistered()
        is LoggedIn -> isLoggedIn()
        is Updated -> isUpdated()
        is PoolsListed -> isPoolsListed()
        is PoolAdded -> isPoolAdded()
        is CleaningsListed -> isCleaningsListed()
        is CleaningAdded -> isCleaingAdded()
        is ChemicalsListed -> isChemicalsListed()
        is ChemicalAdded -> isChemicalAdded()
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

fun CleaningsListed.isCleaningsListed(): Boolean =
    cleanings.size >= 0

fun CleaningAdded.isCleaingAdded(): Boolean =
    cleaning.isCleaning()

fun ChemicalsListed.isChemicalsListed(): Boolean =
    chemicals.size >= 0

fun ChemicalAdded.isChemicalAdded(): Boolean =
    chemical.isChemical()

fun Fault.isFault(): Boolean =
    error.isNotEmpty()