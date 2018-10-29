package harkerrobolib.wrappers

import java.security.InvalidParameterException

import com.ctre.phoenix.ErrorCode
import com.ctre.phoenix.motorcontrol.FeedbackDevice

/**
 * A Talon class which better handles resetting and warning about incorrect parameters.
 * @author Finn Frankis
 * @version Aug 17, 2018
 */
class SafeTalon
/**
 * Constructs a new SafeTalon.java.
 * @param id id the CAN ID to which this Talon corresponds
 */
(id: Int) : TalonSRXWrapper(id) {

    override fun config_IntegralZone(slotIndex: Int, iZone: Int, timeout: Int): ErrorCode {
        isSlotIndex(slotIndex.toDouble())
        return super.config_IntegralZone(slotIndex, iZone, timeout)
    }

    override fun config_kF(slotIndex: Int, value: Double, timeout: Int): ErrorCode {
        isSlotIndex(slotIndex.toDouble())
        return super.config_kF(slotIndex, value, timeout)
    }

    override fun config_kP(slotIndex: Int, value: Double, timeout: Int): ErrorCode {
        isSlotIndex(slotIndex.toDouble())
        return super.config_kP(slotIndex, value, timeout)
    }

    override fun config_kI(slotIndex: Int, value: Double, timeout: Int): ErrorCode {
        isSlotIndex(slotIndex.toDouble())
        return super.config_kI(slotIndex, value, timeout)
    }

    override fun config_kD(slotIndex: Int, value: Double, timeout: Int): ErrorCode {
        isSlotIndex(slotIndex.toDouble())
        return super.config_kD(slotIndex, value, timeout)
    }

    override fun configMaxIntegralAccumulator(slotIndex: Int, value: Double, timeout: Int): ErrorCode {
        isSlotIndex(slotIndex.toDouble())
        return super.configMaxIntegralAccumulator(slotIndex, value, timeout)
    }

    override fun configAllowableClosedloopError(slotIndex: Int, value: Int, timeout: Int): ErrorCode {
        isSlotIndex(slotIndex.toDouble())
        return super.configAllowableClosedloopError(slotIndex, value, timeout)
    }

    override fun getSelectedSensorPosition(loopIndex: Int): Int {
        isLoopIndex(loopIndex.toDouble())
        return super.getSelectedSensorPosition(loopIndex)
    }

    override fun setSelectedSensorPosition(sensorPos: Int, loopIndex: Int, timeout: Int): ErrorCode {
        isLoopIndex(loopIndex.toDouble())
        return super.setSelectedSensorPosition(sensorPos, loopIndex, timeout)
    }

    override fun configSelectedFeedbackSensor(device: FeedbackDevice, loopIndex: Int, timeout: Int): ErrorCode {
        isLoopIndex(loopIndex.toDouble())
        return super.configSelectedFeedbackSensor(device, loopIndex, timeout)
    }

    override fun selectProfileSlot(slotIndex: Int, loopIndex: Int) {
        isSlotIndex(slotIndex.toDouble())
        isLoopIndex(loopIndex.toDouble())
        super.selectProfileSlot(slotIndex, loopIndex)
    }

    private fun isWithin(value: Double, minimum: Double, maximum: Double): Boolean {
        return value >= minimum && value <= maximum
    }

    /**
     * Checks if a given value is a valid PID slot index for a TalonSRX, throwing an exception if it is not.
     * @param value the value to be checked
     */
    private fun isSlotIndex(value: Double) {
        if (!isWithin(value, TalonSRXWrapper.Default.FIRST_PID_SLOT.toDouble(), TalonSRXWrapper.Default.LAST_PID_SLOT.toDouble()))
            throw InvalidParameterException(value.toString() + " is not a valid slot index [0, 3]")
    }

    /**
     * Checks if a given value is a valid PID loop index for a TalonSRX, throwing an exception if it is not.
     * @param value the value to be checked
     */
    private fun isLoopIndex(value: Double) {
        if (!isWithin(value, TalonSRXWrapper.Default.FIRST_PID_LOOP.toDouble(), TalonSRXWrapper.Default.LAST_PID_LOOP.toDouble()))
            throw InvalidParameterException(value.toString() + " is not a valid loop index [0, 1]")
    }

}
