package harkerrobolib.wrappers;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.VelocityMeasPeriod;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

/**
 * TalonSRX wrapper class to reset to factory defaults and more
 * Updated from old Talon wrapper class
 * @author atierno
 * @author Jatin
 */
public class TalonSRXWrapper extends TalonSRX {
    
//    private boolean isReversed = false;
    private final int timeout;
    
    /**
     * Initializes a TalonSRXWrapper with its CAN device number.
     * @param deviceNumber The CAN device number of the Talon.
     */
    public TalonSRXWrapper (int deviceNumber) {
        super(deviceNumber);
        timeout = 10;
    }
    
//    /**
//     * Initializes a TalonWrapper with its channel and reversed flag.
//     * @param channel The PWM channel on the digital module the Talon is attached to.
//     * @param isReversed Whether or not the output of this Talon should be flipped.
//     */
//    public TalonSRXWrapper (int channel, boolean isReversed) {
//        super(channel);
//        this.isReversed = isReversed;
//    }
    
//    /**
//     * Sets the speed of the Talon (handles reversing).
//     * @param speed The speed to set the Talon.
//     */
//    public void set (double speed) {
//        super.set(isReversed ? -speed : speed);
//    }
    
//    /**
//     * Sets the reversed flag of the Talon.
//     * @param flag Set whether or not to reverse the Talon output.
//     */
//    public void setReversed (boolean flag) {
//        isReversed = flag;
//    }
    
//    /**
//     * Gets the reversed flag of the Talon.
//     * @return Whether or not the Talon output is reversed
//     */
//    public boolean getReversed () {
//        return isReversed;
//    }
    
    /**
     * Resets the Talon to its factory defaults, software equivalent of rebooting the talon,<br>
     * based on <a href=https://github.com/CrossTheRoadElec/Phoenix-Documentation>CTRE's Github</a> 
     * and <a href=https://github.com/Team973/2018-inseason/blob/dev/lib/helpers/GreyTalon.h>973's Factory Reset Method</a>
     */
    public void setToFactoryDefault() {
        configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, timeout);  
//        configSelectedFeedbackCoefficient(1.0, 0, timeout)
        setSensorPhase(false);
        setInverted(false);
        setNeutralMode(NeutralMode.Coast);
        getSensorCollection().setQuadraturePosition(0, 0);
        configVelocityMeasurementPeriod(VelocityMeasPeriod.Period_100Ms, timeout);
        configVelocityMeasurementWindow(64, timeout);
//        configSetCustomParam(0, 0, timeout);
//        configSetCustomParam(0, 1, timeout);

        configNominalOutputForward(0.0, timeout);
        configNominalOutputReverse(0.0, timeout);
        configPeakOutputForward(1.0, timeout);
        configPeakOutputReverse(-1.0, timeout);
        configNeutralDeadband(0.04, timeout);
        configOpenloopRamp(0, timeout);
        configClosedloopRamp(0, timeout);

        // Gains
        config_kP(0, 0.0, timeout);
        config_kI(0, 0.0, timeout);
        config_kD(0, 0.0, timeout);
        config_kF(0, 0.0, timeout);
        configMotionCruiseVelocity(0, timeout);
        configMotionAcceleration(0, timeout);
        config_IntegralZone(0, 0, timeout);
        configMaxIntegralAccumulator(0, 0, timeout);
        configAllowableClosedloopError(0, 0, timeout);
        selectProfileSlot(0, 0);
        configMotionProfileTrajectoryPeriod(0, timeout);

        // Limiting
        enableCurrentLimit(false);
        configPeakCurrentDuration(0, timeout);
        configPeakCurrentLimit(0, timeout);
        configContinuousCurrentLimit(0, timeout);
        enableVoltageCompensation(false);
        configVoltageCompSaturation(0, timeout);
        configVoltageMeasurementFilter(32, timeout);
        configForwardSoftLimitThreshold(0, timeout);
        configReverseSoftLimitThreshold(0, timeout);
        configForwardSoftLimitEnable(false, timeout);
        configReverseSoftLimitEnable(false, timeout);
//        configForwardLimitSwitchSource(LimitSwitchSource.Deactivated, LimitSwitchNormal.NormallyOpen, timeout);
//        configReverseLimitSwitchSource(LimitSwitchSource.Deactivated, LimitSwitchNormal.NormallyOpen, timeout);

//        configSensorTerm(SensorTerm.Diff0, FeedbackDevice.QuadEncoder, timeout);
//        configSensorTerm(SensorTerm.Diff1, FeedbackDevice.QuadEncoder, timeout);
//        configSensorTerm(SensorTerm.Sum0, FeedbackDevice.QuadEncoder, timeout);
//        configSensorTerm(SensorTerm.Sum1, FeedbackDevice.QuadEncoder, timeout);
        
//        configClosedLoopPeakOutput(0, 1.0, timeout);
//        configClosedLoopPeriod(0, 1, timeout);
        
//        configAuxPIDPolarity(false, timeout);
        
        set(ControlMode.Disabled, 0);
    }
}
