package harkerrobolib.wrappers;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.ParamEnum;
import com.ctre.phoenix.motion.BufferedTrajectoryPointStream;
import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motorcontrol.ControlFrame;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.Faults;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.FollowerType;
import com.ctre.phoenix.motorcontrol.IMotorController;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.RemoteFeedbackDevice;
import com.ctre.phoenix.motorcontrol.RemoteLimitSwitchSource;
import com.ctre.phoenix.motorcontrol.RemoteSensorSource;
import com.ctre.phoenix.motorcontrol.SensorTerm;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.StickyFaults;
import com.ctre.phoenix.motorcontrol.VelocityMeasPeriod;
import com.ctre.phoenix.motorcontrol.can.FilterConfiguration;
import com.ctre.phoenix.motorcontrol.can.SlotConfiguration;
import com.ctre.phoenix.sensors.CANCoder;

public interface HSMotorController {

    void BaseMotorController(int arbId, String model);

    ErrorCode DestroyObject();
 
    long getHandle();
 
    int getDeviceID();

    void set (ControlMode mode, double outputValue);
 
    void set(ControlMode mode, double demand0, double demand1);
 
    void set(ControlMode mode, double demand0, DemandType demand1Type, double demand1);
 
    void neutralOutput();
 
    void setNeutralMode(NeutralMode neutralMode);
 
    void enableHeadingHold(boolean enable);
 
    void selectDemandType(boolean value);
 
    void setSensorPhase(boolean PhaseSensor);
 
    void setInverted(boolean invert);
 
    void setInverted(InvertType invertType);

    boolean getInverted();
 
    ErrorCode configFactoryDefault(int timeoutMs);
 
    ErrorCode configFactoryDefault();
 
    ErrorCode configOpenloopRamp(double secondsFromNeutralToFull, int timeoutMs);
 
    ErrorCode configOpenloopRamp(double secondsFromNeutralToFull);
 
    ErrorCode configClosedloopRamp(double secondsFromNeutralToFull, int timeoutMs);
 
    ErrorCode configClosedloopRamp(double secondsFromNeutralToFull);
 
    ErrorCode configPeakOutputForward(double percentOut, int timeoutMs);
 
    ErrorCode configPeakOutputForward(double percentOut);
 
    ErrorCode configPeakOutputReverse(double percentOut, int timeoutMs);
 
    ErrorCode configPeakOutputReverse(double percentOut);
 
    ErrorCode configNominalOutputForward(double percentOut, int timeoutMs);
 
    ErrorCode configNominalOutputForward(double percentOut);
 
    ErrorCode configNominalOutputReverse(double percentOut, int timeoutMs);
 
    ErrorCode configNominalOutputReverse(double percentOut);
 
    ErrorCode configNeutralDeadband(double percentDeadband, int timeoutMs);
 
    ErrorCode configNeutralDeadband(double percentDeadband);
 
    ErrorCode configVoltageCompSaturation(double voltage, int timeoutMs);
 
    ErrorCode configVoltageCompSaturation(double voltage);
 
    ErrorCode configVoltageMeasurementFilter(int filterWindowSamples, int timeoutMs);
 
    ErrorCode configVoltageMeasurementFilter(int filterWindowSamples);

    void enableVoltageCompensation(boolean enable);
 
    boolean isVoltageCompensationEnabled();
 
    double getBusVoltage();
 
    double getMotorOutputPercent();
 
    double getMotorOutputVoltage();
 
    double getTemperature();
 
    ErrorCode configSelectedFeedbackSensor(RemoteFeedbackDevice feedbackDevice, int pidIdx, int timeoutMs);
 
    ErrorCode configSelectedFeedbackSensor(RemoteFeedbackDevice feedbackDevice);
 
    ErrorCode configSelectedFeedbackSensor(FeedbackDevice feedbackDevice, int pidIdx, int timeoutMs);
 
    ErrorCode configSelectedFeedbackSensor(FeedbackDevice feedbackDevice);
 
    ErrorCode configSelectedFeedbackCoefficient(double coefficient, int pidIdx, int timeoutMs);
 
    ErrorCode configSelectedFeedbackCoefficient(double coefficient);
 
    ErrorCode configRemoteFeedbackFilter(int deviceID, RemoteSensorSource remoteSensorSource, int remoteOrdinal, int timeoutMs);
 
    ErrorCode configRemoteFeedbackFilter(int deviceID, RemoteSensorSource remoteSensorSource, int remoteOrdinal);
 
