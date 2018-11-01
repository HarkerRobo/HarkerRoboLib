package harkerrobolib.wrappers

/**
 * Represents a standard Xbox controller.
 * @author Finn Frankis
 * @version 10/16/18
 */
class XboxGamepad(port: Int) : HSGamepad(port, A, B, X, Y, START, SELECT, STICK_LEFT, STICK_RIGHT, BUMPER_LEFT, BUMPER_RIGHT, LEFT_X, LEFT_Y, RIGHT_X, RIGHT_Y) {

    override val rightTrigger: Double
        get() = getRawAxis(RIGHT_TRIGGER)

    override val leftTrigger: Double
        get() = getRawAxis(LEFT_TRIGGER)

    companion object {
        val A = 1
        val B = 2
        val X = 3
        val Y = 4
        val SELECT = 7
        val START = 8

        val STICK_LEFT = 9
        val STICK_RIGHT = 10

        val BUMPER_LEFT = 5
        val BUMPER_RIGHT = 6

        val LEFT_X = 0
        val LEFT_Y = 1
        val RIGHT_X = 4
        val RIGHT_Y = 5
        val LEFT_TRIGGER = 2
        val RIGHT_TRIGGER = 3
    }
}
