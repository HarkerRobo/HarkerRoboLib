package harkerrobolib.wrappers;

/**
 * Represents the updated Logitech F310 controller with analog triggers.
 *
 * @author Finn Frankis
 * @version 10/27/18
 */
public class LogitechAnalogGamepad extends HSGamepad {

  public static final int A = 1;
  public static final int B = 2;
  public static final int X = 3;
  public static final int Y = 4;
  public static final int SELECT = 7;
  public static final int START = 8;

  public static final int STICK_LEFT = 9;
  public static final int STICK_RIGHT = 10;

  public static final int BUMPER_LEFT = 5;
  public static final int BUMPER_RIGHT = 6;

  public static final int LEFT_X = 0;
  public static final int LEFT_Y = 1;
  public static final int RIGHT_X = 4;
  public static final int RIGHT_Y = 5;
  public static final int TRIGGER_LEFT = 2;
  public static final int TRIGGER_RIGHT = 3;

  public LogitechAnalogGamepad(int port) {
    super(
        port,
        A,
        B,
        X,
        Y,
        START,
        SELECT,
        STICK_LEFT,
        STICK_RIGHT,
        BUMPER_LEFT,
        BUMPER_RIGHT,
        LEFT_X,
        LEFT_Y,
        RIGHT_X,
        RIGHT_Y);
  }

  public double getRightTrigger() {
    return getRawAxis(TRIGGER_RIGHT);
  }

  public double getLeftTrigger() {
    return getRawAxis(TRIGGER_LEFT);
  }
}
