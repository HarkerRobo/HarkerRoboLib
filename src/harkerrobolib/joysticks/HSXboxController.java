package harkerrobolib.joysticks;

import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class HSXboxController extends CommandXboxController {

  public HSXboxController(int port) {
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
    return super.getHID().getPOV() == 0;
  }

  public boolean getDownDPadButtonState() {
    return super.getHID().getPOV() == 180;
  }

  public boolean getLeftDPadButtonState() {
    return super.getHID().getPOV() == 270;
  }

  public boolean getRightDPadButtonState() {
    return super.getHID().getPOV() == 90;
  }
}