package harkerrobolib.wrappers

import com.ctre.phoenix.ErrorCode
import com.ctre.phoenix.ParamEnum
import com.ctre.phoenix.motion.MotionProfileStatus
import com.ctre.phoenix.motion.TrajectoryPoint
import com.ctre.phoenix.motorcontrol.ControlFrame
import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.DemandType
import com.ctre.phoenix.motorcontrol.Faults
import com.ctre.phoenix.motorcontrol.FeedbackDevice
import com.ctre.phoenix.motorcontrol.FollowerType
import com.ctre.phoenix.motorcontrol.IMotorController
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal
import com.ctre.phoenix.motorcontrol.LimitSwitchSource
import com.ctre.phoenix.motorcontrol.NeutralMode
import com.ctre.phoenix.motorcontrol.RemoteFeedbackDevice
import com.ctre.phoenix.motorcontrol.RemoteLimitSwitchSource
import com.ctre.phoenix.motorcontrol.RemoteSensorSource
import com.ctre.phoenix.motorcontrol.SensorCollection
import com.ctre.phoenix.motorcontrol.SensorTerm
import com.ctre.phoenix.motorcontrol.StatusFrame
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced
import com.ctre.phoenix.motorcontrol.StickyFaults
import com.ctre.phoenix.motorcontrol.VelocityMeasPeriod
import com.ctre.phoenix.motorcontrol.can.TalonSRX

import harkerrobolib.util.Constants

/**
 * Wraps a standard TalonSRX with functionality for resetting and a default timeout for relevant methods.
 *
 * @author Jatin
 * @author Finn Frankis
 * @param deviceNumber the CAN device ID of the Talon
 * @param defaultTimeout the timeout to be used if one is not specified in the method
 */
open class HSTalon (deviceNumber: Int, var defaultTimeout: Int = Constants.DEFAULT_TIMEOUT) : TalonSRX(deviceNumber) {

