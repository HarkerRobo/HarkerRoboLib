package harkerrobolib.commands

import java.io.File
import java.security.InvalidParameterException

import edu.wpi.first.wpilibj.command.Command

class ThrowExceptionCommand(e: RuntimeException) : CallMethodCommand({ throwException(e) }) {

    constructor(s: String) : this(RuntimeException(s)) {}

    companion object {
        fun throwException(e: RuntimeException) {
            throw e
        }
    }

}
