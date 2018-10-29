package harkerrobolib.wrappers

import edu.wpi.first.wpilibj.GenericHID


/**
 *
 * @author Manan
 */
class JoystickButtonWrapper
/**
 * Create a joystick button for triggering commands
 * @param joystick The GenericHID object that has the button (e.g. Joystick, KinectStick, etc)
 * @param buttonNumber The button number (see [GenericHID.getRawButton]
 */
(private val m_joystick: GenericHID, private val m_buttonNumber: Int) : ButtonWrapper() {

    /**
     * Gets the value of the joystick button
     * @return The value of the joystick button
     */
    override fun get(): Boolean {
        return m_joystick.getRawButton(m_buttonNumber)
    }
}