    ErrorCode configRemoteFeedbackFilter(CANCoder canCoderRef, int remoteOrdinal, int timeoutMs);
 
    ErrorCode configRemoteFeedbackFilter(CANCoder canCoderRef, int remoteOrdinal);
 
    ErrorCode configSensorTerm(SensorTerm sensorTerm, FeedbackDevice feedbackDevice, int timeoutMs);
 
    ErrorCode configSensorTerm(SensorTerm sensorTerm, FeedbackDevice feedbackDevice);
 
    ErrorCode configSensorTerm(SensorTerm sensorTerm, RemoteFeedbackDevice feedbackDevice, int timeoutMs);
 
    ErrorCode configSensorTerm(SensorTerm sensorTerm, RemoteFeedbackDevice feedbackDevice);
 
    int getSelectedSensorPosition(int pidIdx);
 
    int getSelectedSensorPosition();
 
    int getSelectedSensorVelocity(int pidIdx);

    int	getSelectedSensorVelocity();

    ErrorCode setSelectedSensorPosition(int sensorPos, int pidIdx, int timeoutMs);
 
    ErrorCode setSelectedSensorPosition(int sensorPos);
     
    ErrorCode setControlFramePeriod(ControlFrame frame, int periodMs);
     
    ErrorCode setControlFramePeriod(int frame, int periodMs);
     
    ErrorCode setStatusFramePeriod(int frameValue, int periodMs, int timeoutMs);
     
    ErrorCode setStatusFramePeriod(int frameValue, int periodMs);
     
    ErrorCode setStatusFramePeriod(StatusFrame frame, int periodMs, int timeoutMs);
     
    ErrorCode setStatusFramePeriod(StatusFrame frame, int periodMs);
     
    int getStatusFramePeriod(int frame, int timeoutMs);
     
    int getStatusFramePeriod(int frame);
     
    int getStatusFramePeriod(StatusFrame frame, int timeoutMs);
    
    int getStatusFramePeriod(StatusFrame frame);
     
    ErrorCode configForwardLimitSwitchSource(RemoteLimitSwitchSource type, LimitSwitchNormal normalOpenOrClose, int deviceID, int timeoutMs);
     
    ErrorCode configForwardLimitSwitchSource(RemoteLimitSwitchSource type, LimitSwitchNormal normalOpenOrClose, int deviceID);
     
    ErrorCode configReverseLimitSwitchSource(RemoteLimitSwitchSource type, LimitSwitchNormal normalOpenOrClose, int deviceID, int timeoutMs);
     
    ErrorCode configReverseLimitSwitchSource(RemoteLimitSwitchSource type, LimitSwitchNormal normalOpenOrClose, int deviceID);
     
    void overrideLimitSwitchesEnable (boolean enable);
     
    ErrorCode configForwardSoftLimitThreshold (int forwardSensorLimit, int timeoutMs);
     
    ErrorCode configForwardSoftLimitThreshold (int forwardSensorLimit);
     
    ErrorCode configReverseSoftLimitThreshold (int reverseSensorLimit, int timeoutMs);
     
    ErrorCode configReverseSoftLimitThreshold (int reverseSensorLimit);
     
    ErrorCode configForwardSoftLimitEnable (boolean enable, int timeoutMs);
     
    ErrorCode configForwardSoftLimitEnable (boolean enable);
     
    ErrorCode configReverseSoftLimitEnable (boolean enable, int timeoutMs);
     
    ErrorCode configReverseSoftLimitEnable (boolean enable);

    void overrideSoftLimitsEnable (boolean enable);
 
    ErrorCode config_kP(int slotIdx, double value, int timeoutMs);
     
    ErrorCode config_kP(int slotIdx, double value);
     
    ErrorCode config_kI(int slotIdx, double value, int timeoutMs);
     
    ErrorCode config_kI(int slotIdx, double value);
     
    ErrorCode config_kD(int slotIdx, double value, int timeoutMs);
     
    ErrorCode config_kD(int slotIdx, double value);
     
    ErrorCode config_kF(int slotIdx, double value, int timeoutMs);
     
    ErrorCode config_kF(int slotIdx, double value);
     
    ErrorCode config_IntegralZone(int slotIdx, int izone, int timeoutMs);
     
    ErrorCode config_IntegralZone(int slotIdx, int izone);
     
    ErrorCode configAllowableClosedloopError(int slotIdx, int allowableClosedLoopError, int timeoutMs);
     