    /**
     * Resets the Talon to its factory defaults - the software equivalent of resetting the talon,<br></br>
     * based on [CTRE's Github](https://github.com/CrossTheRoadElec/Phoenix-Documentation) and [973's Factory Reset Method](https://github.com/Team973/2018-inseason/blob/dev/lib/helpers/GreyTalon.h)
     */
    fun reset() {
        configSelectedFeedbackSensor(Default.SENSOR, Constants.PID_PRIMARY, defaultTimeout)
        configSelectedFeedbackCoefficient(Default.FEEDBACK_COEFFICIENT.toDouble(), Constants.PID_PRIMARY, defaultTimeout)
        setSensorPhase(Default.SENSOR_PHASE)
        inverted = Default.INVERTED
        setNeutralMode(Default.NEUTRAL_MODE)
        sensorCollection.setQuadraturePosition(Default.QUADRATURE_POSITION, defaultTimeout)
        configVelocityMeasurementPeriod(Default.VELOCITY_MEASUREMENT_PERIOD, defaultTimeout)
        configVelocityMeasurementWindow(Default.VELOCITY_MEASUREMENT_WINDOW, defaultTimeout)

        configNominalOutputForward(Default.NOMINAL_OUTPUT_FORWARD, defaultTimeout)
        configNominalOutputReverse(Default.NOMINAL_OUTPUT_REVERSE, defaultTimeout)
        configPeakOutputForward(Default.PEAK_OUTPUT_FORWARD, defaultTimeout)
        configPeakOutputReverse(Default.PEAK_OUTPUT_REVERSE, defaultTimeout)
        configNeutralDeadband(Default.NEUTRAL_DEADBAND, defaultTimeout)
        configOpenloopRamp(Default.OPEN_LOOP_RAMP_TIME, defaultTimeout)
        configClosedloopRamp(Default.CLOSED_LOOP_RAMP_TIME, defaultTimeout)

        // Gains
        for (slot in Default.FIRST_PID_SLOT..Default.LAST_PID_SLOT) {
            config_kP(slot, Default.FPID_VALUE, defaultTimeout)
            config_kI(slot, Default.FPID_VALUE, defaultTimeout)
            config_kD(slot, Default.FPID_VALUE, defaultTimeout)
            config_kF(slot, Default.FPID_VALUE, defaultTimeout)
            config_IntegralZone(slot, Default.IZONE_VALUE, defaultTimeout)
            configMaxIntegralAccumulator(slot, Default.I_ACCUMULATOR.toDouble(), defaultTimeout)
            configAllowableClosedloopError(slot, Default.CLOSED_LOOP_ERROR, defaultTimeout)
            selectProfileSlot(slot, Constants.PID_PRIMARY)
            configClosedLoopPeakOutput(slot, Default.CLOSED_LOOP_PEAK_OUTPUT, defaultTimeout)
            configClosedLoopPeriod(slot, Default.CLOSED_LOOP_PERIOD, defaultTimeout)
        }

        configMotionCruiseVelocity(Default.CRUISE_VELOCITY, defaultTimeout)
        configMotionAcceleration(Default.ACCELERATION, defaultTimeout)
        configMotionProfileTrajectoryPeriod(Default.MOT_PROF_TRAJECTORY_PERIOD, defaultTimeout)

        // Limiting
        enableCurrentLimit(Default.CURRENT_LIMIT_ENABLED)
        configPeakCurrentDuration(Default.PEAK_CURRENT_DURATION, defaultTimeout)
        configPeakCurrentLimit(Default.PEAK_CURRENT_LIMIT, defaultTimeout)
        configContinuousCurrentLimit(Default.CONTINUOUS_CURRENT_LIMIT, defaultTimeout)

        enableVoltageCompensation(Default.VOLTAGE_COMP_ENABLED)
        configVoltageCompSaturation(Default.VOLTAGE_COMP_SATURATION, defaultTimeout)
        configVoltageMeasurementFilter(Default.VOLTAGE_MEASUREMENT_FILTER, defaultTimeout)

        configForwardSoftLimitThreshold(Default.FORWARD_SOFT_LIMIT, defaultTimeout)
        configReverseSoftLimitThreshold(Default.REVERSE_SOFT_LIMIT, defaultTimeout)
        configForwardSoftLimitEnable(Default.SOFT_LIMIT_ENABLED, defaultTimeout)
        configReverseSoftLimitEnable(Default.SOFT_LIMIT_ENABLED, defaultTimeout)
        configForwardLimitSwitchSource(Default.LIMIT_SOURCE, Default.LIMIT_NORMAL_TYPE, defaultTimeout)
        configReverseLimitSwitchSource(Default.LIMIT_SOURCE, Default.LIMIT_NORMAL_TYPE, defaultTimeout)

        configSensorTerm(SensorTerm.Diff0, Default.SENSOR, defaultTimeout)
        configSensorTerm(SensorTerm.Diff1, Default.SENSOR, defaultTimeout)
        configSensorTerm(SensorTerm.Sum0, Default.SENSOR, defaultTimeout)
        configSensorTerm(SensorTerm.Sum1, Default.SENSOR, defaultTimeout)

        configAuxPIDPolarity(Default.AUX_PID_POLARITY, defaultTimeout)

        set(Default.CONTROL_MODE, 0.0)
    }

    fun setStatusFramePeriod(frame: StatusFrameEnhanced, periodMs: Int): ErrorCode {
        // TODO Auto-generated method stub
        return this.setStatusFramePeriod(frame, periodMs, defaultTimeout)
    }

    fun getStatusFramePeriod(frame: StatusFrameEnhanced): Int {
        // TODO Auto-generated method stub
        return this.getStatusFramePeriod(frame, defaultTimeout)
    }


