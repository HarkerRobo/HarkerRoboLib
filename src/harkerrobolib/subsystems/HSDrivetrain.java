package harkerrobolib.subsystems;

import java.util.function.Consumer;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.IMotorController;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.sensors.PigeonIMU;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import harkerrobolib.util.Gains;
import harkerrobolib.wrappers.HSPigeon;
import harkerrobolib.wrappers.HSTalon;

/**
 * Represents a general Drivetrain subsystem with two master motor controllers and two optional follower controllers.
 * @author Finn Frankis
 * @version Aug 17, 2018
 */
public abstract class HSDrivetrain extends Subsystem {
    private HSTalon leftMaster;
    private HSTalon rightMaster;
    private IMotorController leftFollower;
    private IMotorController rightFollower;
    
    private HSPigeon pigeon;
    
    private boolean hasFollowers;
    private boolean hasPigeon;

    /**
     * Constructs a new Drivetrain subsystem with two master controllers, two follower controllers, and a Pigeon.
     * 
     * @param leftMaster the left master Talon.
     * @param rightMaster the right master Talon.
     * @param leftFollower the left follower motor controller (Talon or Victor).
     * @param rightFollower the right folllower motor controller (Talon or Victor).
     * @param pigeon the Pigeon gyroscope.
     */
    public HSDrivetrain(HSTalon leftMaster, HSTalon rightMaster, IMotorController leftFollower,
            IMotorController rightFollower, HSPigeon pigeon) {
        this.leftMaster = leftMaster;
        this.rightMaster = rightMaster;
        this.leftFollower = leftFollower;
        this.rightFollower = rightFollower;
        this.pigeon = pigeon;
        
        followMasters();
        hasFollowers = true;
        hasPigeon = true;
    }
    
    /**
     * Constructs a new Drivetrain subsystem with two master controllers and two follower controllers.
     * 
     * @param leftMaster the left master Talon.
     * @param rightMaster the right master Talon.
     * @param leftFollower the left follower motor controller (Talon or Victor).
     * @param rightFollower the right folllower motor controller (Talon or Victor).
     */
    public HSDrivetrain (HSTalon leftMaster, HSTalon rightMaster, IMotorController leftFollower,
            IMotorController rightFollower) {
    	this(leftMaster, rightMaster, leftFollower, rightFollower, null);
    	hasPigeon = false;
    }

    /**
     * Constructs a new Drivetrain subsystem with two master controllers.
     * 
     * @param leftMaster the left master Talon.
     * @param rightMaster the right master Talon.
     */
    public HSDrivetrain (HSTalon leftMaster, HSTalon rightMaster) {
        this(leftMaster, rightMaster, null, null);
        hasFollowers = false;
    }
    
    /**
     * Constructs a new Drivetrain subsystem without follower controllers but with a Pigeon.
     * @param leftMaster the left master Talon.
     * @param rightMaster the right master Talon.
     * @param pigeon the Pigeon gyroscope.
     */
    public HSDrivetrain (HSTalon leftMaster, HSTalon rightMaster, HSPigeon pigeon) {
    	this(leftMaster, rightMaster, null, null, pigeon);
    	hasFollowers = false;
    }

    /**
     * Applies a given operation to both master Talons (see lambdas in Java for more clarification).
     * @param consumer the operation to be applied.
     */
    public void applyToMasters (Consumer<HSTalon> consumer) {
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
        Consumer<HSTalon> currentLimit = (talon) -> {
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
    public HSTalon getLeftMaster() {
    	return leftMaster;
    }
    
    /**
     * Gets the right master controller.
     * @return the right master controller.
     */
    public HSTalon getRightMaster() {
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
     * Gets the pigeon.
     * @return the Pigen gyroscope.
     */
    public HSPigeon getPigeon() {
    	return pigeon;
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
    
    /**
     * Configures closed loop constants for the two master Talons. 
     * @param slotIndex the PID slot index [0,3] where the constants will be stored
     * @param leftConstants the set of constants for the left Talon
     * @param rightConstants the set of constants for the right Talon
     */
    public void configClosedLoopConstants(int slotIndex, Gains leftConstants, Gains rightConstants) {
    	//TODO simplify using reflections
    	getLeftMaster().config_kF(slotIndex, leftConstants.getkF());
    	getLeftMaster().config_kP(slotIndex, leftConstants.getkP());
    	getLeftMaster().config_kI(slotIndex, leftConstants.getkI());
    	getLeftMaster().config_kD(slotIndex, leftConstants.getkD());
    	getLeftMaster().config_kD(slotIndex, leftConstants.getIZone());
    	
    	getRightMaster().config_kF(slotIndex, rightConstants.getkF());
    	getRightMaster().config_kP(slotIndex, rightConstants.getkP());
    	getRightMaster().config_kI(slotIndex, rightConstants.getkI());
    	getRightMaster().config_kD(slotIndex, rightConstants.getkD());
    	getLeftMaster().config_kD(slotIndex, leftConstants.getIZone());
    }
}
