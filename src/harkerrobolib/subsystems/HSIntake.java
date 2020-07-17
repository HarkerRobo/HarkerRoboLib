package harkerrobolib.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatorCurrentLimitConfiguration;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import harkerrobolib.wrappers.HSMotorController;

/**
 * Represents a general Intake subsystem with two motor controllers.
 *
 * @param masterMotor the master Talon
 * @param followerMotor the follower Talon
 *
 * @author Angela Jia
 * @author Finn Frankis
 *
 * @version 11/7/18
 */
public abstract class HSIntake<Motor extends HSMotorController> extends SubsystemBase {

	private Motor masterMotor;
	private Motor followerMotor;
    private DoubleSolenoid solenoid;

    public HSIntake(Motor masterMotor, Motor followerMotor) {
        this.masterMotor = masterMotor;
		this.followerMotor = followerMotor;
        this.solenoid = null;
    }

    public HSIntake(Motor motor) {
        this.masterMotor = motor;
        this.followerMotor = null;
        this.solenoid = null;
    }
    

	public HSIntake(Motor masterMotor, Motor followerMotor, 
                    DoubleSolenoid solenoid) {
		this.masterMotor = masterMotor;
		this.followerMotor = followerMotor;
        this.solenoid = solenoid;

        followerMotor.follow(masterMotor);
    }


    public HSIntake(Motor motor, DoubleSolenoid solenoid) {
        this.masterMotor = motor;
        this.followerMotor = null;
        this.solenoid = solenoid;
    }
    
    /**
     * Represents direction of intake.
     *
     * @param signum sign for output of master intake with given direction (must be negated for follower)
     */
    public enum IntakeDirection {
        IN(1), OUT(-1);
    	private int signum;
    	private IntakeDirection(int signum) {
    		this.signum = signum;
    	}
    }

    /**
     * Sets the same neutral mode (brake or coast) for the two motor contollers.
     *
     * @param neutralMode the specified NeutralMode
     */
    public void setNeutralMode(NeutralMode neutralMode) {
        masterMotor.setNeutralMode(neutralMode);
    }

    public void setSensorPhase(boolean sensorPhase) {
        masterMotor.setSensorPhase(sensorPhase);
    }

    public void setSensorPhases(boolean masterSensorPhase, boolean followerSensorPhase)
    {
        masterMotor.setSensorPhase(masterSensorPhase);
        if(followerMotor != null)
            followerMotor.setSensorPhase(followerSensorPhase);
    }

    public void invertTalon(boolean inverted) {
        masterMotor.setInverted(inverted);
    }

    public void invertTalons(boolean masterInverted, boolean followerInverted) {
        masterMotor.setInverted(masterInverted);
        if(followerMotor != null)
            followerMotor.setInverted(followerInverted);
    }

    /**
     * Sets the same current limits to both talons.
     *
     * @param peakTime the amount of time peak current is the active limit (seconds)
     * @param peakCurrent the amount of current during peak time
     * @param contCurrent the amount of current during cont time
     * @timeout the time after which a failed CAN command will stop being retried
     */
    public void setCurrentLimits(double peakTimeSeconds, double peakCurrent, double contCurrent) {
        masterMotor.configStatorCurrentLimit(new StatorCurrentLimitConfiguration(true, contCurrent, peakCurrent, peakTimeSeconds));
    }

    /**
     * Sets wheels to the same output and direction.
     *
     * @param output value from controller that sets output for master and follower wheels to same value
     * @param direction sets both wheels to either IN or OUT direction
     */
    public void setOutput(double output, IntakeDirection direction, boolean isPercentOutput) {
        ControlMode mode = isPercentOutput ? ControlMode.PercentOutput : ControlMode.Velocity;
        masterMotor.set(mode, direction.signum * output);
    }

    public boolean isStalling(double minCurrentDraw, double jammedVelocity) {
            return (masterMotor.getOutputCurrent() > minCurrentDraw && 
                   masterMotor.getSelectedSensorVelocity() < jammedVelocity) || (followerMotor != null &&
                   (followerMotor.getOutputCurrent() > minCurrentDraw && 
                   followerMotor.getSelectedSensorVelocity() < jammedVelocity));
    }

    public void toggleSolenoid() {
        solenoid.set(solenoid.get() == Value.kReverse ? Value.kForward : Value.kReverse);
    }
    
    public Motor getMasterMotor() {
        return masterMotor;
    }
    
    public Motor getFollowerMotor() {
        return followerMotor;
    }

    public DoubleSolenoid getSolenoid() {
        return solenoid;
    }
}