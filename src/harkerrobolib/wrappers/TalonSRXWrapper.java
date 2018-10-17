package harkerrobolib.wrappers;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.ParamEnum;
import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motorcontrol.ControlFrame;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.Faults;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.FollowerType;
import com.ctre.phoenix.motorcontrol.IMotorController;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.RemoteFeedbackDevice;
import com.ctre.phoenix.motorcontrol.RemoteLimitSwitchSource;
import com.ctre.phoenix.motorcontrol.RemoteSensorSource;
import com.ctre.phoenix.motorcontrol.SensorCollection;
import com.ctre.phoenix.motorcontrol.SensorTerm;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.StickyFaults;
import com.ctre.phoenix.motorcontrol.VelocityMeasPeriod;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

/**
 * Wraps a standard TalonSRX with functionality for resetting and a default timeout for relevant methods.
 * 
 * @author Jatin
 * @author Finn Frankis
 */
public class TalonSRXWrapper extends TalonSRX {

    // private boolean isReversed = false;
    private final int DEFAULT_TIMEOUT = 10;
    private final int PID_PRIMARY = 0;
    private final int PID_AUXILIARY = 1;

    
    private int timeout;

    /**
     * Constructs a TalonSRXWrapper with the default timeout {{@link #DEFAULT_TIMEOUT}.
     * @param deviceNumber The CAN device ID of the Talon.
     */
    public TalonSRXWrapper (int deviceNumber) {
        super(deviceNumber);
        timeout = DEFAULT_TIMEOUT;
    }
    
    /**
     * Constructs a TalonSRXWrapper.
     * @param deviceNumber the CAN device ID of the Talon.
     * @param defaultTimeout the timeout to be used if one is not specified in the method.
     */
    public TalonSRXWrapper (int deviceNumber, int defaultTimeout) {
    	this(deviceNumber);
    	timeout = defaultTimeout;
    }

    /**
     * Resets the Talon to its factory defaults - the software equivalent of resetting the talon,<br>
     * based on <a href=https://github.com/CrossTheRoadElec/Phoenix-Documentation>CTRE's Github</a> and <a
     * href=https://github.com/Team973/2018-inseason/blob/dev/lib/helpers/GreyTalon.h>973's Factory Reset Method</a>
     */
    public void reset () {
        configSelectedFeedbackSensor(Default.SENSOR, PID_PRIMARY, timeout);
        configSelectedFeedbackCoefficient(Default.FEEDBACK_COEFFICIENT, PID_PRIMARY, timeout);
        setSensorPhase(Default.SENSOR_PHASE);
        setInverted(Default.INVERTED);
        setNeutralMode(Default.NEUTRAL_MODE);
        getSensorCollection().setQuadraturePosition(Default.QUADRATURE_POSITION, timeout);
        configVelocityMeasurementPeriod(Default.VELOCITY_MEASUREMENT_PERIOD, timeout);
        configVelocityMeasurementWindow(Default.VELOCITY_MEASUREMENT_WINDOW, timeout);

        configNominalOutputForward(Default.NOMINAL_OUTPUT_FORWARD, timeout);
        configNominalOutputReverse(Default.NOMINAL_OUTPUT_REVERSE, timeout);
        configPeakOutputForward(Default.PEAK_OUTPUT_FORWARD, timeout);
        configPeakOutputReverse(Default.PEAK_OUTPUT_REVERSE, timeout);
        configNeutralDeadband(Default.NEUTRAL_DEADBAND, timeout);
        configOpenloopRamp(Default.OPEN_LOOP_RAMP_TIME, timeout);
        configClosedloopRamp(Default.CLOSED_LOOP_RAMP_TIME, timeout);

        // Gains
        for (int slot = Default.FIRST_PID_SLOT; slot <= Default.LAST_PID_SLOT; slot++) {
            config_kP(slot, Default.FPID_VALUE, timeout);
            config_kI(slot, Default.FPID_VALUE, timeout);
            config_kD(slot, Default.FPID_VALUE, timeout);
            config_kF(slot, Default.FPID_VALUE, timeout);
            config_IntegralZone(slot, Default.IZONE_VALUE, timeout);
            configMaxIntegralAccumulator(slot, Default.I_ACCUMULATOR, timeout);
            configAllowableClosedloopError(slot, Default.CLOSED_LOOP_ERROR, timeout);
            selectProfileSlot(slot, PID_PRIMARY);
            configClosedLoopPeakOutput(slot, Default.CLOSED_LOOP_PEAK_OUTPUT, timeout);
            configClosedLoopPeriod(slot, Default.CLOSED_LOOP_PERIOD, timeout);
        }

        configMotionCruiseVelocity(Default.CRUISE_VELOCITY, timeout);
        configMotionAcceleration(Default.ACCELERATION, timeout);
        configMotionProfileTrajectoryPeriod(Default.MOT_PROF_TRAJECTORY_PERIOD, timeout);

        // Limiting
        enableCurrentLimit(Default.CURRENT_LIMIT_ENABLED);
        configPeakCurrentDuration(Default.PEAK_CURRENT_DURATION, timeout);
        configPeakCurrentLimit(Default.PEAK_CURRENT_LIMIT, timeout);
        configContinuousCurrentLimit(Default.CONTINUOUS_CURRENT_LIMIT, timeout);

        enableVoltageCompensation(Default.VOLTAGE_COMP_ENABLED);
        configVoltageCompSaturation(Default.VOLTAGE_COMP_SATURATION, timeout);
        configVoltageMeasurementFilter(Default.VOLTAGE_MEASUREMENT_FILTER, timeout);

        configForwardSoftLimitThreshold(Default.FORWARD_SOFT_LIMIT, timeout);
        configReverseSoftLimitThreshold(Default.REVERSE_SOFT_LIMIT, timeout);
        configForwardSoftLimitEnable(Default.SOFT_LIMIT_ENABLED, timeout);
        configReverseSoftLimitEnable(Default.SOFT_LIMIT_ENABLED, timeout);
        configForwardLimitSwitchSource(Default.LIMIT_SOURCE, Default.LIMIT_NORMAL_TYPE, timeout);
        configReverseLimitSwitchSource(Default.LIMIT_SOURCE, Default.LIMIT_NORMAL_TYPE, timeout);

        configSensorTerm(SensorTerm.Diff0, Default.SENSOR, timeout);
        configSensorTerm(SensorTerm.Diff1, Default.SENSOR, timeout);
        configSensorTerm(SensorTerm.Sum0, Default.SENSOR, timeout);
        configSensorTerm(SensorTerm.Sum1, Default.SENSOR, timeout);

        configAuxPIDPolarity(Default.AUX_PID_POLARITY, timeout);

        set(Default.CONTROL_MODE, 0);
    }

