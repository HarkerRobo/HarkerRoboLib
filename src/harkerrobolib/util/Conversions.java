package harkerrobolib.util;

/**
 * Converts from native units of motor to metric units
 *
 * @author Chiling Han
 * @version 01/30/2023
 */
public class Conversions {
    public static enum System {
      ANGLE,
      POSITION,
      VELOCITY
    }
    /*
     * NUMBER_IN_CONVERSIONUNIT * CONVERSIONUNIT_TO_OTHERUNIT = NUMBER_IN_OTHERUNIT
     */
    public static final double INCH_TO_METER = 0.0254; // 1 inch is 0.0254 meters
    public static final double FOOT_TO_INCH = 12.0; // 1 foot is 12 inches
    public static final double FXTICK_TO_ROT = 2048.0; 
    public static final double FXTICK_TO_DEG = 360.0 / FXTICK_TO_ROT;
    public static final double SEC_TO_100MS = 10.0; // 1 second is 10 100ms

    /*
     * TalonFX motor to subsystem
     */
    public static double conversionConstant(System type, double gearRatio, double diameterInInches) {
      double convConstant = 0;
      
      switch (type) {
        case ANGLE:
          convConstant = FXTICK_TO_DEG;
          break;
        case POSITION:
          convConstant = Math.PI * INCH_TO_METER * diameterInInches / FXTICK_TO_ROT;
          break;
        case VELOCITY:
          convConstant = Math.PI * INCH_TO_METER * diameterInInches / FXTICK_TO_ROT * SEC_TO_100MS;
          break;
      }

      convConstant /= gearRatio;

      return convConstant;

    }

}