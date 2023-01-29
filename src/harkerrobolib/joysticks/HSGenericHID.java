package harkerrobolib.joysticks;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID;

public class HSGenericHID extends GenericHID{

  private final int port;
  
  public HSGenericHID(int port) {
    super(port);
    this.port = port;
  }

  public boolean getRawButton(int button) {
    return DriverStation.getStickButton(port, (byte) button);
  }

  public boolean getRawButtonPressed(int button) {
    return DriverStation.getStickButtonPressed(port, (byte) button);
  }

  public boolean getRawButtonReleased(int button) {
    return DriverStation.getStickButtonReleased(port, button);
  }

  public double getRawAxis(int axis) {
    return DriverStation.getStickAxis(port, axis);
  }

  public int getPOV(int pov) {
    return DriverStation.getStickPOV(port, pov);
  }

  public int getPOV() {
    return getPOV(0);
  }

  public int getAxisCount() {
    return DriverStation.getStickAxisCount(port);
  }

  public int getPOVCount() {
    return DriverStation.getStickPOVCount(port);
  }

  public int getButtonCount() {
    return DriverStation.getStickButtonCount(port);
  }

  public boolean isConnected() {
    return DriverStation.isJoystickConnected(port);
  }

  public HIDType getType() {
    return HIDType.of(DriverStation.getJoystickType(port));
  }

  public String getName() {
    return DriverStation.getJoystickName(port);
  }

  public int getAxisType(int axis) {
    return DriverStation.getJoystickAxisType(port, axis);
  }

  public int getPort() {
    return port;
  }

}
