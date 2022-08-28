package harkerrobolib.wrappers;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.ParamEnum;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;
import com.ctre.phoenix.sensors.PigeonIMU_StatusFrame;
import harkerrobolib.util.Constants;

/**
 * Represents a basic PigeonIMU with additional useful features.
 *
 * @author Finn Frankis
 * @since 10/21/18
 */
public class HSPigeon extends PigeonIMU {

  private int timeout;
  private double angleOffset;

  public HSPigeon(int deviceNumber) {
    this(deviceNumber, Constants.DEFAULT_TIMEOUT);
  }

  public HSPigeon(int deviceNumber, int defaultTimeout) {
    super(deviceNumber);
    this.timeout = defaultTimeout;
    angleOffset = 0;
  }

  public HSPigeon(TalonSRX talon) {
    this(talon, Constants.DEFAULT_TIMEOUT);
  }

  public HSPigeon(TalonSRX talon, int defaultTimeout) {
    super(talon);
    this.timeout = defaultTimeout;
    angleOffset = 0;
  }

  /**
   * Gets the current yaw value of the pigeon.
   *
   * @return the yaw
   */
  public double getYaw() {
    double[] ypr = new double[3];
    getYawPitchRoll(ypr);
    return ypr[0];
  }

  /**
   * Gets the current pitch value of the pigeon.
   *
   * @return the pitch
   */
  public double getPitch() {
    double[] ypr = new double[3];
    getYawPitchRoll(ypr);
    return ypr[1];
  }

  /**
   * Gets the current roll value of the pigeon.
   *
   * @return the roll
   */
  public double getRoll() {
    double[] ypr = new double[3];
    getYawPitchRoll(ypr);
    return ypr[2];
  }

  /**
   * Sets the pigeon yaw to a given value.
   *
   * @param angle the angle value to which the pigeon should be set, in pigeon units where 1
   *     rotation is 8192 units
   */
  @Override
  public ErrorCode setYaw(double angle) {
    return super.setYaw(
        angle * 64, timeout); // CTRE's error where replaced angle is off by a factor of 64
  }

  /**
   * Adds a given value to the pigeon yaw.
   *
   * @param angle the angle value which should be added to the pigeon yaw value, in pigeon units
   *     where 1 rotation is 8192 units
   */
  @Override
  public ErrorCode addYaw(double angle) {
    return super.addYaw(
        angle * 64, timeout); // CTRE's error where replaced angle is off by a factor of 64
  }

  /** Zeros the pigeon. */
  public void zero() {
    setYaw(0);
    setAccumZAngle(0);
  }

  @Override
  public ErrorCode setYawToCompass() {
    return super.setYawToCompass(timeout);
  }

  @Override
  public double getFusedHeading() {
    return super.getFusedHeading() + angleOffset;
  }

  @Override
  public ErrorCode setFusedHeading(double angleDeg) {
    angleOffset = angleDeg - getFusedHeading();
    return ErrorCode.OK;
  }

  @Override
  public ErrorCode addFusedHeading(double angleDeg) {
    return super.addFusedHeading(angleDeg, timeout);
  }

  @Override
  public ErrorCode setFusedHeadingToCompass() {
    return super.setFusedHeadingToCompass(timeout);
  }

  @Override
  public ErrorCode setAccumZAngle(double angleDeg) {
    return super.setAccumZAngle(angleDeg, timeout);
  }

  @Override
  public ErrorCode setTemperatureCompensationDisable(boolean bTempCompDisable) {
    return super.setTemperatureCompensationDisable(bTempCompDisable, timeout);
  }

  @Override
  public ErrorCode setCompassDeclination(double angleDegOffset) {
    return super.setCompassDeclination(angleDegOffset, timeout);
  }

  @Override
  public ErrorCode setCompassAngle(double angleDeg) {
    return super.setCompassAngle(angleDeg, timeout);
  }

  @Override
  public ErrorCode enterCalibrationMode(CalibrationMode calMode) {
    return super.enterCalibrationMode(calMode, timeout);
  }

  @Override
  public ErrorCode configSetCustomParam(int newValue, int paramIndex) {
    return super.configSetCustomParam(newValue, paramIndex, timeout);
  }

  @Override
  public int configGetCustomParam(int paramIndex, int timoutMs) {
    return super.configGetCustomParam(paramIndex, timoutMs);
  }

  @Override
  public ErrorCode configSetParameter(ParamEnum param, double value, int subValue, int ordinal) {
    return super.configSetParameter(param, value, subValue, ordinal, timeout);
  }

  @Override
  public ErrorCode configSetParameter(int param, double value, int subValue, int ordinal) {
    return super.configSetParameter(param, value, subValue, ordinal, timeout);
  }

  @Override
  public double configGetParameter(ParamEnum param, int ordinal) {
    return super.configGetParameter(param, ordinal, timeout);
  }

  @Override
  public double configGetParameter(int param, int ordinal) {
    return super.configGetParameter(param, ordinal, timeout);
  }

  @Override
  public ErrorCode setStatusFramePeriod(PigeonIMU_StatusFrame statusFrame, int periodMs) {
    return super.setStatusFramePeriod(statusFrame, periodMs, timeout);
  }

  @Override
  public ErrorCode setStatusFramePeriod(int statusFrame, int periodMs) {
    return super.setStatusFramePeriod(statusFrame, periodMs, timeout);
  }

  @Override
  public int getStatusFramePeriod(PigeonIMU_StatusFrame frame) {
    return super.getStatusFramePeriod(frame, timeout);
  }

  @Override
  public ErrorCode clearStickyFaults() {
    return super.clearStickyFaults(timeout);
  }

  public void setDefaultTimeout(int newTimeout) {
    this.timeout = newTimeout;
  }
}