    fun configVelocityMeasurementPeriod(period: VelocityMeasPeriod): ErrorCode {
        // TODO Auto-generated method stub
        return this.configVelocityMeasurementPeriod(period, defaultTimeout)
    }


    fun configVelocityMeasurementWindow(windowSize: Int): ErrorCode {
        // TODO Auto-generated method stub
        return this.configVelocityMeasurementWindow(windowSize, defaultTimeout)
    }


    fun configForwardLimitSwitchSource(type: LimitSwitchSource, normalOpenOrClose: LimitSwitchNormal): ErrorCode {
        // TODO Auto-generated method stub
        return this.configForwardLimitSwitchSource(type, normalOpenOrClose, defaultTimeout)
    }


    fun configReverseLimitSwitchSource(type: LimitSwitchSource, normalOpenOrClose: LimitSwitchNormal): ErrorCode {
        // TODO Auto-generated method stub
        return this.configReverseLimitSwitchSource(type, normalOpenOrClose, defaultTimeout)
    }


    fun configPeakCurrentLimit(amps: Int): ErrorCode {
        // TODO Auto-generated method stub
        return this.configPeakCurrentLimit(amps, defaultTimeout)
    }


    fun configPeakCurrentDuration(milliseconds: Int): ErrorCode {
        // TODO Auto-generated method stub
        return this.configPeakCurrentDuration(milliseconds, defaultTimeout)
    }


    fun configContinuousCurrentLimit(amps: Int): ErrorCode {
        // TODO Auto-generated method stub
        return this.configContinuousCurrentLimit(amps, defaultTimeout)
    }

    fun configOpenloopRamp(secondsFromNeutralToFull: Double): ErrorCode {
        // TODO Auto-generated method stub
        return this.configOpenloopRamp(secondsFromNeutralToFull, defaultTimeout)
    }


    fun configClosedloopRamp(secondsFromNeutralToFull: Double): ErrorCode {
        // TODO Auto-generated method stub
        return this.configClosedloopRamp(secondsFromNeutralToFull, defaultTimeout)
    }


    fun configPeakOutputForward(percentOut: Double): ErrorCode {
        // TODO Auto-generated method stub
        return this.configPeakOutputForward(percentOut, defaultTimeout)
    }


    fun configPeakOutputReverse(percentOut: Double): ErrorCode {
        // TODO Auto-generated method stub
        return this.configPeakOutputReverse(percentOut, defaultTimeout)
    }


    fun configNominalOutputForward(percentOut: Double): ErrorCode {
        // TODO Auto-generated method stub
        return this.configNominalOutputForward(percentOut, defaultTimeout)
    }


    fun configNominalOutputReverse(percentOut: Double): ErrorCode {
        // TODO Auto-generated method stub
        return this.configNominalOutputReverse(percentOut, defaultTimeout)
    }


    fun configNeutralDeadband(percentDeadband: Double): ErrorCode {
        // TODO Auto-generated method stub
        return this.configNeutralDeadband(percentDeadband, defaultTimeout)
    }


    fun configVoltageCompSaturation(voltage: Double): ErrorCode {
        // TODO Auto-generated method stub
        return this.configVoltageCompSaturation(voltage, defaultTimeout)
    }


    fun configVoltageMeasurementFilter(filterWindowSamples: Int): ErrorCode {
        // TODO Auto-generated method stub
        return this.configVoltageMeasurementFilter(filterWindowSamples, defaultTimeout)
    }

    fun configSelectedFeedbackSensor(feedbackDevice: RemoteFeedbackDevice, pidIdx: Int): ErrorCode {
        // TODO Auto-generated method stub
        return this.configSelectedFeedbackSensor(feedbackDevice, pidIdx, defaultTimeout)
    }


    fun configSelectedFeedbackSensor(feedbackDevice: FeedbackDevice, pidIdx: Int): ErrorCode {
        // TODO Auto-generated method stub
        return this.configSelectedFeedbackSensor(feedbackDevice, pidIdx, defaultTimeout)
    }


