package objektwerks

fun License.isLicense(): Boolean =
    length == 36

fun Pin.isPin(): Boolean =
    length == 7

fun Account.isValid(): Boolean =
    (id > 0) &&
    (license.length == 36) &&
    (pin.length == 7) &&
    (email.contains("@"))

fun Register.isValid(): Boolean =
    (email.contains("@"))

fun Login.isValid(): Boolean =
    (email.contains("@")) &&
    (pin.length == 7)

fun Registered.isValid(): Boolean =
    account.isValid()

fun LoggedIn.isValid(): Boolean =
    account.isValid()