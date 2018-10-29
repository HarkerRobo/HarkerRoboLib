package harkerrobolib.wrappers

/**
 * Represents a standard Logitech controller.
 * @author Finn Frankis
 * @version 10/16/18
 */
class LogitechGamepad(port: Int) : GamepadWrapper(port, A, B, X, Y, START, SELECT, STICK_LEFT, STICK_RIGHT, BUMPER_LEFT, BUMPER_RIGHT, LEFT_X, LEFT_Y, RIGHT_X, RIGHT_Y) {

    override val leftTrigger: Double
        get() = (if (getRawButton(TRIGGER_LEFT) == true) 1 else 0).toDouble()

    override val rightTrigger: Double
        get() = (if (getRawButton(TRIGGER_RIGHT) == true) 1 else 0).toDouble()

    companion object {
        val A = 2
        val B = 3
        val X = 1
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
        val RIGHT_Y = 3
        val TRIGGER_LEFT = 7
        val TRIGGER_RIGHT = 8
    }
}