    fun configSelectedFeedbackCoefficient(coefficient: Double, pidIdx: Int): ErrorCode {
        // TODO Auto-generated method stub
        return this.configSelectedFeedbackCoefficient(coefficient, pidIdx, defaultTimeout)
    }


    fun configRemoteFeedbackFilter(deviceID: Int, remoteSensorSource: RemoteSensorSource, remoteOrdinal: Int): ErrorCode {
        // TODO Auto-generated method stub
        return this.configRemoteFeedbackFilter(deviceID, remoteSensorSource, remoteOrdinal, defaultTimeout)
    }


    fun configSensorTerm(sensorTerm: SensorTerm, feedbackDevice: FeedbackDevice): ErrorCode {
        // TODO Auto-generated method stub
        return this.configSensorTerm(sensorTerm, feedbackDevice, defaultTimeout)
    }

    fun setSelectedSensorPosition(sensorPos: Int, pidIdx: Int): ErrorCode {
        // TODO Auto-generated method stub
        return this.setSelectedSensorPosition(sensorPos, pidIdx, defaultTimeout)
    }

    fun setStatusFramePeriod(frameValue: Int, periodMs: Int): ErrorCode {
        // TODO Auto-generated method stub
        return this.setStatusFramePeriod(frameValue, periodMs, defaultTimeout)
    }


    fun setStatusFramePeriod(frame: StatusFrame, periodMs: Int): ErrorCode {
        // TODO Auto-generated method stub
        return this.setStatusFramePeriod(frame, periodMs, defaultTimeout)
    }


    fun getStatusFramePeriod(frame: Int): Int {
        // TODO Auto-generated method stub
        return this.getStatusFramePeriod(frame, defaultTimeout)
    }


    fun getStatusFramePeriod(frame: StatusFrame): Int {
        // TODO Auto-generated method stub
        return this.getStatusFramePeriod(frame, defaultTimeout)
    }


    fun configForwardLimitSwitchSource(type: RemoteLimitSwitchSource, normalOpenOrClose: LimitSwitchNormal,
                                       deviceID: Int): ErrorCode {
        // TODO Auto-generated method stub
        return this.configForwardLimitSwitchSource(type, normalOpenOrClose, deviceID, defaultTimeout)
    }


    fun configReverseLimitSwitchSource(type: RemoteLimitSwitchSource, normalOpenOrClose: LimitSwitchNormal,
                                       deviceID: Int): ErrorCode {
        // TODO Auto-generated method stub
        return this.configReverseLimitSwitchSource(type, normalOpenOrClose, deviceID, defaultTimeout)
    }


    protected fun configForwardLimitSwitchSource(typeValue: Int, normalOpenOrCloseValue: Int, deviceID: Int): ErrorCode {
        // TODO Auto-generated method stub
        return this.configForwardLimitSwitchSource(typeValue, normalOpenOrCloseValue, deviceID, defaultTimeout)
    }


    protected fun configReverseLimitSwitchSource(typeValue: Int, normalOpenOrCloseValue: Int, deviceID: Int): ErrorCode {
        // TODO Auto-generated method stub
        return this.configReverseLimitSwitchSource(typeValue, normalOpenOrCloseValue, deviceID, defaultTimeout)
    }

    fun configForwardSoftLimitThreshold(forwardSensorLimit: Int): ErrorCode {
        // TODO Auto-generated method stub
        return this.configForwardSoftLimitThreshold(forwardSensorLimit, defaultTimeout)
    }


    fun configReverseSoftLimitThreshold(reverseSensorLimit: Int): ErrorCode {
        // TODO Auto-generated method stub
        return this.configReverseSoftLimitThreshold(reverseSensorLimit, defaultTimeout)
    }


    fun configForwardSoftLimitEnable(enable: Boolean): ErrorCode {
        // TODO Auto-generated method stub
        return this.configForwardSoftLimitEnable(enable, defaultTimeout)
    }


