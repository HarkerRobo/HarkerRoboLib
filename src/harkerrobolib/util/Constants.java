package harkerrobolib.util;

/**
 * Holds universal constants for use throughout the library.
 *
 * @author Finn Frankis
 * @version 10/21/18
 */
public final class Constants {
  public static final int DEFAULT_TIMEOUT = 10;
  public static final double MAX_VOLTAGE = 10;
  public static final double ROBOT_LOOP = 0.02;

  public static final int MAX_CAN_FRAME_PERIOD = 255;

  public static final int PID_PRIMARY = 0;
  public static final int PID_AUXILIARY = 1;

  public static final int SLOT_INDEX = 0;

  public static final double JOYSTICK_DEADBAND = 0.15;
  public static final double TRIGGER_DEADBAND = 0.1;

  public static final int DRIVER_ID = 0;
  public static final int OPERATOR_ID = 1;
}
