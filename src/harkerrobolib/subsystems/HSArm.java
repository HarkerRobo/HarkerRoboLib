package harkerrobolib.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import harkerrobolib.wrappers.HSMotorController;

/**
 * Represents a general Arm subsystem with a single motor controller.
 *
 * @param talon the arm's Talon
 *
 * @author Finn Frankis
 * @author Angela Jia
 * @version 10/31/18
 */
public abstract class HSArm<Motor extends HSMotorController> extends SubsystemBase {

	private Motor talon;
    private final double feedForwardGrav;
    
    public enum ArmDirection{
        UP(1), DOWN(-1);
    	private int sign;
    	private ArmDirection(int sign) {
    		this.sign = sign;
    	}
    }
	
	public HSArm(Motor talon, double feedForwardGrav)
	{
		this.talon = talon;
		this.feedForwardGrav = feedForwardGrav;
    }

    public void setCurrentLimits(int peakTime, int peakCurrent, int contCurrent) {
        talon.configPeakCurrentDuration(peakTime);
        talon.configPeakCurrentLimit(peakCurrent);
        talon.configContinuousCurrentLimit(contCurrent);
        talon.enableCurrentLimit(true);
    }

    public void armMotionPercentOutput(double output, ArmDirection direction) {
        armMotionPercentOutput(direction.sign * output);
    }

    public void armMotionPercentOutput(double output) {
        talon.set(ControlMode.PercentOutput, output, DemandType.ArbitraryFeedForward, feedForwardGrav);
    }

    public double getTalonCurrent() {
    	return talon.getOutputCurrent();
    }
    
    public Motor getTalon() {
        return talon;
    }
    
    public double getFeedForwardGrav() {
        return feedForwardGrav;
    }
}