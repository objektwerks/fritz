package objektwerks

import java.util.Random

object Pin {
    private val specialChars = "~!@#$%^&*-+=<>?/:;".toList()
    private val random = Random()

    private fun newSpecialChar() : Char = specialChars( random.nextInt(specialChars.size) )

    fun newInstance() : String =
        Random.from(
            Random
                .alphanumeric
                .take(5)
                .mkString
                .prepended(newSpecialChar)
                .appended(newSpecialChar)
        ).mkString
}