package objektwerks

import kotlinx.serialization.Serializable

enum class UOM {
    gl, l, lb, kg, tablet
}

sealed interface Entity {
    val id: Id
}

typealias Id = Long
typealias License = String
typealias Pin = String
typealias Email = String
typealias Name = String
typealias Volume = Int

@Serializable
data class Account(override val id: Id,
                   val license: License,
                   val pin: Pin,
                   val email: Email) : Entity

@Serializable
data class Pool(override val id: Long,
                val license: License,
                val name: Name,
                val volume: Volume,
                val uom: String = UOM.gl.toString()) : Entity