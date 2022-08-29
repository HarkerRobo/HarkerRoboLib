package harkerrobolib.joysticks;

import edu.wpi.first.wpilibj.GenericHID;

/**
 * Wraps a D-Pad button for convenient angle access.
 *
 * @author Manan
 * @author Finn Frankis
 */
public class HSDPadButton extends HSButton {
  private final GenericHID m_joystick;
  private final int m_degrees;

  /**
   * Create a joystick button for triggering commands
   *
   * @param joystick The joystick to which this D-Pad corresponds.
   * @param degrees the angle on the D-Pad to which this button corresponds.
   */
  public HSDPadButton(GenericHID joystick, int degrees) {
    m_joystick = joystick;
    m_degrees = degrees;
  }

  /**
   * Gets the value of the joystick button
   *
   * @return The value of the joystick button
   */
  public boolean get() {
    return m_joystick.getPOV() == m_degrees;
  }
}
