package harkerrobolib.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatorCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import harkerrobolib.util.Conversions;
import harkerrobolib.util.Gains;
import harkerrobolib.util.Conversions.SpeedUnit;

/**
 * Represents the Shooter Subsystem, controlled by two Falcon 500s.
 * 
 * @author Jatin Kohli
 * @author Shahzeb Lakhani 
 * @author Anirudh Kotamraju
 * @author Arjun Dixit
 * @author Aimee Wang
 * @author Rohan Bhowmik
 * @author Chirag Kaushik
 * 
 * @since April 25, 2020
 */
public abstract class HSShooter extends SubsystemBase {
    private TalonFX master;
    private TalonFX follower;

    private boolean sensorPhase;
    private int gearRatio;
    private double wheelDiameter;
    private int ticksPerRev;

    /**
     * Constructs a shooter
     * 
     * @param master The master Falcon
     * @param follower The follower Falcon
     * @param sensorPhase The sensor phase for the master Falcon encoder
     * @param gearRatio The gear ratio from motor to flywheel
     * @param wheelDiameter The diameter of the shooter wheels
     * @param ticksPerRev The amount of encoder ticks per revolution
     */
    public HSShooter(TalonFX master, TalonFX follower,
        boolean sensorPhase, int gearRatio, double wheelDiameter, int ticksPerRev) {
        this.master = master;
        this.follower = follower;
        this.sensorPhase = sensorPhase;

        this.gearRatio = gearRatio;
        this.wheelDiameter = wheelDiameter;
        this.ticksPerRev = ticksPerRev;
        
        setupFlywheel();
    }

    /**
     * Sets the master and follower inverts
     * @param masterInverted the master invert
     * @param followerInverted the follower invert
     */
    public void setInverted(boolean masterInverted, boolean followerInverted) {
        master.setInverted(masterInverted);
        follower.setInverted(followerInverted);
    }
    
    /**
     * Configs the voltage comp
     * @param voltageComp to be set
     * @param enable whether or not to enable voltage compensation
     */
    public void configVoltageComp(double voltageComp, boolean enable) {
        master.configVoltageCompSaturation(voltageComp);
        master.enableVoltageCompensation(enable);
    }

    /**
     * Configs stator current limits
     * @param continuousLimit The continuous current limit
     * @param peakLimit The peak current limit
     * @param peakDur The amount of time that the motor is allowed 
     * to peak before output is stopped
     */
    public void configStatorCurrentLimit(double continuousLimit, double peakLimit, double peakDur) {
        master.configStatorCurrentLimit(
            new StatorCurrentLimitConfiguration(true, continuousLimit, peakLimit, peakDur));
    }

    /**
     * Sets up the master and follower talons.
     */
    public void setupFlywheel() {
        master.configFactoryDefault();
        follower.configFactoryDefault();
        
        follower.follow(master);
        
        master.setNeutralMode(NeutralMode.Coast);
      
        master.setSensorPhase(sensorPhase);

        master.configForwardSoftLimitEnable(false);
        master.configReverseSoftLimitEnable(false);
        master.overrideLimitSwitchesEnable(false);
    }

    /**
     * Set up for Velocity PID.
     * 
     * @param velocitySlot the slot for flywheel velocity
     * @param pidGains the pid constants to be set
     */
    public void setupVelocityPID(int velocitySlot, Gains pidGains) {
        master.config_kF(velocitySlot, pidGains.getkF());
        master.config_kP(velocitySlot, pidGains.getkP());
        master.config_kI(velocitySlot, pidGains.getkI());
        master.config_kD(velocitySlot, pidGains.getkD());
        master.config_IntegralZone(velocitySlot, pidGains.getIZone());
    }

    /**
     * Spins the shooter flywheel at a certain percent output
     * 
     * @param percentOutput The percent output to set the motors to
     */
    public void spinShooterPercentOutput(double percentOutput) {
        if(percentOutput == 0) 
            master.set(ControlMode.Disabled, 0);
        else
            master.set(ControlMode.PercentOutput, percentOutput);
    }

    /**
     * Spins the shooter flywheel at a certain velocity (in feet/second)
     * 
     * @param velocity the velocity to set
     */
    public void spinShooterVelocity(double velocity) {
        double velocityInTicks = Conversions.convertSpeed(SpeedUnit.FEET_PER_SECOND, velocity * gearRatio, SpeedUnit.ENCODER_UNITS, wheelDiameter, ticksPerRev);
        if(0.95 * velocityInTicks > master.getSelectedSensorVelocity())
            master.set(ControlMode.PercentOutput, 1);
        else 
            master.set(ControlMode.Velocity, velocityInTicks);
    }

    /**
     * Gets the master falcon.
     * 
     * @return the master falcon
     */
    public TalonFX getMaster() {
        return master;
    }

    /**
     * Gets the follower falcon.
     * 
     * @return the follower falcon
     */
    public TalonFX getFollower() {
        return follower;
    }
}