    ErrorCode configAllowableClosedloopError(int slotIdx, int allowableClosedLoopError);
     
    ErrorCode configMaxIntegralAccumulator(int slotIdx, double iaccum, int timeoutMs);
     
    ErrorCode configMaxIntegralAccumulator(int slotIdx, double iaccum);
     
    ErrorCode configClosedLoopPeakOutput(int slotIdx, double percentOut, int timeoutMs);
     
    ErrorCode configClosedLoopPeakOutput(int slotIdx, double percentOut);
     
    ErrorCode configClosedLoopPeriod(int slotIdx, int loopTimeMs, int timeoutMs);
     
    ErrorCode configClosedLoopPeriod(int slotIdx, int loopTimeMs);
     
    ErrorCode configAuxPIDPolarity(boolean invert, int timeoutMs);
     
    ErrorCode configAuxPIDPolarity(boolean invert);
     
    ErrorCode setIntegralAccumulator(double iaccum, int pidIdx, int timeoutMs);
     
    ErrorCode setIntegralAccumulator(double iaccum);
     
    int getClosedLoopError(int pidIdx);
     
    int getClosedLoopError();
     
    double getIntegralAccumulator(int pidIdx);
     
    double getIntegralAccumulator();
     
    double getErrorDerivative(int pidIdx);
     
    double getErrorDerivative();
     
    void selectProfileSlot(int slotIdx, int pidIdx);
     
    double getClosedLoopTarget(int pidIdx);
     
    double getClosedLoopTarget();
     
    int getActiveTrajectoryPosition();
     
    int getActiveTrajectoryPosition(int pidIdx);
     
    int getActiveTrajectoryVelocity();
     
    int getActiveTrajectoryVelocity(int pidIdx);
     
    double getActiveTrajectoryHeading();
     
    double getActiveTrajectoryArbFeedFwd();
     
    double getActiveTrajectoryArbFeedFwd(int pidIdx);
     
    ErrorCode configMotionCruiseVelocity(int sensorUnitsPer100ms, int timeoutMs);
     
    ErrorCode configMotionCruiseVelocity(int sensorUnitsPer100ms);
     
    ErrorCode configMotionAcceleration(int sensorUnitsPer100msPerSec, int timeoutMs);
     
    ErrorCode configMotionAcceleration(int sensorUnitsPer100msPerSec);
     
    ErrorCode configMotionSCurveStrength(int curveStrength, int timeoutMs);
     
    ErrorCode configMotionSCurveStrength(int curveStrength);
     
    ErrorCode clearMotionProfileTrajectories();
     
    int getMotionProfileTopLevelBufferCount();
     
    ErrorCode pushMotionProfileTrajectory(TrajectoryPoint trajPt);
    
    ErrorCode startMotionProfile(BufferedTrajectoryPointStream stream, int minBufferedPts, ControlMode motionProfControlMode);
     
    boolean isMotionProfileFinished();
     
    boolean isMotionProfileTopLevelBufferFull();
     
    void processMotionProfileBuffer();
     
    ErrorCode getMotionProfileStatus(MotionProfileStatus statusToFill);
     
    ErrorCode clearMotionProfileHasUnderrun(int timeoutMs);
     
    ErrorCode clearMotionProfileHasUnderrun();
     
    ErrorCode changeMotionControlFramePeriod(int periodMs);
     
    ErrorCode configMotionProfileTrajectoryPeriod(int baseTrajDurationMs, int timeoutMs);
     
    ErrorCode configMotionProfileTrajectoryPeriod(int baseTrajDurationMs);
     
    ErrorCode configMotionProfileTrajectoryInterpolationEnable(boolean enable, int timeoutMs);
     
    ErrorCode configMotionProfileTrajectoryInterpolationEnable(boolean enable);
     
    ErrorCode configFeedbackNotContinuous(boolean feedbackNotContinuous, int timeoutMs);
     
    ErrorCode configRemoteSensorClosedLoopDisableNeutralOnLOS(boolean remoteSensorClosedLoopDisableNeutralOnLOS, int timeoutMs);
     
    ErrorCode configClearPositionOnLimitF(boolean clearPositionOnLimitF, int timeoutMs);
     
    ErrorCode configClearPositionOnLimitR(boolean clearPositionOnLimitR, int timeoutMs);
     
    ErrorCode configClearPositionOnQuadIdx(boolean clearPositionOnQuadIdx, int timeoutMs);
    
