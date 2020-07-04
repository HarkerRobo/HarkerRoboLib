package harkerrobolib.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import harkerrobolib.util.Conversions;
import harkerrobolib.util.Conversions.SpeedUnit;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import edu.wpi.first.wpilibj.DoubleSolenoid;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;

/**
 * Represents a general Flywheel shooter with a master talon and a follower talon.
 * 
 * @author Aimee Wang
 * @since 7/2/20
 */
public class HSFlywheel extends SubsystemBase {

    private static TalonFX master;
    private static TalonFX follower;
    private static TalonFXInvertType masterInvert;
    private static TalonFXInvertType followerInvert;

    public static double FLYWHEEL_KF = 0;
    public static double FLYWHEEL_KP = 0;
    public static double FLYWHEEL_KI = 0;
    public static double FLYWHEEL_KD = 0;

    public static final int VELOCITY_SLOT = 0;

    private static boolean SENSOR_PHASE = false;

    private static double WHEEL_DIAMETER;
    private static int TICKS_PER_REVOLUTION;
    private static double GEAR_RATIO;


    /**
     * Constructs an instance of HSFlywheel
     * 
     * @param shooterMasterID CAN ID of the master talon
     * @param shooterFollowerID CAN ID of the follower talon
     */
    public HSFlywheel(int shooterMasterID, int shooterFollowerID, double wheelDiameter, int ticksPerRevolution, double gearRatio) {

        master = new TalonFX(shooterMasterID);
        follower = new TalonFX(shooterFollowerID);

        TICKS_PER_REVOLUTION = ticksPerRevolution;
        WHEEL_DIAMETER = wheelDiameter;
        GEAR_RATIO = gearRatio;

        setupFlywheel();
    }

    /**
     * sets up the master and follower talons
     */
    public void setupFlywheel() {

        master.configFactoryDefault();
        follower.configFactoryDefault();

        follower.follow(master);

        master.setInverted(masterInvert);
        follower.setInverted(followerInvert);  

        master.setSensorPhase(SENSOR_PHASE);
        master.setNeutralMode(NeutralMode.Coast);
        
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
        master.config_kF(VELOCITY_SLOT, FLYWHEEL_KF);
        master.config_kP(VELOCITY_SLOT, FLYWHEEL_KP);
        master.config_kI(VELOCITY_SLOT, FLYWHEEL_KI);
        master.config_kD(VELOCITY_SLOT, FLYWHEEL_KD);
    }

    public TalonFX getMaster() {

        return master;
    }

    public TalonFX getFollower() {

        return follower;
    }

    @Override
    public void periodic() {
 

    }
    
}

