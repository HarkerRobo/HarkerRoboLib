package harkerrobolib.util;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatorCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.sensors.SensorVelocityMeasPeriod;
import harkerrobolib.wrappers.HSFalcon;

/*
 * Helps build Talon FX motors with default configs
 * 
 * @author Chiling Han
 * @version 01/30/2023
 */
public class HSFalconBuilder {

  private NeutralMode neutralMode = NeutralMode.Brake;
  private boolean invert = false;
  private SensorVelocityMeasPeriod velocityMeasPeriod = SensorVelocityMeasPeriod.Period_100Ms;
  private int velocityWindow = 16;
  private int voltageFilter = 16;
  private int fastCANFrame = (int) (1000 * Constants.ROBOT_LOOP);
  private int slowCANFrame = 2 * fastCANFrame;
  private StatorCurrentLimitConfiguration stator;
  private SupplyCurrentLimitConfiguration supply;
  private double voltageComp = Constants.MAX_VOLTAGE;
  
  public HSFalconBuilder neutralMode(NeutralMode neutralMode) {
    this.neutralMode = neutralMode;
    return this;
  }

  public HSFalconBuilder invert(boolean invert) {
    this.invert = invert;
    return this;
  }

  public HSFalconBuilder velocityMeasurementPeriod(SensorVelocityMeasPeriod period) {
    velocityMeasPeriod = period;
    return this;
  }

  public HSFalconBuilder voltageFilter(int voltageFilter) {
    this.voltageFilter = voltageFilter;
    return this;
  }

  public HSFalconBuilder statorLimit(double peak, double sustained, double peakdur) {
    stator = new StatorCurrentLimitConfiguration(true, sustained, peak, peakdur);
    supply = null;
    return this;
  }

  public HSFalconBuilder supplyLimit(double peak, double sustained, double peakdur) {
    supply = new SupplyCurrentLimitConfiguration(true, sustained, peak, peakdur);
    stator = null;
    return this;
  }

  public HSFalconBuilder canFramePeriods(int fast, int slow) {
    fastCANFrame = fast;
    slowCANFrame = slow;
    return this;
  }

  public HSFalconBuilder velocityWindow(int window) {
    velocityWindow = window;
    return this;
  }

  public HSFalcon build(int deviceID, String canbus) {
    HSFalcon falcon = new HSFalcon(deviceID, canbus);
    falcon.configFactoryDefault();
    falcon.setNeutralMode(neutralMode);
    falcon.setInverted(invert);
    falcon.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);
    falcon.configVelocityMeasurementPeriod(velocityMeasPeriod);
    falcon.configVelocityMeasurementWindow(velocityWindow);
    falcon.configVoltageMeasurementFilter(voltageFilter);
    falcon.configVoltageCompSaturation(voltageComp);
    falcon.enableVoltageCompensation(true);
    if (stator != null) falcon.configStatorCurrentLimit(stator);
    if (supply != null) falcon.configSupplyCurrentLimit(supply);
    for (StatusFrame frame : StatusFrame.values())
      falcon.setStatusFramePeriod(frame, Constants.MAX_CAN_FRAME_PERIOD);
    falcon.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, fastCANFrame);
    falcon.setStatusFramePeriod(StatusFrameEnhanced.Status_4_AinTempVbat, slowCANFrame);
    falcon.selectProfileSlot(Constants.SLOT_INDEX, Constants.PID_PRIMARY);
    falcon.configClosedLoopPeriod(Constants.SLOT_INDEX, 1);
    return falcon;
  }
}
