package harkerrobolib.wrappers;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SensorTerm;
import com.ctre.phoenix.motorcontrol.VelocityMeasPeriod;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

/**
 * TalonSRX wrapper class to reset to factory defaults and more Updated from old Talon wrapper class
 * @author atierno
 * @author Jatin
 * @author Finn Frankis
 */
public class TalonSRXWrapper extends TalonSRX {

    // private boolean isReversed = false;
    private final int DEFAULT_TIMEOUT = 10;
    private final int PID_PRIMARY = 0;
    private final int PID_AUXILIARY = 0;

    private int timeout;

    /**
     * Initializes a TalonSRXWrapper with its CAN device number.
     * @param deviceNumber The CAN device number of the Talon.
     */
    public TalonSRXWrapper (int deviceNumber) {
        super(deviceNumber);
        timeout = DEFAULT_TIMEOUT;
    }

    // /**
    // * Initializes a TalonWrapper with its channel and reversed flag.
    // * @param channel The PWM channel on the digital module the Talon is attached to.
    // * @param isReversed Whether or not the output of this Talon should be flipped.
    // */
    // public TalonSRXWrapper (int channel, boolean isReversed) {
    // super(channel);
    // this.isReversed = isReversed;
    // }

    // /**
    // * Sets the speed of the Talon (handles reversing).
    // * @param speed The speed to set the Talon.
    // */
    // public void set (double speed) {
    // super.set(isReversed ? -speed : speed);
    // }

    // /**
    // * Sets the reversed flag of the Talon.
    // * @param flag Set whether or not to reverse the Talon output.
    // */
    // public void setReversed (boolean flag) {
    // isReversed = flag;
    // }

    // /**
    // * Gets the reversed flag of the Talon.
    // * @return Whether or not the Talon output is reversed
    // */
    // public boolean getReversed () {
    // return isReversed;
    // }

    /**
     * Resets the Talon to its factory defaults, software equivalent of resetting the talon,<br>
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

    
    /**
     * Contains all the default states for a TalonSRX. 
     * @author Finn Frankis
     * @version Aug 18, 2018
     */
    private static class Default {
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