    /**
     * Gets the default timeout.
     * @return the default timeout
     */
    public int getDefaultTimeout () {
        return timeout;
    }

    /**
     * Sets the default timeout for use when none is specified.
     * @param newTimeout the new timeout
     */
    public void setDefaultTimeout (int newTimeout) {
        this.timeout = newTimeout;
    }

    
	public ErrorCode setStatusFramePeriod(StatusFrameEnhanced frame, int periodMs) {
		// TODO Auto-generated method stub
		return super.setStatusFramePeriod(frame, periodMs, timeout);
	}

	public int getStatusFramePeriod(StatusFrameEnhanced frame) {
		// TODO Auto-generated method stub
		return super.getStatusFramePeriod(frame, timeout);
	}

	
	public ErrorCode configVelocityMeasurementPeriod(VelocityMeasPeriod period) {
		// TODO Auto-generated method stub
		return super.configVelocityMeasurementPeriod(period, timeout);
	}

	
	public ErrorCode configVelocityMeasurementWindow(int windowSize) {
		// TODO Auto-generated method stub
		return super.configVelocityMeasurementWindow(windowSize, timeout);
	}

	
	public ErrorCode configForwardLimitSwitchSource(LimitSwitchSource type, LimitSwitchNormal normalOpenOrClose) {
		// TODO Auto-generated method stub
		return super.configForwardLimitSwitchSource(type, normalOpenOrClose, timeout);
	}

	
	public ErrorCode configReverseLimitSwitchSource(LimitSwitchSource type, LimitSwitchNormal normalOpenOrClose) {
		// TODO Auto-generated method stub
		return super.configReverseLimitSwitchSource(type, normalOpenOrClose, timeout);
	}

	
	public ErrorCode configPeakCurrentLimit(int amps) {
		// TODO Auto-generated method stub
		return super.configPeakCurrentLimit(amps, timeout);
	}

	
	public ErrorCode configPeakCurrentDuration(int milliseconds) {
		// TODO Auto-generated method stub
		return super.configPeakCurrentDuration(milliseconds, timeout);
	}

	
	public ErrorCode configContinuousCurrentLimit(int amps) {
		// TODO Auto-generated method stub
		return super.configContinuousCurrentLimit(amps, timeout);
	}

