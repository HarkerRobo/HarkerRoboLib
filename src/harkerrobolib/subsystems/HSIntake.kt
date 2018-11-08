package harkerrobolib.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.NeutralMode
import com.ctre.phoenix.motorcontrol.can.TalonSRX
import edu.wpi.first.wpilibj.command.Subsystem
import harkerrobolib.wrappers.HSTalon

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

    private fun setNeutralModes(neutralMode : NeutralMode) {
        leftTalon.setNeutralMode(neutralMode)
        rightTalon.setNeutralMode(neutralMode)
    }

    private fun setCurrentLimits(peakTime : Int, peakCurrent : Int, contCurrent : Int, timeout : Int = -1) {
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
     * Sets wheels to the same output and direction
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