    fun configReverseSoftLimitEnable(enable: Boolean): ErrorCode {
        // TODO Auto-generated method stub
        return this.configReverseSoftLimitEnable(enable, defaultTimeout)
    }

    fun config_kP(slotIdx: Int, value: Double): ErrorCode {
        // TODO Auto-generated method stub
        return this.config_kP(slotIdx, value, defaultTimeout)
    }


    fun config_kI(slotIdx: Int, value: Double): ErrorCode {
        // TODO Auto-generated method stub
        return this.config_kI(slotIdx, value, defaultTimeout)
    }


    fun config_kD(slotIdx: Int, value: Double): ErrorCode {
        // TODO Auto-generated method stub
        return this.config_kD(slotIdx, value, defaultTimeout)
    }


    fun config_kF(slotIdx: Int, value: Double): ErrorCode {
        // TODO Auto-generated method stub
        return this.config_kF(slotIdx, value, defaultTimeout)
    }


    fun config_IntegralZone(slotIdx: Int, izone: Int): ErrorCode {
        // TODO Auto-generated method stub
        return this.config_IntegralZone(slotIdx, izone, defaultTimeout)
    }


    fun configAllowableClosedloopError(slotIdx: Int, allowableClosedLoopError: Int): ErrorCode {
        // TODO Auto-generated method stub
        return this.configAllowableClosedloopError(slotIdx, allowableClosedLoopError, defaultTimeout)
    }


    fun configMaxIntegralAccumulator(slotIdx: Int, iaccum: Double): ErrorCode {
        // TODO Auto-generated method stub
        return this.configMaxIntegralAccumulator(slotIdx, iaccum, defaultTimeout)
    }


    fun configClosedLoopPeakOutput(slotIdx: Int, percentOut: Double): ErrorCode {
        // TODO Auto-generated method stub
        return this.configClosedLoopPeakOutput(slotIdx, percentOut, defaultTimeout)
    }


    fun configClosedLoopPeriod(slotIdx: Int, loopTimeMs: Int): ErrorCode {
        // TODO Auto-generated method stub
        return this.configClosedLoopPeriod(slotIdx, loopTimeMs, defaultTimeout)
    }


    fun configAuxPIDPolarity(invert: Boolean): ErrorCode {
        // TODO Auto-generated method stub
        return this.configAuxPIDPolarity(invert, defaultTimeout)
    }


    fun setIntegralAccumulator(iaccum: Double, pidIdx: Int): ErrorCode {
        // TODO Auto-generated method stub
        return this.setIntegralAccumulator(iaccum, pidIdx, defaultTimeout)
    }

    fun configMotionCruiseVelocity(sensorUnitsPer100ms: Int): ErrorCode {
        // TODO Auto-generated method stub
        return this.configMotionCruiseVelocity(sensorUnitsPer100ms, defaultTimeout)
    }


    fun configMotionAcceleration(sensorUnitsPer100msPerSec: Int): ErrorCode {
        // TODO Auto-generated method stub
        return this.configMotionAcceleration(sensorUnitsPer100msPerSec, defaultTimeout)
    }

    fun clearMotionProfileHasUnderrun(): ErrorCode {
        // TODO Auto-generated method stub
        return this.clearMotionProfileHasUnderrun(defaultTimeout)
    }

    fun configMotionProfileTrajectoryPeriod(baseTrajDurationMs: Int): ErrorCode {
        // TODO Auto-generated method stub
        return this.configMotionProfileTrajectoryPeriod(baseTrajDurationMs, defaultTimeout)
    }

    fun clearStickyFaults(): ErrorCode {
        // TODO Auto-generated method stub
        return this.clearStickyFaults(defaultTimeout)
    }

    fun configSetCustomParam(newValue: Int, paramIndex: Int): ErrorCode {
        // TODO Auto-generated method stub
        return this.configSetCustomParam(newValue, paramIndex, defaultTimeout)
    }


