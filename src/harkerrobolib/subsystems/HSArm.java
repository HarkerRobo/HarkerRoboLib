package harkerrobolib.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;

import edu.wpi.first.wpilibj.command.Subsystem;
import harkerrobolib.wrappers.HSTalon;

/**
 * Represents a general Arm subsystem with a single motor controller.
 *
 * @param  talon the arm's Talon
 *
 * @author Finn Frankis
 * @author Angela Jia
 * @version 10/31/18
 */
public abstract class HSArm extends Subsystem {

	private HSTalon talon;
	private final double feedForwardGrav;
	
	public HSArm(HSTalon talon, double feedForwardGrav)
	{
		this.talon = talon;
		this.feedForwardGrav = feedForwardGrav;
	}
    public enum ArmDirection{
        UP(1), DOWN(-1);
    	private int sign;
    	private ArmDirection(int sign) {
    		this.sign = sign;
    	}
    }

    public void setCurrentLimits( int peakTime, int peakCurrent, int contCurrent, int timeout) {
        talon.configPeakCurrentDuration(peakTime, timeout);
        talon.configPeakCurrentLimit(peakCurrent, timeout);
        talon.configContinuousCurrentLimit(contCurrent, timeout);
        talon.enableCurrentLimit(true);
    }

    public void setCurrentLimits( int peakTime, int peakCurrent, int contCurrent) {
        setCurrentLimits(peakTime, peakCurrent, contCurrent, talon.getDefaultTimeout());
}

    public void armMotionPercentOutput(double output, ArmDirection direction) {
        armMotionPercentOutput(direction.sign * output);
    }

    public void armMotionPercentOutput (double output) {
        talon.set(ControlMode.PercentOutput, output, DemandType.ArbitraryFeedForward, feedForwardGrav);
    }

    public double getTalonCurrent() {
    	return talon.getOutputCurrent();
    }
    
    public HSTalon getTalon() {
        return talon;
    }
    
    public double getFeedForwardGrav() {
        return feedForwardGrav;
    }
}