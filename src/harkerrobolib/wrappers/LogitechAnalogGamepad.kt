package harkerrobolib.wrappers

/**
 * Represents the updated Logitech F310 controller with analog triggers.
 * @author Finn Frankis
 * @version 10/27/18
 */
class LogitechAnalogGamepad(port: Int) : HSGamepad(port, A, B, X, Y, START, SELECT, STICK_LEFT, STICK_RIGHT, BUMPER_LEFT, BUMPER_RIGHT, LEFT_X, LEFT_Y, RIGHT_X, RIGHT_Y) {

    override val rightTrigger: Double
        get() = getRawAxis(TRIGGER_RIGHT)

    override val leftTrigger: Double
        get() = getRawAxis(TRIGGER_LEFT)

    companion object {

        val A = 1
        val B = 2
        val X = 3
        val Y = 4
        val SELECT = 9
        val START = 10

        val STICK_LEFT = 11
        val STICK_RIGHT = 12

        val BUMPER_LEFT = 5
        val BUMPER_RIGHT = 6

        val LEFT_X = 0
        val LEFT_Y = 1
        val RIGHT_X = 2
        val RIGHT_Y = 5
        val TRIGGER_LEFT = 2
        val TRIGGER_RIGHT = 3
    }
}
