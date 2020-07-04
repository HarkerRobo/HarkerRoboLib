package harkerrobolib.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import harkerrobolib.util.Conversions;
import harkerrobolib.util.Conversions.SpeedUnit;
import harkerrobolib.wrappers.HSMotorController;

import edu.wpi.first.wpilibj.DoubleSolenoid;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatorCurrentLimitConfiguration;

/**
 * Represents a general Flywheel shooter with a master talon and a follower talon.
 * 
 * @author Aimee Wang
 * @author Kunal Jain
 * @author Ada Praun-Petrovic
 * @author Angela Jia
 * @since 7/2/20
 */
public abstract class HSFlywheel<Motor extends HSMotorController> extends SubsystemBase {

    private Motor master;
    private Motor follower;
    private final InvertType MASTER_INVERT;
    private final InvertType FOLLOWER_INVERT;

    public static int VELOCITY_SLOT;

    private static boolean SENSOR_PHASE;

    private static double WHEEL_DIAMETER;
    private static int TICKS_PER_REVOLUTION;
    private static double GEAR_RATIO;
    
    public static double VOLTAGE_COMP;
    
    public static double STALL_MIN_VELOCITY;
    public static double STALL_CURRENT;

    private double[] velocityPIDConstants;

    /**
     * Constructs an instance of HSFlywheel. No current limit setup
     * 
     * @param shooterMasterID CAN ID of the master talon
     * @param shooterFollowerID CAN ID of the follower talon
     */
    public HSFlywheel(Motor shooterMaster, Motor shooterFollower, double wheelDiameter, int ticksPerRevolution, double gearRatio, boolean sensorPhase, InvertType masterInvert, InvertType followerInvert, int velocitySlot, double[] velocityPIDConstants, double minVelocity, double voltageComp, double stallCurrent) {

        master = shooterMaster;
        follower = shooterFollower;

        TICKS_PER_REVOLUTION = ticksPerRevolution;
        WHEEL_DIAMETER = wheelDiameter;
        GEAR_RATIO = gearRatio;
        SENSOR_PHASE = sensorPhase;
        MASTER_INVERT = masterInvert;
        FOLLOWER_INVERT = followerInvert;
        VOLTAGE_COMP = voltageComp;
        VELOCITY_SLOT = velocitySlot;
        STALL_MIN_VELOCITY = minVelocity;
        STALL_CURRENT = stallCurrent;

        setupFlywheel();
    }

    /**
     * Sets up the master and follower talons.
     */
    public void setupFlywheel() {

        master.configFactoryDefault();
        follower.configFactoryDefault();

        follower.follow(master);

        master.setInverted(MASTER_INVERT);
        follower.setInverted(FOLLOWER_INVERT);  

        master.setSensorPhase(SENSOR_PHASE);
        master.setNeutralMode(NeutralMode.Coast);
        follower.setNeutralMode(NeutralMode.Coast);

        master.configVoltageCompSaturation(VOLTAGE_COMP);
        master.enableVoltageCompensation(true);
        
        setUpVelocityPID();

    }

    public void spinShooterPercentOutput(double percentOutput) {

        if(percentOutput == 0)
            master.set(ControlMode.Disabled, 0);
        else
            master.set(ControlMode.PercentOutput, percentOutput);

    }

    public void spinShooterVelocity(double velocity) {
    
        double velocityTicks = Conversions.convertSpeed(SpeedUnit.FEET_PER_SECOND, velocity * GEAR_RATIO, SpeedUnit.ENCODER_UNITS, WHEEL_DIAMETER, TICKS_PER_REVOLUTION);
        master.set(ControlMode.Velocity, velocityTicks);

    }

    public void setUpVelocityPID() {
        master.config_kF(VELOCITY_SLOT, velocityPIDConstants[0]);
        master.config_kP(VELOCITY_SLOT, velocityPIDConstants[1]);
        master.config_kI(VELOCITY_SLOT, velocityPIDConstants[2]);
        master.config_kD(VELOCITY_SLOT, velocityPIDConstants[3]);

        if (velocityPIDConstants.length > 4)
            master.config_IntegralZone(VELOCITY_SLOT, (int) velocityPIDConstants[4]);
    }

    public boolean checkStalling(){
        return (master.getStatorCurrent() > STALL_CURRENT && master.getSelectedSensorVelocity() < STALL_MIN_VELOCITY);
    }

    public Motor getMaster() {

        return master;
    }

    public Motor getFollower() {

        return follower;
    }

    @Override
    public void periodic() {
 
    }
    
}