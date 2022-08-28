package harkerrobolib.wrappers;

/**
 * Represents a standard Logitech controller.
 *
 * @author Finn Frankis
 * @version 10/16/18
 */
public class LogitechGamepad extends HSGamepad {
  public static final int A = 2;
  public static final int B = 3;
  public static final int X = 1;
  public static final int Y = 4;
  public static final int SELECT = 9;
  public static final int START = 10;

  public static final int STICK_LEFT = 11;
  public static final int STICK_RIGHT = 12;

  public static final int BUMPER_LEFT = 5;
  public static final int BUMPER_RIGHT = 6;

  public static final int LEFT_X = 0;
  public static final int LEFT_Y = 1;
  public static final int RIGHT_X = 2;
  public static final int RIGHT_Y = 3;
  public static final int TRIGGER_LEFT = 7;
  public static final int TRIGGER_RIGHT = 8;

  public LogitechGamepad(int port) {
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

  public double getLeftTrigger() {
    return (getRawButton(TRIGGER_LEFT) == true) ? 1 : 0;
  }

  public double getRightTrigger() {
    return (getRawButton(TRIGGER_RIGHT) == true) ? 1 : 0;
  }
}
