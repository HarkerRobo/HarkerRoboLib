package harkerrobolib.util;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatorCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.sensors.SensorVelocityMeasPeriod;
import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.FeedbackConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.configs.VoltageConfigs;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj.motorcontrol.Talon;

import harkerrobolib.wrappers.HSFalcon;
import harkerrobolib.wrappers.HSMotorController;

/*
 * Helps build Talon FX motors with default configs
 *
 * @author Chiling Han
 * @version 01/30/2023
 */
public class HSFalconBuilder {

  private MotorOutputConfigs motorConfigs;
  private FeedbackConfigs config;
  private NeutralModeValue neutralMode;
  private InvertedValue invert;
  private SensorVelocityMeasPeriod velocityMeasPeriod = SensorVelocityMeasPeriod.Period_100Ms;
  private int velocityWindow = 16;
  private int voltageFilter = 16;
  private int fastCANFrame = (int) (1000 * Constants.ROBOT_LOOP);
  private int slowCANFrame = 2 * fastCANFrame;
  private CurrentLimitsConfigs stator;
  private CurrentLimitsConfigs supply;
  private double voltageComp = Constants.MAX_VOLTAGE;

  public HSFalconBuilder neutralMode(NeutralModeValue neutralMode) {
    this.neutralMode = neutralMode;
    return this;
  }

  public HSFalconBuilder invert(InvertedValue invert) {
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
    stator.StatorCurrentLimitEnable = true;
    stator.StatorCurrentLimit = peak;
    stator.SupplyCurrentThreshold = peakdur;
    stator.SupplyTimeThreshold = sustained;
    supply = null;
    return this;
  }

  public HSFalconBuilder supplyLimit(double peak, double sustained, double peakdur) {
    supply = new CurrentLimitsConfigs();
    supply.SupplyCurrentLimit = peak;
    supply.SupplyCurrentThreshold = peakdur;
    supply.SupplyTimeThreshold = sustained;
    supply.SupplyCurrentLimitEnable = true;
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

  public TalonFX build(int deviceID, String canbus) {
    TalonFX falcon = new TalonFX(deviceID, canbus);
    falcon.getConfigurator().apply(new TalonFXConfiguration());
    motorConfigs.NeutralMode = neutralMode;
    motorConfigs.Inverted = invert;
    falcon.getConfigurator().apply(motorConfigs);
    config.FeedbackSensorSource = FeedbackSensorSourceValue.RotorSensor;
    falcon.getConfigurator().apply(config, 1);
    //falcon.configVelocityMeasurementWindow(velocityWindow);
    //falcon.configVoltageMeasurementFilter(voltageFilter);
    if (stator != null){
      falcon.getConfigurator().apply(stator);
    }
    if (supply != null){
      falcon.getConfigurator().apply(supply);
    }
    falcon.getPosition().setUpdateFrequency(Constants.MAX_CAN_FRAME_PERIOD);
    //falcon.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, fastCANFrame);
    //falcon.setStatusFramePeriod(StatusFrameEnhanced.Status_4_AinTempVbat, slowCANFrame);
    //falcon.selectProfileSlot(Constants.SLOT_INDEX, Constants.PID_PRIMARY); seems to be selected when calling motion magic 
    return falcon;
  }
}