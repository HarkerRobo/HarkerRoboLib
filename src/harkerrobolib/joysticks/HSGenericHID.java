package harkerrobolib.joysticks;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.DriverStation;

import java.util.HashMap;
import java.util.Map;

public class HSGenericHID {
  public enum RumbleType {
    kLeftRumble,
    kRightRumble
  }

  public enum HIDType {
    kUnknown(-1),
    kXInputUnknown(0),
    kXInputGamepad(1),
    kXInputWheel(2),
    kXInputArcadeStick(3),
    kXInputFlightStick(4),
    kXInputDancePad(5),
    kXInputGuitar(6),
    kXInputGuitar2(7),
    kXInputDrumKit(8),
    kXInputGuitar3(11),
    kXInputArcadePad(19),
    kHIDJoystick(20),
    kHIDGamepad(21),
    kHIDDriving(22),
    kHIDFlight(23),
    kHID1stPerson(24);

    public final int value;

    @SuppressWarnings("PMD.UseConcurrentHashMap")
    private static final Map<Integer, HIDType> map = new HashMap<>();

    HIDType(int value) {
      this.value = value;
    }

    static {
      for (HIDType hidType : HIDType.values()) {
        map.put(hidType.value, hidType);
      }
    }

    public static HIDType of(int value) {
      return map.get(value);
    }
  }

  private final int port;
  private int m_outputs;
  private short m_leftRumble;
  private short m_rightRumble;
  
  public HSGenericHID(int port) {
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

  public void setOutput(int outputNumber, boolean value) {
    m_outputs = (m_outputs & ~(1 << (outputNumber - 1))) | ((value ? 1 : 0) << (outputNumber - 1));
    HAL.setJoystickOutputs((byte) port, m_outputs, m_leftRumble, m_rightRumble);
  }

  public void setOutputs(int value) {
    m_outputs = value;
    HAL.setJoystickOutputs((byte) port, m_outputs, m_leftRumble, m_rightRumble);
  }

  public void setRumble(RumbleType type, double value) {
    if (value < 0) {
      value = 0;
    } else if (value > 1) {
      value = 1;
    }
    if (type == RumbleType.kLeftRumble) {
      m_leftRumble = (short) (value * 65535);
    } else {
      m_rightRumble = (short) (value * 65535);
    }
    HAL.setJoystickOutputs((byte) port, m_outputs, m_leftRumble, m_rightRumble);
  }
}
