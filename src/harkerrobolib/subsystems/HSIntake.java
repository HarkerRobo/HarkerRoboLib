package harkerrobolib.subsystems;

import java.util.function.Consumer;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import harkerrobolib.wrappers.HSMotorController;
import harkerrobolib.wrappers.HSTalon;

/**
 * Represents a general Intake subsystem with two motor controllers.
 *
 * @param leftMotor the left Talon
 * @param rightTalon the right Talon
 *
 * @author Angela Jia
 * @author Finn Frankis
 *
 * @version 11/7/18
 */
public abstract class HSIntake<Motor extends HSMotorController> extends SubsystemBase {

	private Motor leftMotor;
	private Motor rightTalon;
	
	public HSIntake(Motor leftMotor, Motor rightTalon) {
		this.leftMotor = leftMotor;
		this.rightTalon = rightTalon;
    }
    
    /**
     * Represents direction of intake.
     *
     * @param signum sign for output of left intake with given direction (must be negated for right)
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
    public void setNeutralModes(NeutralMode neutralMode) {
        leftMotor.setNeutralMode(neutralMode);
        rightTalon.setNeutralMode(neutralMode);
    }

    public void invertTalons(boolean leftInverted, boolean rightInverted) {
        leftMotor.setInverted(leftInverted);
        rightTalon.setInverted(rightInverted);
    }

    /**
     * Sets the same current limits to both talons.
     *
     * @param peakTime the amount of time peak current is the active limit
     * @param peakCurrent the amount of current during peak time
     * @param contCurrent the amount of current during cont time
     * @timeout the time after which a failed CAN command will stop being retried
     */
    public void setCurrentLimits(int peakTime, int peakCurrent, int contCurrent) {
        Consumer<Motor> applyCurrentLimit =  (talon) -> {
            talon.configPeakCurrentDuration(peakTime);
            talon.configPeakCurrentLimit(peakCurrent);
            talon.configContinuousCurrentLimit(contCurrent);
            talon.enableCurrentLimit(true);
            };
        
        applyCurrentLimit.accept(leftMotor);
        applyCurrentLimit.accept(rightTalon);
    }

    /**
     * Sets wheels to the same output and direction.
     *
     * @param output value from controller that sets output for left and right wheels to same value
     * @param direction sets both wheels to either IN or OUT direction
     */
    public void setOutput(double output, IntakeDirection direction) {
        if (direction == IntakeDirection.IN) {
            setOutput(output, output, IntakeDirection.IN, IntakeDirection.IN);
        } else {
            setOutput(output, output, IntakeDirection.OUT, IntakeDirection.OUT);
        }

    }

    /**
     * Sets each wheel to its specified output.
     *
     * @param leftOutput value that sets output for the left wheel
     * @param rightOutput value that sets output for the right wheel
     */
    public void setOutput(double leftOutput, double rightOutput) {
        leftMotor.set(ControlMode.PercentOutput, leftOutput);
        rightTalon.set(ControlMode.PercentOutput, rightOutput);
    }

    /**
     * Sets wheels to their respective outputs and directions.
     *
     * @param leftOutput value from controller that sets left wheel output
     * @param rightOutput value from controller that sets right wheel output
     * @param leftDirection enum representing left wheel's direction
     * @param rightDirection enum representing right wheel's direction
     */
    public void setOutput(double leftOutput, double rightOutput, IntakeDirection leftDirection, IntakeDirection rightDirection) {
        if (leftDirection == IntakeDirection.IN) {
            leftMotor.set(ControlMode.PercentOutput, leftDirection.signum * leftOutput);
        } else {
            leftMotor.set(ControlMode.PercentOutput, leftDirection.signum * -leftOutput);
        }
        if (rightDirection == IntakeDirection.IN) {
            rightTalon.set(ControlMode.PercentOutput, rightDirection.signum * -rightOutput);
        } else {
            rightTalon.set(ControlMode.PercentOutput, rightDirection.signum * rightOutput);
        }

    }
    
    public Motor getleftMotor() {
        return leftMotor;
    }
    
    public Motor getRightTalon() {
        return rightTalon;
    }

}