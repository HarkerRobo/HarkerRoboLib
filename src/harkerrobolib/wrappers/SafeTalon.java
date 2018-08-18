package harkerrobolib.wrappers;

import java.security.InvalidParameterException;

import com.ctre.phoenix.ErrorCode;

/**
 * A Talon class which better handles resetting and warning about incorrect parameters.
 * @author Finn Frankis
 * @version Aug 17, 2018
 */
public class SafeTalon extends TalonSRXWrapper {

    /**
     * Constructs a new SafeTalon.java.
     * @param id id the CAN ID to which this Talon corresponds
     */
    public SafeTalon (int id) {
        super(id);
    }

    @Override
    public ErrorCode config_IntegralZone (int slotIndex, int iZone, int timeout) {
        isSlotIndex(slotIndex);
        return super.config_IntegralZone(slotIndex, iZone, timeout);
    }

    @Override
    public ErrorCode config_kF (int slotIndex, double value, int timeout) {
        isSlotIndex(slotIndex);
        return super.config_kF(slotIndex, value, timeout);
    }

    @Override
    public ErrorCode config_kP (int slotIndex, double value, int timeout) {
        isSlotIndex(slotIndex);
        return super.config_kP(slotIndex, value, timeout);
    }

    @Override
    public ErrorCode config_kI (int slotIndex, double value, int timeout) {
        isSlotIndex(slotIndex);
        return super.config_kI(slotIndex, value, timeout);
    }

    @Override
    public ErrorCode config_kD (int slotIndex, double value, int timeout) {
        isSlotIndex(slotIndex);
        return super.config_kD(slotIndex, value, timeout);
    }

    @Override
    public ErrorCode configMaxIntegralAccumulator (int slotIndex, double value, int timeout) {
        isSlotIndex(slotIndex);
        return super.configMaxIntegralAccumulator(slotIndex, value, timeout);
    }

    @Override
    public ErrorCode configAllowableClosedloopError (int slotIndex, int value, int timeout) {
        isSlotIndex(slotIndex);
        return super.configAllowableClosedloopError(slotIndex, value, timeout);
    }

    @Override
    public void selectProfileSlot (int slotIndex, int loopIndex) {
        isSlotIndex(slotIndex);
        isLoopIndex(loopIndex);
        super.selectProfileSlot(slotIndex, loopIndex);
    }

    private boolean isWithin (double value, double minimum, double maximum) {
        return value >= minimum && value <= maximum;
    }

    /**
     * Checks if a given value is a valid PID slot index for a TalonSRX, throwing an exception if it is not.
     * @param value the value to be checked
     */
    private void isSlotIndex (double value) {
        if (!isWithin(value, Default.FIRST_PID_SLOT, Default.LAST_PID_SLOT))
            throw new InvalidParameterException(value + " is not a valid slot index [0, 3]");
    }
    
    /**
     * Checks if a given value is a valid PID loop index for a TalonSRX, throwing an exception if it is not.
     * @param value the value to be checked
     */
    private void isLoopIndex (double value) {
        if (!isWithin(value, Default.FIRST_PID_LOOP, Default.LAST_PID_LOOP))
            throw new InvalidParameterException(value + " is not a valid loop index [0, 1]");
    }

}