    fun configGetCustomParam(paramIndex: Int): Int {
        // TODO Auto-generated method stub
        return this.configGetCustomParam(paramIndex, defaultTimeout)
    }


    fun configSetParameter(param: ParamEnum, value: Double, subValue: Int, ordinal: Int): ErrorCode {
        // TODO Auto-generated method stub
        return this.configSetParameter(param, value, subValue, ordinal, defaultTimeout)
    }


    fun configSetParameter(param: Int, value: Double, subValue: Int, ordinal: Int): ErrorCode {
        // TODO Auto-generated method stub
        return this.configSetParameter(param, value, subValue, ordinal, defaultTimeout)
    }


    fun configGetParameter(param: ParamEnum, ordinal: Int): Double {
        // TODO Auto-generated method stub
        return this.configGetParameter(param, ordinal, defaultTimeout)
    }


    fun configGetParameter(param: Int, ordinal: Int): Double {
        // TODO Auto-generated method stub
        return this.configGetParameter(param, ordinal, defaultTimeout)
    }

    /**
     * Contains all the default states for a TalonSRX.
     * @author Finn Frankis
     * @version Aug 18, 2018
     */
    protected object Default {
        val SENSOR = FeedbackDevice.QuadEncoder
        val SENSOR_PHASE = false
        val INVERTED = false
        val FEEDBACK_COEFFICIENT = 1
        val NEUTRAL_MODE = NeutralMode.Coast

        val QUADRATURE_POSITION = 0

        val VELOCITY_MEASUREMENT_PERIOD = VelocityMeasPeriod.Period_100Ms
        val VELOCITY_MEASUREMENT_WINDOW = 64

        val NOMINAL_OUTPUT_FORWARD = 0.0
        val NOMINAL_OUTPUT_REVERSE = 0.0
        val PEAK_OUTPUT_FORWARD = 1.0
        val PEAK_OUTPUT_REVERSE = -1.0
        val NEUTRAL_DEADBAND = 0.04

        val OPEN_LOOP_RAMP_TIME = 0.0
        val CLOSED_LOOP_RAMP_TIME = 0.0

        val FIRST_PID_SLOT = 0
        val LAST_PID_SLOT = 3
        val FIRST_PID_LOOP = 0
        val LAST_PID_LOOP = 1
        val FPID_VALUE = 0.0
        val IZONE_VALUE = 0
        val I_ACCUMULATOR = 0
        val CLOSED_LOOP_ERROR = 0

        val CRUISE_VELOCITY = 0
        val ACCELERATION = 0
        val MOT_PROF_TRAJECTORY_PERIOD = 0

        val CURRENT_LIMIT_ENABLED = false
        val PEAK_CURRENT_DURATION = 0
        val PEAK_CURRENT_LIMIT = 0
        val CONTINUOUS_CURRENT_LIMIT = 0

        val VOLTAGE_COMP_ENABLED = false
        val VOLTAGE_COMP_SATURATION = 0.0
        val VOLTAGE_MEASUREMENT_FILTER = 32

        val FORWARD_SOFT_LIMIT = 0
        val REVERSE_SOFT_LIMIT = 0
        val SOFT_LIMIT_ENABLED = false
        val LIMIT_SOURCE = LimitSwitchSource.Deactivated
        val LIMIT_NORMAL_TYPE = LimitSwitchNormal.NormallyOpen

        val CLOSED_LOOP_PEAK_OUTPUT = 1.0
        val CLOSED_LOOP_PERIOD = 1
        val AUX_PID_POLARITY = false

        val CONTROL_MODE = ControlMode.Disabled
    }
}
/**
 * Constructs a HSTalon with the default timeout {[Constants.DEFAULT_TIMEOUT].
 * @param deviceNumber The CAN device ID of the Talon.
 */