	public ErrorCode configOpenloopRamp(double secondsFromNeutralToFull) {
		// TODO Auto-generated method stub
		return super.configOpenloopRamp(secondsFromNeutralToFull, timeout);
	}

	
	public ErrorCode configClosedloopRamp(double secondsFromNeutralToFull) {
		// TODO Auto-generated method stub
		return super.configClosedloopRamp(secondsFromNeutralToFull, timeout);
	}

	
	public ErrorCode configPeakOutputForward(double percentOut) {
		// TODO Auto-generated method stub
		return super.configPeakOutputForward(percentOut, timeout);
	}

	
	public ErrorCode configPeakOutputReverse(double percentOut) {
		// TODO Auto-generated method stub
		return super.configPeakOutputReverse(percentOut, timeout);
	}

	
	public ErrorCode configNominalOutputForward(double percentOut) {
		// TODO Auto-generated method stub
		return super.configNominalOutputForward(percentOut, timeout);
	}

	
	public ErrorCode configNominalOutputReverse(double percentOut) {
		// TODO Auto-generated method stub
		return super.configNominalOutputReverse(percentOut, timeout);
	}

	
	public ErrorCode configNeutralDeadband(double percentDeadband) {
		// TODO Auto-generated method stub
		return super.configNeutralDeadband(percentDeadband, timeout);
	}

	
	public ErrorCode configVoltageCompSaturation(double voltage) {
		// TODO Auto-generated method stub
		return super.configVoltageCompSaturation(voltage, timeout);
	}

	
	public ErrorCode configVoltageMeasurementFilter(int filterWindowSamples) {
		// TODO Auto-generated method stub
		return super.configVoltageMeasurementFilter(filterWindowSamples, timeout);
	}
	public ErrorCode configSelectedFeedbackSensor(RemoteFeedbackDevice feedbackDevice, int pidIdx) {
		// TODO Auto-generated method stub
		return super.configSelectedFeedbackSensor(feedbackDevice, pidIdx, timeout);
	}

	
	public ErrorCode configSelectedFeedbackSensor(FeedbackDevice feedbackDevice, int pidIdx) {
		// TODO Auto-generated method stub
		return super.configSelectedFeedbackSensor(feedbackDevice, pidIdx, timeout);
	}

	
	public ErrorCode configSelectedFeedbackCoefficient(double coefficient, int pidIdx) {
		// TODO Auto-generated method stub
		return super.configSelectedFeedbackCoefficient(coefficient, pidIdx, timeout);
	}

	
	public ErrorCode configRemoteFeedbackFilter(int deviceID, RemoteSensorSource remoteSensorSource, int remoteOrdinal) {
		// TODO Auto-generated method stub
		return super.configRemoteFeedbackFilter(deviceID, remoteSensorSource, remoteOrdinal, timeout);
	}

	
	public ErrorCode configSensorTerm(SensorTerm sensorTerm, FeedbackDevice feedbackDevice) {
		// TODO Auto-generated method stub
		return super.configSensorTerm(sensorTerm, feedbackDevice, timeout);
	}
	
	public ErrorCode setSelectedSensorPosition(int sensorPos, int pidIdx) {
		// TODO Auto-generated method stub
		return super.setSelectedSensorPosition(sensorPos, pidIdx, timeout);
	}

	public ErrorCode setStatusFramePeriod(int frameValue, int periodMs) {
		// TODO Auto-generated method stub
		return super.setStatusFramePeriod(frameValue, periodMs, timeout);
	}

	
	public ErrorCode setStatusFramePeriod(StatusFrame frame, int periodMs) {
		// TODO Auto-generated method stub
		return super.setStatusFramePeriod(frame, periodMs, timeout);
	}

	
	public int getStatusFramePeriod(int frame) {
		// TODO Auto-generated method stub
		return super.getStatusFramePeriod(frame, timeout);
	}

	
	public int getStatusFramePeriod(StatusFrame frame) {
		// TODO Auto-generated method stub
		return super.getStatusFramePeriod(frame, timeout);
	}

	
	public ErrorCode configForwardLimitSwitchSource(RemoteLimitSwitchSource type, LimitSwitchNormal normalOpenOrClose,
			int deviceID) {
		// TODO Auto-generated method stub
		return super.configForwardLimitSwitchSource(type, normalOpenOrClose, deviceID, timeout);
	}

	
	public ErrorCode configReverseLimitSwitchSource(RemoteLimitSwitchSource type, LimitSwitchNormal normalOpenOrClose,
			int deviceID) {
		// TODO Auto-generated method stub
		return super.configReverseLimitSwitchSource(type, normalOpenOrClose, deviceID, timeout);
	}

	
	protected ErrorCode configForwardLimitSwitchSource(int typeValue, int normalOpenOrCloseValue, int deviceID) {
		// TODO Auto-generated method stub
		return super.configForwardLimitSwitchSource(typeValue, normalOpenOrCloseValue, deviceID, timeout);
	}

	
	protected ErrorCode configReverseLimitSwitchSource(int typeValue, int normalOpenOrCloseValue, int deviceID) {
		// TODO Auto-generated method stub
		return super.configReverseLimitSwitchSource(typeValue, normalOpenOrCloseValue, deviceID, timeout);
	}
	
