package harkerrobolib.subsystems

import java.util.function.Consumer

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.FeedbackDevice
import com.ctre.phoenix.motorcontrol.IMotorController
import com.ctre.phoenix.motorcontrol.NeutralMode
import com.ctre.phoenix.motorcontrol.can.TalonSRX
import com.ctre.phoenix.motorcontrol.can.VictorSPX
import com.ctre.phoenix.sensors.PigeonIMU

import edu.wpi.first.wpilibj.Talon
import edu.wpi.first.wpilibj.command.Subsystem
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import harkerrobolib.util.Gains
import harkerrobolib.wrappers.PigeonWrapper
import harkerrobolib.wrappers.TalonSRXWrapper

/**
 * Represents a general Drivetrain subsystem with two master motor controllers and two optional follower controllers.
 * @author Finn Frankis
 * @version Aug 17, 2018
 */
abstract class DrivetrainSubsystem
/**
 * Constructs a new Drivetrain subsystem with two master controllers, two follower controllers, and a Pigeon.
 *
 * @param leftMaster the left master Talon.
 * @param rightMaster the right master Talon.
 * @param leftFollower the left follower motor controller (Talon or Victor).
 * @param rightFollower the right folllower motor controller (Talon or Victor).
 * @param pigeon the Pigeon gyroscope.
 */
