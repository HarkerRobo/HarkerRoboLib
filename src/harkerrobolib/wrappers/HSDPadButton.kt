package harkerrobolib.wrappers

import edu.wpi.first.wpilibj.GenericHID


/**
 * Wraps a D-Pad button for convenient angle access.
 * @author Manan
 * @author Finn Frankis
 */
class HSDPadButton
/**
 * Create a joystick button for triggering commands
 * @param joystick The joystick to which this D-Pad corresponds.
 * @param degrees the angle on the D-Pad to which this button corresponds.
 */
(private val m_joystick: GenericHID, private val m_degrees: Int) : HSButton() {

    /**
     * Gets the value of the joystick button
     * @return The value of the joystick button
     */
    override fun get(): Boolean {
        return m_joystick.pov == m_degrees
    }
}

