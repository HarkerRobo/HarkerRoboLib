package harkerrobolib.util;

/**
 * Wrapper class for a series of methods allowing for easy unit conversions.
 *
 * @author Kunal Jain
 * @version 8/27/22
 */
public final class Conversions {
  public static enum LinearUnit {
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

    public double to(LinearUnit unit, double amt) {
      return unit.fromMeters(toMeters(amt));
    }

    public double to(AngleUnit unit, double amt, double diameter) {
      return unit.fromRotations(amt / AngleUnit.RADIAN.fromRotations(diameter));
    }

    public double to(AngleUnit unit, double amt, LinearUnit diameterUnit, double diameter) {
      return unit.fromRotations(
          amt / AngleUnit.RADIAN.fromRotations(diameterUnit.to(this, diameter)));
    }
  }

  public static enum TimeUnit {
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

    public double to(TimeUnit unit, double amt) {
      return unit.fromSeconds(toSeconds(amt));
    }
  }

  public static enum AngleUnit {
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

    public double to(AngleUnit unit, double amt) {
      return unit.fromRotations(toRotations(amt));
    }

    public double to(LinearUnit unit, double amt, double diameter) {
      return RADIAN.fromRotations(toRotations(amt)) * diameter;
    }

    public double to(LinearUnit unit, double amt, LinearUnit diameterUnit, double diameter) {
      return RADIAN.fromRotations(toRotations(amt)) * diameterUnit.to(unit, diameter);
    }
  }

  public static class VelUnit {
    LinearUnit displacementLinear;
    AngleUnit displacementAngle;
    TimeUnit time;

    public VelUnit(AngleUnit displacement) {
      displacementAngle = displacement;
      if (displacement.equals(AngleUnit.TALONFX)) time = TimeUnit.CTRE_VEL;
    }

    public VelUnit(LinearUnit displacement) {
      displacementLinear = displacement;
      time = TimeUnit.SECOND;
    }

    public double to(VelUnit unit, double amt) {
      if (displacementLinear == null) amt = displacementAngle.to(unit.displacementAngle, amt);
      else amt = displacementLinear.to(unit.displacementLinear, amt);
      amt = unit.time.to(time, amt);
      return amt;
    }

    public double to(VelUnit unit, double amt, double diameter) {
      if (displacementLinear == null)
        amt = displacementAngle.to(unit.displacementLinear, amt, diameter);
      else amt = displacementLinear.to(unit.displacementAngle, amt, diameter);
      amt = unit.time.to(time, amt);
      return amt;
    }

    public double to(VelUnit unit, double amt, LinearUnit diameterUnit, double diameter) {
      if (displacementLinear == null)
        amt = displacementAngle.to(unit.displacementLinear, amt, diameterUnit, diameter);
      else amt = displacementLinear.to(unit.displacementAngle, amt, diameterUnit, diameter);
      amt = unit.time.to(time, amt);
      return amt;
    }
  }
}
