package harkerrobolib.wrappers

import edu.wpi.first.wpilibj.Joystick

/**
 * A Joystick wrapper for gamepads that include more accurate/useful names for the analogue stick axes
 * @author neymikajain
 * @author atierno
 */
abstract class HSGamepad(port: Int,
                         buttonAPort: Int, buttonBPort: Int, buttonXPort: Int, buttonYPort: Int,
                         buttonStartPort: Int, buttonSelectPort: Int,
                         buttonStickLeftPort: Int, buttonStickRightPort: Int,
                         buttonBumperLeftPort: Int, buttonBumperRightPort: Int,
                         private val axisLeftX: Int, private val axisLeftY: Int, private val axisRightX: Int, private val axisRightY: Int) : Joystick(port) {
    /**
     * Gets an instance of Button A
     * @return An instance of the button
     */
    val buttonA: HSJoystickButton
    /**
     * Gets an instance of Button B
     * @return An instance of the button
     */
    val buttonB: HSJoystickButton
    /**
     * Gets an instance of Button X
     * @return An instance of the button
     */
    val buttonX: HSJoystickButton
    /**
     * Gets an instance of Button Y
     * @return An instance of the button
     */
    val buttonY: HSJoystickButton
    /**
     * Gets an instance of the Start Button
     * @return An instance of the button
     */
    val buttonStart: HSJoystickButton
    /**
     * Gets an instance of the Select Button
     * @return An instance of the button
     */
    val buttonSelect: HSJoystickButton
    /**
     * Gets an instance of the Left Stick Button
     * @return An instance of the button
     */
    val buttonStickLeft: HSJoystickButton
    /**
     * Gets an instance of the Right Stick Button
     * @return An instance of the button
     */
    val buttonStickRight: HSJoystickButton
    /**
     * Gets an instance of the Left Bumper
     * @return An instance of the button
     */
    val buttonBumperLeft: HSJoystickButton
    /**
     * Gets an instance of the Right Bumper
     * @return An instance of the button
     */
    val buttonBumperRight: HSJoystickButton

    /**
     * Gets the X value being input to the left joystick.
     * @return the left X value
     */
    val leftX: Double
        get() = getRawAxis(axisLeftX)

    /**
     * Gets the Y value being input to the left joystick.
     * @return the left Y value
     */
    //by default, forward returns a negative number, which is unintuitive
    val leftY: Double
        get() = -getRawAxis(axisLeftY)

    /**
     * Gets the Y value being input to the right joystick.
     * @return the left Y value
     */
    val rightX: Double
        get() = getRawAxis(axisRightX)

    /**
     * Gets the Y value being input to the right joystick.
     * @return the right Y value
     */
    //by default, forward returns a negative number, which is unintuitive
    val rightY: Double
        get() = -getRawAxis(axisRightY)

    /**
     * Gets the amount the right trigger is currently being pressed.
     * @return the amount by which the right trigger is pressed
     */
    abstract val rightTrigger: Double

    /**
     * Gets the amount the left trigger is currently being pressed.
     * @return the amount by which the left trigger is pressed
     */
    abstract val leftTrigger: Double

    /**
     * Gets whether or not Button A is pressed
     * @return The state of the button
     */
    val buttonAState: Boolean
        get() = buttonA.get()

    /**
     * Gets whether or not Button B is pressed
     * @return The state of the button
     */
    val buttonBState: Boolean
        get() = buttonB.get()

    /**
     * Gets whether or not Button X is pressed
     * @return The state of the button
     */
    val buttonXState: Boolean
        get() = buttonX.get()

    /**
     * Gets whether or not Button Y is pressed
     * @return The state of the button
     */
    val buttonYState: Boolean
        get() = buttonY.get()

    /**
     * Gets whether or not the Start Button is pressed
     * @return The state of the button
     */
    val buttonStartState: Boolean
        get() = buttonStart.get()

    /**
     * Gets whether or not the Select Button is pressed
     * @return The state of the button
     */
    val buttonSelectState: Boolean
        get() = buttonSelect.get()

    /**
     * Gets whether or not the Left Stick Button is pressed
     * @return The state of the button
     */
    val buttonStickLeftState: Boolean
        get() = buttonStickLeft.get()

    /**
     * Gets whether or not the Right Stick Button is pressed
     * @return The state of the button
     */
    val buttonStickRightState: Boolean
        get() = buttonStickRight.get()

    /**
     * Gets whether or not the Left Bumper is pressed
     * @return The state of the button
     */
    val buttonBumperLeftState: Boolean
        get() = buttonBumperLeft.get()

    /**
     * Gets whether or not the Right Bumper is pressed
     * @return The state of the button
     */
    val buttonBumperRightState: Boolean
        get() = buttonBumperRight.get()

    init {
        buttonA = HSJoystickButton(this, buttonAPort)
        buttonB = HSJoystickButton(this, buttonBPort)
        buttonX = HSJoystickButton(this, buttonXPort)
        buttonY = HSJoystickButton(this, buttonYPort)
        buttonStart = HSJoystickButton(this, buttonStartPort)
        buttonSelect = HSJoystickButton(this, buttonSelectPort)
        buttonStickLeft = HSJoystickButton(this, buttonStickLeftPort)
        buttonStickRight = HSJoystickButton(this, buttonStickRightPort)
        buttonBumperLeft = HSJoystickButton(this, buttonBumperLeftPort)
        buttonBumperRight = HSJoystickButton(this, buttonBumperRightPort)
    }
}
