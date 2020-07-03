package harkerrobolib.subsystems;

import java.util.function.Consumer;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.IMotorController;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import harkerrobolib.util.Conversions;
import harkerrobolib.util.Gains;
import harkerrobolib.wrappers.HSMotorController;
import harkerrobolib.wrappers.HSPigeon;
import harkerrobolib.wrappers.HSTalon;

/**
 * Represents a general Drivetrain subsystem with two master motor controllers and two optional follower controllers.
 *
 * @author Finn Frankis
 * @version Aug 17, 2018
 */
public abstract class HSDiffDrivetrain<Motor extends HSMotorController> extends SubsystemBase {
    private Motor leftMaster;
    private Motor rightMaster;
    private IMotorController leftFollower;
    private IMotorController rightFollower;
    
    private HSPigeon pigeon;
    
    private boolean hasFollowers;
    private boolean hasPigeon;

    public static double WHEEL_DIAMETER;

    /**
     * Constructs a new Drivetrain subsystem with two master controllers, two follower controllers, and a Pigeon.
     * 
     * @param leftMaster the left master Talon.
     * @param rightMaster the right master Talon.
     * @param leftFollower the left follower motor controller (Talon or Victor).
     * @param rightFollower the right folllower motor controller (Talon or Victor).
     * @param pigeon the Pigeon gyroscope.
     */
    public HSDiffDrivetrain(Motor leftMaster, Motor rightMaster, IMotorController leftFollower,
            IMotorController rightFollower, HSPigeon pigeon, double wheelDiameter) {
        this.leftMaster = leftMaster;
        this.rightMaster = rightMaster;
        this.leftFollower = leftFollower;
        this.rightFollower = rightFollower;
        this.pigeon = pigeon;
        
        hasFollowers = true;
        hasPigeon = true;

        WHEEL_DIAMETER = wheelDiameter;
        Conversions.setWheelDiameter(WHEEL_DIAMETER);

        followMasters();
    }
    
    /**
     * Constructs a new Drivetrain subsystem with two master controllers and two follower controllers.
     * 
     * @param leftMaster the left master Talon.
     * @param rightMaster the right master Talon.
     * @param leftFollower the left follower motor controller (Talon or Victor).
     * @param rightFollower the right folllower motor controller (Talon or Victor).
     */
    public HSDiffDrivetrain (Motor leftMaster, Motor rightMaster, IMotorController leftFollower,
            IMotorController rightFollower, double wheelDiameter) {
        this(leftMaster, rightMaster, leftFollower, rightFollower, null, wheelDiameter);
        hasPigeon = false;
    }

    /**
     * Constructs a new Drivetrain subsystem with two master controllers.
     * 
     * @param leftMaster the left master Talon.
     * @param rightMaster the right master Talon.
     */
    public HSDiffDrivetrain (Motor leftMaster, Motor rightMaster, double wheelDiameter) {
        this(leftMaster, rightMaster, null, null, wheelDiameter);
        hasFollowers = false;
    }
    
    /**
     * Constructs a new Drivetrain subsystem without follower controllers but with a Pigeon.
     * @param leftMaster the left master Talon.
     * @param rightMaster the right master Talon.
     * @param pigeon the Pigeon gyroscope.
     */
    public HSDiffDrivetrain (Motor leftMaster, Motor rightMaster, HSPigeon pigeon, double wheelDiameter) {
        this(leftMaster, rightMaster, null, null, pigeon, wheelDiameter);
        hasFollowers = false;
    }

    /**
     * Applies a given operation to both master Talons (see lambdas in Java for more clarification).
     * @param consumer the operation to be applied.
     */
    public void applyToMasters (Consumer<Motor> consumer) {
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
     */
    public void setCurrentLimit(int peakLimit, int peakTime, int continuousLimit) {
        Consumer<Motor> currentLimit = (talon) -> {
            talon.configPeakCurrentLimit(peakLimit);
            talon.configPeakCurrentDuration(peakTime);
            talon.configContinuousCurrentLimit(continuousLimit);
            talon.enableCurrentLimit(true);
        };
        applyToMasters(currentLimit);
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
        applyToMasters ((talon) -> {talon.configFactoryDefault();});
    }
    
    /**
     * Inverts all the talons on the drivetrain (including followers).
     * @param leftMasterInverted whether to invert the left master
     * @param rightMasterInverted whether to invert the right master
     * @param leftFollowerInverted whether to invert the left follower
     * @param rightFollowerInverted whether to invert the right follower
     */
    public void invertTalons(boolean leftMasterInverted, boolean rightMasterInverted, 
            boolean leftFollowerInverted, boolean rightFollowerInverted) {
        invertTalons(leftMasterInverted, rightMasterInverted);
        if(hasFollowers) {
            leftFollower.setInverted(leftFollowerInverted);
            rightFollower.setInverted(rightFollowerInverted);
        }
    }
    
    /**
     * Inverts only the master talons on the drivetrain.
     * @param leftMasterInverted whether to invert the left master
     * @param rightMasterInverted whether to invert the right master
     */
    public void invertTalons(boolean leftMasterInverted, boolean rightMasterInverted) {
        leftMaster.setInverted(leftMasterInverted);
        rightMaster.setInverted(rightMasterInverted);
    }
    /**
     * Gets the left master controller.
     * @return the left master controller.
     */
    public Motor getLeftMaster() {
        return leftMaster;
    }
    
    /**
     * Gets the right master controller.
     * @return the right master controller.
     */
    public Motor getRightMaster() {
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
        SmartDashboard.putNumber("Left Talon Position " + (pidLoop == 0 ? "Primary" : "Auxiliary"), 
                getLeftMaster().getSelectedSensorPosition(pidLoop));
        SmartDashboard.putNumber("Right Talon Position" + (pidLoop == 0 ? "Primary" : "Auxiliary"), 
                getRightMaster().getSelectedSensorPosition(pidLoop));
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
    // public void configClosedLoopConstants(int slotIndex, Gains leftConstants, Gains rightConstants) {
    //     leftMaster.configClosedLoopConstants(slotIndex, leftConstants);
    //     rightMaster.configClosedLoopConstants(slotIndex, rightConstants);
    // }

    /**
     * Drives the robot with a given percent output.
     * 
     * @param speed the speed at which the robot should drive
     * @param turn the amount by which the robot should turn (negative for left, positive for right)
     */
    public void arcadeDrivePercentOutput (double speed, double turn) {
        double divisor = Math.max(1, Math.max(Math.abs(speed + Math.pow(turn, 2)), Math.abs(speed - Math.pow(turn, 2))));
        double leftOutputBase = speed + turn * Math.abs(turn);
        leftMaster.set(ControlMode.PercentOutput, leftOutputBase / divisor);

        double rightOutputBase = speed - turn * Math.abs(turn);
        rightMaster.set(ControlMode.PercentOutput,
                rightOutputBase/divisor);
    }

    /**
     * Drives the robot with a given velocity.
     * 
     * @param speed the speed at which the robot should drive (in ticks/100ms)
     * @param turn the amount by which the robot should turn (negative for left, positive for right) (in ticks/100ms)
     */
    public void arcadeDriveVelocity (double speed, double turn) {
        double divisor = Math.max(1, Math.max(Math.abs(speed + Math.pow(turn, 2)), Math.abs(speed - Math.pow(turn, 2))));
        double leftOutputBase = speed + turn * Math.abs(turn);
        leftMaster.set(ControlMode.Velocity, leftOutputBase / divisor);

        double rightOutputBase = speed - turn * Math.abs(turn);
        rightMaster.set(ControlMode.Velocity,
                rightOutputBase/divisor);
    }
}