	public ErrorCode configForwardSoftLimitThreshold(int forwardSensorLimit) {
		// TODO Auto-generated method stub
		return super.configForwardSoftLimitThreshold(forwardSensorLimit, timeout);
	}

	
	public ErrorCode configReverseSoftLimitThreshold(int reverseSensorLimit) {
		// TODO Auto-generated method stub
		return super.configReverseSoftLimitThreshold(reverseSensorLimit, timeout);
	}

	
	public ErrorCode configForwardSoftLimitEnable(boolean enable) {
		// TODO Auto-generated method stub
		return super.configForwardSoftLimitEnable(enable, timeout);
	}

	
	public ErrorCode configReverseSoftLimitEnable(boolean enable) {
		// TODO Auto-generated method stub
		return super.configReverseSoftLimitEnable(enable, timeout);
	}
	
	public ErrorCode config_kP(int slotIdx, double value) {
		// TODO Auto-generated method stub
		return super.config_kP(slotIdx, value, timeout);
	}

	
	public ErrorCode config_kI(int slotIdx, double value) {
		// TODO Auto-generated method stub
		return super.config_kI(slotIdx, value, timeout);
	}

	
	public ErrorCode config_kD(int slotIdx, double value) {
		// TODO Auto-generated method stub
		return super.config_kD(slotIdx, value, timeout);
	}

	
	public ErrorCode config_kF(int slotIdx, double value) {
		// TODO Auto-generated method stub
		return super.config_kF(slotIdx, value, timeout);
	}

	
	public ErrorCode config_IntegralZone(int slotIdx, int izone) {
		// TODO Auto-generated method stub
		return super.config_IntegralZone(slotIdx, izone, timeout);
	}

	
	public ErrorCode configAllowableClosedloopError(int slotIdx, int allowableClosedLoopError) {
		// TODO Auto-generated method stub
		return super.configAllowableClosedloopError(slotIdx, allowableClosedLoopError, timeout);
	}

	
	public ErrorCode configMaxIntegralAccumulator(int slotIdx, double iaccum) {
		// TODO Auto-generated method stub
		return super.configMaxIntegralAccumulator(slotIdx, iaccum, timeout);
	}

	
	public ErrorCode configClosedLoopPeakOutput(int slotIdx, double percentOut) {
		// TODO Auto-generated method stub
		return super.configClosedLoopPeakOutput(slotIdx, percentOut, timeout);
	}

	
	public ErrorCode configClosedLoopPeriod(int slotIdx, int loopTimeMs) {
		// TODO Auto-generated method stub
		return super.configClosedLoopPeriod(slotIdx, loopTimeMs, timeout);
	}

	
	public ErrorCode configAuxPIDPolarity(boolean invert) {
		// TODO Auto-generated method stub
		return super.configAuxPIDPolarity(invert, timeout);
	}

	
	public ErrorCode setIntegralAccumulator(double iaccum, int pidIdx) {
		// TODO Auto-generated method stub
		return super.setIntegralAccumulator(iaccum, pidIdx, timeout);
	}

	public ErrorCode configMotionCruiseVelocity(int sensorUnitsPer100ms) {
		// TODO Auto-generated method stub
		return super.configMotionCruiseVelocity(sensorUnitsPer100ms, timeout);
	}

	
	public ErrorCode configMotionAcceleration(int sensorUnitsPer100msPerSec) {
		// TODO Auto-generated method stub
		return super.configMotionAcceleration(sensorUnitsPer100msPerSec, timeout);
	}

	public ErrorCode clearMotionProfileHasUnderrun() {
		// TODO Auto-generated method stub
		return super.clearMotionProfileHasUnderrun(timeout);
	}

