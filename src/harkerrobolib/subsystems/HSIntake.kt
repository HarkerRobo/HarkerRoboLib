package harkerrobolib.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.NeutralMode
import edu.wpi.first.wpilibj.command.Subsystem
import harkerrobolib.wrappers.HSTalon

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
abstract class HSIntake(val leftTalon : HSTalon, val rightTalon : HSTalon) : Subsystem() {

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
     * Sets the same neutral mode (brake or coast) for the two motor contollers.
     *
     * @param neutralMode the specified NeutralMode
     */
    fun setNeutralModes(neutralMode : NeutralMode) {
        leftTalon.setNeutralMode(neutralMode)
        rightTalon.setNeutralMode(neutralMode)
    }

    /**
     * Sets the same current limits to both talons.
     *
     * @param peakTime the amount of time peak current is the active limit
     * @param peakCurrent the amount of current during peak time
     * @param contCurrent the amount of current during cont time
     * @timeout the time after which a failed CAN command will stop being retried
     */
    fun setCurrentLimits(peakTime : Int, peakCurrent : Int, contCurrent : Int, timeout : Int = -1) {
        val applyCurrentLimit = { talon : HSTalon, peakLimit:Int, contLimit: Int ->
            val newTimeout : Int = if (timeout == -1) talon.defaultTimeout else timeout
            talon.configPeakCurrentDuration(peakTime, newTimeout)
            talon.configPeakCurrentLimit(peakLimit, newTimeout)
            talon.configContinuousCurrentLimit(contLimit, newTimeout)
            talon.enableCurrentLimit(true)}

        applyCurrentLimit.invoke(leftTalon, peakCurrent, contCurrent)
        applyCurrentLimit.invoke(rightTalon, peakCurrent, contCurrent)
    }

    /**
     * Sets wheels to the same output and direction.
     *
     * @param output value from controller that sets output for left and right wheels to same value
     * @param direction sets both wheels to either IN or OUT direction
     */
    fun intakeOuttakeCube(output: Double, direction: IntakeDirection) {
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
    fun intakeOuttakeCube(leftOutput: Double, rightOutput: Double) {
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
    fun intakeOuttakeCube(leftOutput: Double, rightOutput: Double, leftDirection: IntakeDirection, rightDirection: IntakeDirection) {
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