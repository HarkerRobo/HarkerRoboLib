package harkerrobolib.joysticks;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

/**
 * Makes a JoystickButton with an Xbox Controller Trigger
 *
 * @author Chirag Kaushik
 * @author Jatin Kohli
 * @author Shahzeb Lakhani
 * @author Angela Jia
 * @since February 17, 2020
 */
public class HSTriggerButton extends JoystickButton {
  private static final double TRIGGER_DEADBAND = 0.1;

  private int port;
  private GenericHID joystick;

  public HSTriggerButton(GenericHID joystick, int port) {
    super(joystick, -1); // values do not matter
    this.joystick = joystick;
    this.port = port;
  }

  public double getAnalog() {
    return joystick.getRawAxis(port);
  }

  @Override
  public boolean getAsBoolean() {
    return getAnalog() > TRIGGER_DEADBAND;
  }

  public void whilePressed(Command c) {
    onTrue(c);
    onFalse(new InstantCommand(() -> c.cancel()));
  }
}