    ErrorCode configLimitSwitchDisableNeutralOnLOS(boolean limitSwitchDisableNeutralOnLOS, int timeoutMs);
     
    ErrorCode configSoftLimitDisableNeutralOnLOS(boolean softLimitDisableNeutralOnLOS, int timeoutMs);
     
    ErrorCode configPulseWidthPeriod_EdgesPerRot(int pulseWidthPeriod_EdgesPerRot, int timeoutMs);
     
    ErrorCode configPulseWidthPeriod_FilterWindowSz(int pulseWidthPeriod_FilterWindowSz, int timeoutMs);
     
    ErrorCode getLastError();
     
    ErrorCode getFaults(Faults toFill);
     
    ErrorCode getStickyFaults(StickyFaults toFill);
     
    ErrorCode clearStickyFaults(int timeoutMs);
     
    ErrorCode clearStickyFaults();

    int getFirmwareVersion();
 
    boolean hasResetOccurred();
     
    ErrorCode configSetCustomParam(int newValue, int paramIndex, int timeoutMs);
     
    ErrorCode configSetCustomParam(int newValue, int paramIndex);
     
    int configGetCustomParam(int paramIndex, int timeoutMs);
     
    int configGetCustomParam(int paramIndex);
     
    ErrorCode configSetParameter(ParamEnum param, double value, int subValue, int ordinal, int timeoutMs);
     
    ErrorCode configSetParameter(ParamEnum param, double value, int subValue, int ordinal);
     
    ErrorCode configSetParameter(int param, double value, int subValue, int ordinal, int timeoutMs);
     
    ErrorCode configSetParameter(int param, double value, int subValue, int ordinal);
     
    double configGetParameter(ParamEnum param, int ordinal, int timeoutMs);
     
    double configGetParameter(ParamEnum param, int ordinal);
     
    double configGetParameter(int param, int ordinal, int timeoutMs);
    
    double configGetParameter(int param, int ordinal);
     
    int getBaseID();
     
    ControlMode getControlMode();
    
    void follow(IMotorController masterToFollow, FollowerType followerType);
     
    void follow(IMotorController masterToFollow);
     
    void valueUpdated();
     
    ErrorCode configureSlot(SlotConfiguration slot);
     
    ErrorCode configureSlot(SlotConfiguration slot, int slotIdx, int timeoutMs);
     
    void getSlotConfigs(SlotConfiguration slot, int slotIdx, int timeoutMs);
     
    void getSlotConfigs(SlotConfiguration slot);
     
    ErrorCode configureFilter(FilterConfiguration filter, int ordinal, int timeoutMs, boolean enableOptimizations);
     
    ErrorCode configureFilter(FilterConfiguration filter, int ordinal, int timeoutMs);
     
    ErrorCode configureFilter(FilterConfiguration filter);
     
    void getFilterConfigs(FilterConfiguration filter, int ordinal, int timeoutMs);
     
    void getFilterConfigs(FilterConfiguration filter);

    ErrorCode setStatusFramePeriod(StatusFrameEnhanced frame, int periodMs, int timeoutMs);

    ErrorCode setStatusFramePeriod(StatusFrameEnhanced frame, int periodMs);

    int getStatusFramePeriod(StatusFrameEnhanced frame, int timeoutMs);

    int getStatusFramePeriod(StatusFrameEnhanced frame);

    double getOutputCurrent();

    double getStatorCurrent();

    double getSupplyCurrent();

    ErrorCode configVelocityMeasurementPeriod(VelocityMeasPeriod period, int timeoutMs);

    ErrorCode configVelocityMeasurementPeriod(VelocityMeasPeriod period);

    ErrorCode configVelocityMeasurementWindow(int windowSize, int timeoutMs);

    ErrorCode configVelocityMeasurementWindow(int windowSize);

    ErrorCode configForwardLimitSwitchSource(LimitSwitchSource type, LimitSwitchNormal normalOpenOrClose, int timeoutMs);

    ErrorCode configForwardLimitSwitchSource(LimitSwitchSource type, LimitSwitchNormal normalOpenOrClose);

    ErrorCode configReverseLimitSwitchSource(LimitSwitchSource type, LimitSwitchNormal normalOpenOrClose, int timeoutMs);

    ErrorCode configReverseLimitSwitchSource(LimitSwitchSource type, LimitSwitchNormal normalOpenOrClose);

    int isFwdLimitSwitchClosed();

    int isRevLimitSwitchClosed();

}