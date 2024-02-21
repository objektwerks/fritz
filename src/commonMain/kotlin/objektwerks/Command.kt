package objektwerks

import kotlinx.serialization.Serializable

sealed interface Command

sealed interface Licensed {
    val license: String
}

@Serializable
data class Register(val email: Email) : Command

@Serializable
data class Login(val email: Email, val pin: Pin) : Command

@Serializable
data class ListPools(override val license: String) : Command, Licensed

@Serializable
data class AddPool(override val license: String, val pool: Pool) : Command, Licensed

@Serializable
data class UpdatePool(override val license: String, val pool: Pool) : Command, Licensed

@Serializable
data class ListCleanings(override val license: String, val poolId: Id) : Command, Licensed

@Serializable
data class AddCleaning(override val license: String, val cleaning: Cleaning) : Command, Licensed

@Serializable
data class UpdateCleaning(override val license: String, val cleaning: Cleaning) : Command, Licensed

@Serializable
data class ListMeasurements(override val license: String, val poolId: Id) : Command, Licensed

@Serializable
data class AddMeasurement(override val license: String, val measurement: Measurement) : Command, Licensed

@Serializable
data class UpdateMeasurement(override val license: String, val measurement: Measurement) : Command, Licensed

@Serializable
data class ListChemicals(override val license: String, val poolId: Id) : Command, Licensed

@Serializable
data class AddChemical(override val license: String, val chemical: Chemical) : Command, Licensed

@Serializable
data class UpdateChemical(override val license: String, val chemical: Chemical) : Command, Licensed