package harkerrobolib.commands

class ThrowException(e: RuntimeException) : CallMethod({ throwException(e) }) {

    constructor(s: String) : this(RuntimeException(s)) {}

    companion object {
        fun throwException(e: RuntimeException) {
            throw e
        }
    }

}
