package harkerrobolib.subsystems;

import java.util.function.Consumer;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.IMotorController;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import harkerrobolib.wrappers.TalonSRXWrapper;

/**
 * Represents a general Drivetrain subsystem with two master motor controllers and two optional follower controllers.
 * @author Finn Frankis
 * @version Aug 17, 2018
 */
public abstract class DrivetrainSubsystem extends Subsystem {
    private TalonSRXWrapper leftMaster;
    private TalonSRXWrapper rightMaster;
    private IMotorController leftFollower;
    private IMotorController rightFollower;
    private boolean hasFollowers;

    /**
     * Constructs a new Drivetrain subsystem with two master controllers and two follower controllers.
     * 
     * @param leftMaster the left master Talon.
     * @param rightMaster the right master Talon.
     * @param leftFollower the left follower motor controller (Talon or Victor).
     * @param rightFollower the right folllower motor controller (Talon or Victor).
     */
    public DrivetrainSubsystem (TalonSRXWrapper leftMaster, TalonSRXWrapper rightMaster, IMotorController leftFollower,
            IMotorController rightFollower) {
        this.leftMaster = leftMaster;
        this.rightMaster = rightMaster;
        this.leftFollower = leftFollower;
        this.rightFollower = rightFollower;
        
        followMasters();
        hasFollowers = true;
    }

    /**
     * Constructs a new Drivetrain subsystem with two master controllers.
     * 
     * @param leftMaster the left master Talon.
     * @param rightMaster the right master Talon.
     */
    public DrivetrainSubsystem (TalonSRXWrapper leftMaster, TalonSRXWrapper rightMaster) {
        this(leftMaster, rightMaster, null, null);
        hasFollowers = false;
    }

    /**
     * Applies a given operation to both master Talons (see lambdas in Java for more clarification).
     * @param consumer the operation to be applied.
     */
    public void applyToMasters (Consumer<TalonSRXWrapper> consumer) {
        consumer.accept(leftMaster);
        consumer.accept(rightMaster);
    }

    /**
     * Applies a given operation to both master and follower motor controllers.
     * @param consumer the operation to be applied.
     */
    public void applyToAll (Consumer<IMotorController> consumer) {
        consumer.accept(leftMaster);
        consumer.accept(rightMaster);
        
        if (hasFollowers) {
	        consumer.accept(leftFollower);
	        consumer.accept(rightFollower);
        }
    }

    /**
     * Tells the follower controllers to follow the master controllers.
     */
    public void followMasters () {
        if (hasFollowers) {
            leftFollower.follow(leftMaster);
            rightFollower.follow(rightMaster);
        }
    }
    
    /**
     * Sets all active controllers to a given neutral mode (brake or coast).
     * @param nm the neutral mode to be applied.
     */
    public void setNeutralMode (NeutralMode nm) {
        applyToAll((controller) -> {controller.setNeutralMode(nm);});

    }

    /**
     * Configures both masters for current limiting.
     * @param peakLimit the peak limit (temporary, to account for current spikes).
     * @param peakTime the time for which the peak limit is active
     * @param continuousLimit the continuous limit, or the one which is active after the conclusion of the peak limit.
     * @param timeout the time after which a failed current limit command will stop being retried.
     */
    public void setCurrentLimit (int peakLimit, int peakTime, int continuousLimit, int timeout) {
        Consumer<TalonSRXWrapper> currentLimit = (talon) -> {
            int newTimeout = (timeout == -1) ? talon.getDefaultTimeout() : timeout;
            talon.configPeakCurrentLimit(peakLimit, newTimeout);
            talon.configPeakCurrentDuration(peakTime, newTimeout);
            talon.configContinuousCurrentLimit(continuousLimit, newTimeout);
        };
        applyToMasters (currentLimit);
    }
    