	public ErrorCode configMotionProfileTrajectoryPeriod(int baseTrajDurationMs) {
		// TODO Auto-generated method stub
		return super.configMotionProfileTrajectoryPeriod(baseTrajDurationMs, timeout);
	}
	
	public ErrorCode clearStickyFaults() {
		// TODO Auto-generated method stub
		return super.clearStickyFaults(timeout);
	}

	public ErrorCode configSetCustomParam(int newValue, int paramIndex) {
		// TODO Auto-generated method stub
		return super.configSetCustomParam(newValue, paramIndex, timeout);
	}

	
	public int configGetCustomParam(int paramIndex) {
		// TODO Auto-generated method stub
		return super.configGetCustomParam(paramIndex, timeout);
	}

	
	public ErrorCode configSetParameter(ParamEnum param, double value, int subValue, int ordinal) {
		// TODO Auto-generated method stub
		return super.configSetParameter(param, value, subValue, ordinal, timeout);
	}

	
	public ErrorCode configSetParameter(int param, double value, int subValue, int ordinal) {
		// TODO Auto-generated method stub
		return super.configSetParameter(param, value, subValue, ordinal, timeout);
	}

	
	public double configGetParameter(ParamEnum param, int ordinal) {
		// TODO Auto-generated method stub
		return super.configGetParameter(param, ordinal, timeout);
	}

	
	public double configGetParameter(int param, int ordinal) {
		// TODO Auto-generated method stub
		return super.configGetParameter(param, ordinal, timeout);
	}
	
	/**
     * Contains all the default states for a TalonSRX. 
     * @author Finn Frankis
     * @version Aug 18, 2018
     */
    protected static class Default {
        public static final FeedbackDevice SENSOR = FeedbackDevice.QuadEncoder;
        public static final boolean SENSOR_PHASE = false;
        public static final boolean INVERTED = false;
        public static final int FEEDBACK_COEFFICIENT = 1;
        public static final NeutralMode NEUTRAL_MODE = NeutralMode.Coast;

        public static final int QUADRATURE_POSITION = 0;

        public static final VelocityMeasPeriod VELOCITY_MEASUREMENT_PERIOD = VelocityMeasPeriod.Period_100Ms;
        public static final int VELOCITY_MEASUREMENT_WINDOW = 64;

        public static final double NOMINAL_OUTPUT_FORWARD = 0;
        public static final double NOMINAL_OUTPUT_REVERSE = 0;
        public static final double PEAK_OUTPUT_FORWARD = 1;
        public static final double PEAK_OUTPUT_REVERSE = -1;
        public static final double NEUTRAL_DEADBAND = 0.04;

        public static final double OPEN_LOOP_RAMP_TIME = 0.0;
        public static final double CLOSED_LOOP_RAMP_TIME = 0.0;

        public static final int FIRST_PID_SLOT = 0;
        public static final int LAST_PID_SLOT = 3;
        public static final int FIRST_PID_LOOP = 0;
        public static final int LAST_PID_LOOP = 1;
        public static final double FPID_VALUE = 0;
        public static final int IZONE_VALUE = 0;
        public static final int I_ACCUMULATOR = 0;
        public static final int CLOSED_LOOP_ERROR = 0;
        
        public static final int CRUISE_VELOCITY = 0;
        public static final int ACCELERATION = 0;
        public static final int MOT_PROF_TRAJECTORY_PERIOD = 0;
        
        public static final boolean CURRENT_LIMIT_ENABLED = false;
        public static final int PEAK_CURRENT_DURATION = 0;
        public static final int PEAK_CURRENT_LIMIT = 0;
        public static final int CONTINUOUS_CURRENT_LIMIT = 0;

        public static final boolean VOLTAGE_COMP_ENABLED = false;
        public static final double VOLTAGE_COMP_SATURATION = 0;
        public static final int VOLTAGE_MEASUREMENT_FILTER = 32;

        public static final int FORWARD_SOFT_LIMIT = 0;
        public static final int REVERSE_SOFT_LIMIT = 0;
        public static final boolean SOFT_LIMIT_ENABLED = false;
        public static final LimitSwitchSource LIMIT_SOURCE = LimitSwitchSource.Deactivated;
        public static final LimitSwitchNormal LIMIT_NORMAL_TYPE = LimitSwitchNormal.NormallyOpen;

        public static final double CLOSED_LOOP_PEAK_OUTPUT = 1.0;
        public static final int CLOSED_LOOP_PERIOD = 1;
        public static final boolean AUX_PID_POLARITY = false;

        public static final ControlMode CONTROL_MODE = ControlMode.Disabled;
    }
}
