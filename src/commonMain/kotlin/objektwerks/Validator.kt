package objektwerks

fun Account.isValid(): Boolean =
    (id > 0) &&
    (license.length == 36) &&
    (pin.length == 7) &&
    (email.contains("@"))