    /**
     * Configures both masters for current limiting, where each one's default timeout is used.
     * @param peakLimit the peak limit (temporary, to account for current spikes).
     * @param peakTime the time for which the peak limit is active
     * @param continuousLimit the continuous limit, or the one which is active after the conclusion of the peak limit.
     */
    public void setCurrentLimit (int peakLimit, int peakTime, int continuousLimit) {
        setCurrentLimit(peakLimit, peakTime, continuousLimit, -1);
    }
    
    /**
     * Sets both masters to a given output.
     * @param mode the mode to which both Talons should be set.
     * @param outputValue the value of the output tied to the given control mode.
     */
    public void setBoth (ControlMode mode, double outputValue) {
    	applyToMasters((talon) -> {talon.set(mode, outputValue);});
    }
    
    /**
     * Resets both masters.
     */
    public void resetMasters() {
    	applyToMasters ((talon) -> {talon.reset();});
    }
    
    /**
     * Gets the left master controller.
     * @return the left master controller.
     */
    public TalonSRXWrapper getLeftMaster() {
    	return leftMaster;
    }
    
    /**
     * Gets the right master controller.
     * @return the right master controller.
     */
    public TalonSRXWrapper getRightMaster() {
    	return rightMaster;
    }
    
    /**
     * Gets the left follower controller.
     * @return the left follower controller.
     */
    public IMotorController getLeftFollower() {
    	return leftFollower;
    }
    
    /**
     * Gets the right follower controller.
     * @return the right follower controller.
     */
    public IMotorController getRightFollower() {
    	return rightFollower;
    }
    
    /**
     * Configures both Talons to point to a given sensor.
     * @param fd the type of sensor to which the Talon should use
     * @param pidLoop the loop index where this sensor should be placed [0,1]
     */
    public void configBothFeedbackSensors(FeedbackDevice fd, int pidLoop)
    {
        getLeftMaster().configSelectedFeedbackSensor(fd, 
                    pidLoop);
        getRightMaster().configSelectedFeedbackSensor(fd, 
                    pidLoop);
    }
    
    /**
     * Prints the current output percentage to the motors to SmartDashboard.
     */
    public void printMotorOutputPercentage()
    {
        SmartDashboard.putNumber("Left Talon Output Percentage", getLeftMaster().getMotorOutputPercent());
        SmartDashboard.putNumber("Right Talon Output Percentage", getRightMaster().getMotorOutputPercent());
    }
    
    /**
     * Prints the closed loop error of the Talons in a given loop.
     * @param pidLoop the loop index [0,1]
     */
    public void printClosedLoopError (int pidLoop)
    {
        SmartDashboard.putNumber("Left Talon Closed Loop Error " + (pidLoop == 0 ? "Primary" : "Auxiliary"), getLeftMaster().getClosedLoopError(pidLoop));
        SmartDashboard.putNumber("Right Talon Closed Loop Error " + (pidLoop == 0 ? "Primary" : "Auxiliary"), getRightMaster().getClosedLoopError(pidLoop));
    }
    
    /**
     * Prints the sensor positions of the Talons in a given loop.
     * @param pidLoop the loop index [0,1]
     */
    public void printSensorPositions (int pidLoop)
    {
        SmartDashboard.putNumber("Left Talon Position " + (pidLoop == 0 ? "Primary" : "Auxiliary"), getLeftMaster().getSelectedSensorPosition(pidLoop));
        SmartDashboard.putNumber("Right Talon Position" + (pidLoop == 0 ? "Primary" : "Auxiliary"), getRightMaster().getSelectedSensorPosition(pidLoop));
    }
    
    /**
     * Determines whether the closed loop error for both sides is within a given value.
     * @param loopIndex the loop index, either primary or auxiliary [0,1]
     * @param allowableError the error tolerance to be checked
     * @return true if the absolute value of the error is within the value; false otherwise
     */
    public boolean isClosedLoopErrorWithin (int loopIndex, double allowableError)
    {
        return Math.abs(getLeftMaster().getClosedLoopError(loopIndex)) < allowableError
                && Math.abs(getRightMaster().getClosedLoopError(loopIndex)) < allowableError;
    }
}
