package harkerrobolib.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.DemandType
import com.ctre.phoenix.motorcontrol.can.TalonSRX
import edu.wpi.first.wpilibj.Talon
import edu.wpi.first.wpilibj.command.Subsystem
import harkerrobolib.wrappers.TalonSRXWrapper
import jdk.nashorn.internal.objects.Global

/**
 * Represents a general Arm subsystem with a single motor controller.
 *
 * @author Finn Frankis
 * @author Angela Jia
 * @version 10/31/18
 */
abstract class ArmSubsystem(val talon : TalonSRXWrapper, val feedForwardGrav : Double = 0.0 ) : Subsystem(){

    enum class ArmDirection {
        UP, DOWN
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
        val modifiedOutput = output//MathUtil.constrain(output, MAX_MOTION_SPEED, MIN_MOTION_SPEED)
        if (direction == ArmDirection.UP) {
            armMotionPercentOutput(modifiedOutput)
        } else {
            armMotionPercentOutput(-modifiedOutput)
        }
    }

    fun armMotionPercentOutput (output: Double) {
        talon.set(ControlMode.PercentOutput, output, DemandType.ArbitraryFeedForward, feedForwardGrav)
    }

    fun getTalonCurrent() = talon.outputCurrent
}