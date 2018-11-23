package harkerrobolib.subsystems;

import java.util.function.Consumer;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.wpilibj.command.Subsystem;
import harkerrobolib.wrappers.HSTalon;

/**
 * Represents a general Intake subsystem with two motor controllers.
 *
 * @param leftTalon the left Talon
 * @param rightTalon the right Talon
 *
 * @author Angela Jia
 * @author Finn Frankis
 *
 * @version 11/7/18
 */
public abstract class HSIntake extends Subsystem {

	private HSTalon leftTalon;
	private HSTalon rightTalon;
	
	public HSIntake(HSTalon leftTalon, HSTalon rightTalon) {
		this.leftTalon = leftTalon;
		this.rightTalon = rightTalon;
	}
    /**
     * Represents direction of intake.
     *
     * @param signum sign for output of left intake with given direction (must be negated for right)
     */
    enum IntakeDirection {
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
        leftTalon.setNeutralMode(neutralMode);
        rightTalon.setNeutralMode(neutralMode);
    }

    /**
     * Sets the same current limits to both talons.
     *
     * @param peakTime the amount of time peak current is the active limit
     * @param peakCurrent the amount of current during peak time
     * @param contCurrent the amount of current during cont time
     * @timeout the time after which a failed CAN command will stop being retried
     */
    public void setCurrentLimits(int peakTime, int peakCurrent, int contCurrent, int timeout) {
        Consumer<HSTalon> applyCurrentLimit =  (talon) -> {
            int newTimeout = timeout == -1 ? talon.getDefaultTimeout() : timeout;
            talon.configPeakCurrentDuration(peakTime, newTimeout);
            talon.configPeakCurrentLimit(peakCurrent, newTimeout);
            talon.configContinuousCurrentLimit(contCurrent, newTimeout);
            talon.enableCurrentLimit(true);
            };
        
        applyCurrentLimit.accept(leftTalon);
        applyCurrentLimit.accept(rightTalon);
    }

    public void setCurrentLimits(int peakTime, int peakCurrent, int contCurrent) {
        setCurrentLimits (peakTime, peakCurrent, contCurrent, -1);
    }

    /**
     * Sets wheels to the same output and direction.
     *
     * @param output value from controller that sets output for left and right wheels to same value
     * @param direction sets both wheels to either IN or OUT direction
     */
    public void intakeOuttakeCube(double output, IntakeDirection direction) {
        if (direction == IntakeDirection.IN) {
            intakeOuttakeCube(output, output, IntakeDirection.IN, IntakeDirection.IN);
        } else {
            intakeOuttakeCube(output, output, IntakeDirection.OUT, IntakeDirection.OUT);
        }

    }

    /**
     * Sets each wheel to its specified output.
     *
     * @param leftOutput value that sets output for the left wheel
     * @param rightOutput value that sets output for the right wheel
     */
    public void intakeOuttakeCube(double leftOutput, double rightOutput) {
        leftTalon.set(ControlMode.PercentOutput, leftOutput);
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
    public void intakeOuttakeCube(double leftOutput, double rightOutput, IntakeDirection leftDirection, IntakeDirection rightDirection) {
        if (leftDirection == IntakeDirection.IN) {
            leftTalon.set(ControlMode.PercentOutput, leftDirection.signum * leftOutput);
        } else {
            leftTalon.set(ControlMode.PercentOutput, leftDirection.signum * -leftOutput);
        }
        if (rightDirection == IntakeDirection.IN) {
            rightTalon.set(ControlMode.PercentOutput, rightDirection.signum * -rightOutput);
        } else {
            rightTalon.set(ControlMode.PercentOutput, rightDirection.signum * rightOutput);
        }

    }
    
    public HSTalon getLeftTalon() {
        return leftTalon;
    }
    
    public HSTalon getRightTalon() {
        return rightTalon;
    }

}