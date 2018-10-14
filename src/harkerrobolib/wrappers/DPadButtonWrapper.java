package harkerrobolib.wrappers;

import edu.wpi.first.wpilibj.GenericHID;


/**
 *
 * @author Manan
 */
public class DPadButtonWrapper extends ButtonWrapper{
    private final GenericHID m_joystick;
    private final int m_degrees;

    /**
     * Create a joystick button for triggering commands
     * @param joystick The GenericHID object that has the button (e.g. Joystick, KinectStick, etc)
     * @param buttonNumber The button number (see {@link GenericHID#getRawButton(int) }
     */
    public DPadButtonWrapper(GenericHID joystick, int degrees) {
        m_joystick = joystick;
        m_degrees = degrees;
    }
    
    /**
     * Gets the value of the joystick button
     * @return The value of the joystick button
     */
    public boolean get() {
        return m_joystick.getPOV() == m_degrees;
    }
}