(
        /**
         * Gets the left master controller.
         * @return the left master controller.
         */
        val leftMaster: TalonSRXWrapper,
        /**
         * Gets the right master controller.
         * @return the right master controller.
         */
        val rightMaster: TalonSRXWrapper,
        /**
         * Gets the left follower controller.
         * @return the left follower controller.
         */
        val leftFollower: IMotorController?,
        /**
         * Gets the right follower controller.
         * @return the right follower controller.
         */
        val rightFollower: IMotorController?,
        /**
         * Gets the pigeon.
         * @return the Pigen gyroscope.
         */
        val pigeon: PigeonWrapper?) : Subsystem() {

    private var hasFollowers: Boolean = false
    private var hasPigeon: Boolean = false

    init {
        followMasters()
        hasFollowers = true
        hasPigeon = true
    }

    /**
     * Constructs a new Drivetrain subsystem with two master controllers and two follower controllers.
     *
     * @param leftMaster the left master Talon.
     * @param rightMaster the right master Talon.
     * @param leftFollower the left follower motor controller (Talon or Victor).
     * @param rightFollower the right folllower motor controller (Talon or Victor).
     */
    constructor(leftMaster: TalonSRXWrapper, rightMaster: TalonSRXWrapper, leftFollower: IMotorController?,
                rightFollower: IMotorController?) : this(leftMaster, rightMaster, leftFollower, rightFollower, null) {
        hasPigeon = false
    }

    /**
     * Constructs a new Drivetrain subsystem with two master controllers.
     *
     * @param leftMaster the left master Talon.
     * @param rightMaster the right master Talon.
     */
    constructor(leftMaster: TalonSRXWrapper, rightMaster: TalonSRXWrapper) : this(leftMaster, rightMaster, null, null) {
        hasFollowers = false
    }

    /**
     * Constructs a new Drivetrain subsystem without follower controllers but with a Pigeon.
     * @param leftMaster the left master Talon.
     * @param rightMaster the right master Talon.
     * @param pigeon the Pigeon gyroscope.
     */
    constructor(leftMaster: TalonSRXWrapper, rightMaster: TalonSRXWrapper, pigeon: PigeonWrapper) : this(leftMaster, rightMaster, null, null, pigeon) {
        hasFollowers = false
    }

    /**
     * Applies a given operation to both master Talons (see lambdas in Java for more clarification).
     * @param consumer the operation to be applied.
     */
    fun applyToMasters(lambda : (TalonSRXWrapper) -> Unit) {
        lambda.invoke(leftMaster)
        lambda.invoke(rightMaster)
    }

    /**
     * Applies a given operation to both master and follower motor controllers.
     * @param consumer the operation to be applied.
     */
    fun applyToAll(lambda : (IMotorController) -> Unit) {
        lambda.invoke(leftMaster)
        lambda.invoke(rightMaster)

        if (hasFollowers) {
            lambda.invoke(leftFollower!!)
            lambda.invoke(rightFollower!!)
        }
    }

    /**
     * Tells the follower controllers to follow the master controllers.
     */
    fun followMasters() {
        if (hasFollowers) {
            leftFollower!!.follow(leftMaster)
            rightFollower!!.follow(rightMaster)
        }
    }

    /**
     * Sets all active controllers to a given neutral mode (brake or coast).
     * @param nm the neutral mode to be applied.
     */
    fun setNeutralMode(nm: NeutralMode) {
        applyToAll({ controller : IMotorController -> controller.setNeutralMode(nm) })

    }

    /**
     * Configures both masters for current limiting.
     * @param peakLimit the peak limit (temporary, to account for current spikes).
     * @param peakTime the time for which the peak limit is active
     * @param continuousLimit the continuous limit, or the one which is active after the conclusion of the peak limit.
     * @param timeout the time after which a failed current limit command will stop being retried.
     */
    @JvmOverloads
    fun setCurrentLimit(peakLimit: Int, peakTime: Int, continuousLimit: Int, timeout: Int = -1) {
        val currentLimit = { talon : TalonSRXWrapper ->
            val newTimeout = if (timeout == -1) talon.defaultTimeout else timeout
            talon.configPeakCurrentLimit(peakLimit, newTimeout)
            talon.configPeakCurrentDuration(peakTime, newTimeout)
            talon.configContinuousCurrentLimit(continuousLimit, newTimeout)
            Unit
        }
        applyToMasters(currentLimit)
    }

    /**
     * Sets both masters to a given output.
     * @param mode the mode to which both Talons should be set.
     * @param outputValue the value of the output tied to the given control mode.
     */
    fun setBoth(mode: ControlMode, outputValue: Double) {
        applyToMasters({ talon -> talon.set(mode, outputValue) })
    }

    /**
     * Resets both masters.
     */
    fun resetMasters() {
        applyToMasters({ talon -> talon.reset() })
    }


    /**
     * Configures both Talons to point to a given sensor.
     * @param fd the type of sensor to which the Talon should use
     * @param pidLoop the loop index where this sensor should be placed [0,1]
     */
    fun configBothFeedbackSensors(fd: FeedbackDevice, pidLoop: Int) {
        leftMaster.configSelectedFeedbackSensor(fd,
                pidLoop)
        rightMaster.configSelectedFeedbackSensor(fd,
                pidLoop)
    }

    /**
     * Prints the current output percentage to the motors to SmartDashboard.
     */
    fun printMotorOutputPercentage() {
        SmartDashboard.putNumber("Left Talon Output Percentage", leftMaster.motorOutputPercent)
        SmartDashboard.putNumber("Right Talon Output Percentage", rightMaster.motorOutputPercent)
    }

    /**
     * Prints the closed loop error of the Talons in a given loop.
     * @param pidLoop the loop index [0,1]
     */
    fun printClosedLoopError(pidLoop: Int) {
        SmartDashboard.putNumber("Left Talon Closed Loop Error " + if (pidLoop == 0) "Primary" else "Auxiliary", leftMaster.getClosedLoopError(pidLoop).toDouble())
        SmartDashboard.putNumber("Right Talon Closed Loop Error " + if (pidLoop == 0) "Primary" else "Auxiliary", rightMaster.getClosedLoopError(pidLoop).toDouble())
    }

    /**
     * Prints the sensor positions of the Talons in a given loop.
     * @param pidLoop the loop index [0,1]
     */
    fun printSensorPositions(pidLoop: Int) {
        SmartDashboard.putNumber("Left Talon Position " + if (pidLoop == 0) "Primary" else "Auxiliary", leftMaster.getSelectedSensorPosition(pidLoop).toDouble())
        SmartDashboard.putNumber("Right Talon Position" + if (pidLoop == 0) "Primary" else "Auxiliary", rightMaster.getSelectedSensorPosition(pidLoop).toDouble())
    }

    /**
     * Determines whether the closed loop error for both sides is within a given value.
     * @param loopIndex the loop index, either primary or auxiliary [0,1]
     * @param allowableError the error tolerance to be checked
     * @return true if the absolute value of the error is within the value; false otherwise
     */
    fun isClosedLoopErrorWithin(loopIndex: Int, allowableError: Double): Boolean {
        return Math.abs(leftMaster.getClosedLoopError(loopIndex)) < allowableError && Math.abs(rightMaster.getClosedLoopError(loopIndex)) < allowableError
    }

    /**
     * Configures closed loop constants for the two master Talons.
     * @param slotIndex the PID slot index [0,3] where the constants will be stored
     * @param leftConstants the set of constants for the left Talon
     * @param rightConstants the set of constants for the right Talon
     */
    fun configClosedLoopConstants(slotIndex: Int, leftConstants: Gains, rightConstants: Gains) {
        //TODO simplify using reflections
        leftMaster.config_kF(slotIndex, leftConstants.getkF())
        leftMaster.config_kP(slotIndex, leftConstants.getkP())
        leftMaster.config_kI(slotIndex, leftConstants.getkI())
        leftMaster.config_kD(slotIndex, leftConstants.getkD())
        leftMaster.config_kD(slotIndex, leftConstants.iZone)

        rightMaster.config_kF(slotIndex, rightConstants.getkF())
        rightMaster.config_kP(slotIndex, rightConstants.getkP())
        rightMaster.config_kI(slotIndex, rightConstants.getkI())
        rightMaster.config_kD(slotIndex, rightConstants.getkD())
        leftMaster.config_kD(slotIndex, leftConstants.iZone)
    }
}
/**
 * Configures both masters for current limiting, where each one's default timeout is used.
 * @param peakLimit the peak limit (temporary, to account for current spikes).
 * @param peakTime the time for which the peak limit is active
 * @param continuousLimit the continuous limit, or the one which is active after the conclusion of the peak limit.
 */
