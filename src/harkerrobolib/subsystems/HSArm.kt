package harkerrobolib.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.DemandType
import edu.wpi.first.wpilibj.command.Subsystem
import harkerrobolib.wrappers.HSTalon

/**
 * Represents a general Arm subsystem with a single motor controller.
 *
 * @author Finn Frankis
 * @author Angela Jia
 * @version 10/31/18
 */
abstract class HSArm(val talon : HSTalon, val feedForwardGrav : Double = 0.0 ) : Subsystem(){

    enum class ArmDirection (val sign : Int) {
        UP(1), DOWN(-1);

        operator fun times (other : ArmDirection) =  sign * other.sign
        operator fun times (other : Double) = sign * other
    }

    fun setCurrentLimits( peakTime : Int, peakCurrent : Int, contCurrent : Int, timeout : Int = talon.defaultTimeout ) {
        with (talon) {
            configPeakCurrentDuration(peakTime, timeout)
            configPeakCurrentLimit(peakCurrent, timeout)
            configContinuousCurrentLimit(contCurrent, timeout)
            enableCurrentLimit(true)
        }
    }

    fun armMotionPercentOutput(output: Double, direction: ArmDirection) {
        armMotionPercentOutput(direction * output)
    }

    fun armMotionPercentOutput (output: Double) {
        talon.set(ControlMode.PercentOutput, output, DemandType.ArbitraryFeedForward, feedForwardGrav)
    }

    fun getTalonCurrent() = talon.outputCurrent
}
