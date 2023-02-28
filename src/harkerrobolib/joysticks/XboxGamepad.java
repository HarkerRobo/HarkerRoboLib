package harkerrobolib.joysticks;

import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * Represents a standard Xbox controller.
 *
 * @author Chiling Han
 * @version 01/30/2023
 */
public class XboxGamepad extends CommandXboxController {

  private static final int DPAD_UP_ANGLE = 0;
  private static final int DPAD_LEFT_ANGLE = 270;
  private static final int DPAD_RIGHT_ANGLE = 90;
  private static final int DPAD_DOWN_ANGLE = 180;

  public XboxGamepad(int port) {
    super(port);
  }

  public Trigger getButtonA() {
    return super.a();
  }

  public Trigger getButtonB() {
    return super.b();
  }

  public Trigger getButtonX() {
    return super.x();
  }

  public Trigger getButtonY() {
    return super.y();
  }

  public Trigger getRightBumper() {
    return super.rightBumper();
  }

  public Trigger getLeftBumper() {
    return super.leftBumper();
  }

  public Trigger getButtonStart() {
    return super.start();
  }

  public Trigger getButtonSelect() {
    return super.back();
  }

  public double getRightTrigger() {
    return super.getRightTriggerAxis();
  }

  public double getLeftTrigger() {
    return super.getLeftTriggerAxis();
  }

  public double getRightY() {
    return -super.getRightY();
  }

  public double getLeftY() {
    return -super.getLeftY();
  }

  public Trigger getUpDPadButton() {
    return super.povUp();
  }

  public Trigger getDownDPadButton() {
    return super.povDown();
  }

  public Trigger getLeftDPadButton() {
    return super.povLeft();
  }

  public Trigger getRightDPadButton() {
    return super.povRight();
  }

  public boolean getButtonAState() {
    return getButtonA().getAsBoolean();
  }

  public boolean getButtonBState() {
    return getButtonB().getAsBoolean();
  }

  public boolean getButtonXState() {
    return getButtonX().getAsBoolean();
  }

  public boolean getButtonYState() {
    return getButtonY().getAsBoolean();
  }

  public boolean getButtonStartState() {
    return getButtonStart().getAsBoolean();
  }

  public boolean getButtonSelectState() {
    return getButtonSelect().getAsBoolean();
  }

  public boolean getLeftBumperState() {
    return getLeftBumper().getAsBoolean();
  }

  public boolean getRightBumperState() {
    return getRightBumper().getAsBoolean();
  }

  public boolean getUpDPadButtonState() {
    return super.getHID().getPOV() == DPAD_UP_ANGLE;
  }

  public boolean getDownDPadButtonState() {
    return super.getHID().getPOV() == DPAD_DOWN_ANGLE;
  }

  public boolean getLeftDPadButtonState() {
    return super.getHID().getPOV() == DPAD_LEFT_ANGLE;
  }

  public boolean getRightDPadButtonState() {
    return super.getHID().getPOV() == DPAD_RIGHT_ANGLE;
  }

}
