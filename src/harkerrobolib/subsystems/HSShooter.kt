package harkerrobolib.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.FeedbackDevice
import com.ctre.phoenix.motorcontrol.IMotorController
import com.ctre.phoenix.motorcontrol.NeutralMode

import edu.wpi.first.wpilibj.command.Subsystem
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import harkerrobolib.util.Gains
import harkerrobolib.wrappers.HSPigeon
import harkerrobolib.wrappers.HSTalon

/**
 * Represents a Shooter subsystem for the offseason shooter bot
 *
 * @author Finn Frankis
 * @author Jatin Kohli
 *
 * @param leftTalon the left Talon.
 * @param rightTalon the right Talon.
 *
 * @version 11/18/18
 */
abstract class HSDrivetrain
(
        val leftTalon: HSTalon,
        val rightTalon: HSTalon
        ) : Subsystem() {

    /**
     * Represents direction of intake.
     *
     * @param signum sign for output of left intake with given direction (must be negated for right)
     */
    enum class IntakeDirection(val signum : Int) {
        IN(1), OUT(-1);
        operator fun times(other : Double) = signum * other
    }
	
    /**
     * Applies a given operation to both Talons (see lambdas in Java for more clarification).
     * @param consumer the operation to be applied.
     */
    fun applyToTalons(lambda : (HSTalon) -> Unit) {
        lambda.invoke(leftTalon)
        lambda.invoke(rightTalon)
    }
	
    /**
     * Sets all active controllers to a given neutral mode (brake or coast).
     * @param nm the neutral mode to be applied.
     */
    fun setNeutralMode(nm: NeutralMode) {
        applyToTalons({ controller : HSTalon -> controller.setNeutralMode(nm) })

    }

    /**
     * Configures both Talons for current limiting.
     * @param peakLimit the peak limit (temporary, to account for current spikes).
     * @param peakTime the time for which the peak limit is active
     * @param continuousLimit the continuous limit, or the one which is active after the conclusion of the peak limit.
     * @param timeout the time after which a failed current limit command will stop being retried.
     */
    @JvmOverloads
    fun setCurrentLimit(peakLimit: Int, peakTime: Int, continuousLimit: Int, timeout: Int = -1) {
        val currentLimit = { talon : HSTalon ->
            val newTimeout = if (timeout == -1) talon.defaultTimeout else timeout
            talon.configPeakCurrentLimit(peakLimit, newTimeout)
            talon.configPeakCurrentDuration(peakTime, newTimeout)
            talon.configContinuousCurrentLimit(continuousLimit, newTimeout)
            Unit
        }
        applyToTalons(currentLimit)
    }

    /**
     * Resets both talons.
     */
    fun resetTalons() {
        applyToTalons({ talon -> talon.reset() })
    }


    /**
     * Configures the sensor for the Talons to point to a given sensor.
     * @param fd the type of sensor to which the Talon should use
     * @param pidLoop the loop index where this sensor should be placed [0,1]
     */
    fun configFeedbackSensor(fd: FeedbackDevice, pidLoop: Int) {
        leftTalon.configSelectedFeedbackSensor(fd,
                pidLoop)
        rightTalon.configSelectedFeedbackSensor(fd,
                pidLoop)
    }

    /**
     * Prints the current output percentage to the motors to SmartDashboard.
     */
    fun printMotorOutputPercentage() {
        SmartDashboard.putNumber("Left Talon Output Percentage", leftTalon.motorOutputPercent)
        SmartDashboard.putNumber("Right Talon Output Percentage", rightTalon.motorOutputPercent)
    }

    /**
     * Prints the closed loop error of the Talons in a given loop.
     * @param pidLoop the loop index [0,1]
     */
    fun printClosedLoopError(pidLoop: Int) {
        SmartDashboard.putNumber("Left Talon Closed Loop Error " + if (pidLoop == 0) "Primary" else "Auxiliary", leftTalon.getClosedLoopError(pidLoop).toDouble())
        SmartDashboard.putNumber("Right Talon Closed Loop Error " + if (pidLoop == 0) "Primary" else "Auxiliary", rightTalon.getClosedLoopError(pidLoop).toDouble())
    }

    /**
     * Prints the sensor positions of the Talons in a given loop.
     * @param pidLoop the loop index [0,1]
     */
    fun printSensorPositions(pidLoop: Int) {
        SmartDashboard.putNumber("Left Talon Position " + if (pidLoop == 0) "Primary" else "Auxiliary", leftTalon.getSelectedSensorPosition(pidLoop).toDouble())
        SmartDashboard.putNumber("Right Talon Position" + if (pidLoop == 0) "Primary" else "Auxiliary", rightTalon.getSelectedSensorPosition(pidLoop).toDouble())
    }

    /**
     * Determines whether the closed loop error for both sides is within a given value.
     * @param loopIndex the loop index, either primary or auxiliary [0,1]
     * @param allowableError the error tolerance to be checked
     * @return true if the absolute value of the error is within the value; false otherwise
     */
    fun isClosedLoopErrorWithin(loopIndex: Int, allowableError: Double): Boolean {
        return Math.abs(leftTalon.getClosedLoopError(loopIndex)) < allowableError && Math.abs(rightTalon.getClosedLoopError(loopIndex)) < allowableError
    }

    /**
     * Configures closed loop constants for the two Talons.
     * @param slotIndex the PID slot index [0,3] where the constants will be stored
     * @param leftConstants the set of constants for the left Talon
     * @param rightConstants the set of constants for the right Talon
     */
    fun configClosedLoopConstants(slotIndex: Int, leftConstants: Gains, rightConstants: Gains) {
        arrayOf("kF", "kP", "kI", "kD", "iZone").forEach {
            leftTalon.javaClass.getMethod("config_${it}", Int.javaClass, Double.javaClass).invoke(leftTalon, slotIndex,
                    leftConstants.javaClass.getField(it).get(leftTalon) as Double)
            rightTalon.javaClass.getMethod("config_${it}", Int.javaClass, Double.javaClass).invoke(rightTalon, slotIndex,
                    rightConstants.javaClass.getField(it).get(rightTalon) as Double)
        }
    }

    /**
     * Sets wheels to the same output and direction.
     *
     * @param output value from controller that sets output for left and right wheels to same value
     * @param direction sets both wheels to either IN or OUT direction
     */
    fun intakeOuttake(output: Double, direction: IntakeDirection) {
        if (direction == IntakeDirection.IN) {
            intakeOuttakeCube(output, output, IntakeDirection.IN, IntakeDirection.IN)
        } else {
            intakeOuttakeCube(output, output, IntakeDirection.OUT, IntakeDirection.OUT)
        }

    }

    /**
     * Sets each wheel to its specified output.
     *
     * @param leftOutput value that sets output for the left wheel
     * @param rightOutput value that sets output for the right wheel
     */
    fun intakeOuttake(leftOutput: Double, rightOutput: Double) {
        leftTalon.set(ControlMode.PercentOutput, leftOutput)
        rightTalon.set(ControlMode.PercentOutput, rightOutput)
    }

    /**
     * Sets wheels to their respective outputs and directions.
     *
     * @param leftOutput value from controller that sets left wheel output
     * @param rightOutput value from controller that sets right wheel output
     * @param leftDirection enum representing left wheel's direction
     * @param rightDirection enum representing right wheel's direction
     */
    fun intakeOuttake(leftOutput: Double, rightOutput: Double, leftDirection: IntakeDirection, rightDirection: IntakeDirection) {
        if (leftDirection == IntakeDirection.IN) {
            leftTalon[ControlMode.PercentOutput] = leftDirection * leftOutput
        } else {
            leftTalon[ControlMode.PercentOutput] = leftDirection * -leftOutput
        }
        if (rightDirection == IntakeDirection.IN) {
            rightTalon[ControlMode.PercentOutput] = rightDirection * -rightOutput
        } else {
            rightTalon[ControlMode.PercentOutput] = rightDirection * rightOutput
        }

    }
}