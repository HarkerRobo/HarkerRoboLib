package harkerrobolib.util;

import java.security.InvalidParameterException;

/**
 * Wrapper class for a series of methods allowing for easy unit conversions.
 *
 * @author Kunal Jain
 * @version 8/27/22
 */
public final class Conversions {
  public interface Unit {
    double convert(Unit unit, double amt);
  }

  public static enum LinearUnit implements Unit {
    INCH(39.37007874),
    FOOT(3.280839895),
    MILLIMETER(1000.0),
    CENTIMETER(100.0),
    METER(1.0);
    private final double fromMeters;

    LinearUnit(double fromMeters) {
      this.fromMeters = fromMeters;
    }

    private double toMeters(double amt) {
      return amt / fromMeters;
    }

    private double fromMeters(double amt) {
      return amt * fromMeters;
    }

    public double convert(Unit unit, double amt) {
      return ((LinearUnit) unit).fromMeters(toMeters(amt));
    }

    public double convertToAngular(double amt, double diameter, AngleUnit unit) {
      return unit.fromRotations(amt / AngleUnit.RADIAN.fromRotations(diameter));
    }
  }

  public static enum TimeUnit implements Unit {
    NANOSECOND(Math.pow(10, -9)),
    MICROSECOND(Math.pow(10, -6)),
    MILLISECOND(0.001),
    CTRE_VEL(0.1),
    SECOND(1.0),
    MINUTE(60.0);
    private final double toSeconds;

    TimeUnit(double toSeconds) {
      this.toSeconds = toSeconds;
    }

    private double toSeconds(double amt) {
      return amt * toSeconds;
    }

    private double fromSeconds(double amt) {
      return amt / toSeconds;
    }

    public double convert(Unit unit, double amt) {
      return ((TimeUnit) unit).fromSeconds(toSeconds(amt));
    }
  }

  public static enum AngleUnit implements Unit {
    QUADRATURE(4096.0),
    TALONFX(2048.0),
    DEGREE(360),
    RADIAN(2 * Math.PI),
    ROTATION(1.0);
    private final double fromRotations;

    AngleUnit(double fromRotations) {
      this.fromRotations = fromRotations;
    }

    private double toRotations(double amt) {
      return amt / fromRotations;
    }

    private double fromRotations(double amt) {
      return amt * fromRotations;
    }

    public double convert(Unit unit, double amt) {
      return ((AngleUnit) unit).fromRotations(toRotations(amt));
    }

    public double convertToLinear(double amt, double diameter) {
      return RADIAN.fromRotations(toRotations(amt)) * diameter;
    }
  }

  public static class VelocityUnit implements Unit {
    Unit displacement;
    TimeUnit time;

    public VelocityUnit(Unit displacement, TimeUnit time) {
      this.displacement = displacement;
      this.time = time;
    }

    public double convert(Unit unit, double amt) {
      VelocityUnit vel = (VelocityUnit) unit;
      amt = displacement.convert(vel.displacement, amt);
      amt = vel.time.convert(time, amt);
      return amt;
    }

    public double convert(Unit unit, double amt, double diameter) {
      VelocityUnit vel = (VelocityUnit) unit;
      if (displacement instanceof LinearUnit) {
        amt =
            ((LinearUnit) displacement)
                .convertToAngular(amt, diameter, (AngleUnit) vel.displacement);
      }
      amt = vel.time.convert(time, amt);
      return amt;
    }
  }

  /**
   * Converts a value of one unit to a value of another unit.
   *
   * @param startUnit the unit of the passed-in value (either AngleUnit, SpeedUnit, PositionUnit,
   *     TimeUnit).
   * @param startValue the value to convert.
   * @param desiredUnit the desired unit of the passed-in value.
   * @precondition startUnit and desiredUnit both measure the same quantity.
   * @return the converted value.
   */
  public static double convert(Unit startUnit, double startValue, Unit desiredUnit) {
    if (startUnit.getClass().equals(desiredUnit.getClass())) {
      return startUnit.convert(desiredUnit, startValue);
    }
    throw new InvalidParameterException("Unit classes are non-equivalent");
  }

  public static double convert(
      Unit startUnit, double startValue, double diameter, Unit desiredUnit) {
    if (startUnit instanceof LinearUnit) {
      return ((LinearUnit) startUnit)
          .convertToAngular(startValue, diameter, (AngleUnit) desiredUnit);
    } else if (startUnit instanceof AngleUnit) {
      return ((LinearUnit) startUnit)
          .convertToAngular(startValue, diameter, (AngleUnit) desiredUnit);
    } else return ((VelocityUnit) startUnit).convert(desiredUnit, startValue, diameter);
  }
}
