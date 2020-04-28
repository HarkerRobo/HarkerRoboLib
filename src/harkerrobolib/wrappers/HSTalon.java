package harkerrobolib.wrappers;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import harkerrobolib.util.Constants;

/**
 * Wraps a standard TalonSRX.
 * 
 * @author Chirag Kaushik
 * @author Ada Praun-Petrovic
 */
public class HSTalon extends TalonSRX implements HSMotorController {
    /**
     * Constructs a TalonSRXWrapper with the default timeout {{@link Constants#DEFAULT_TIMEOUT}.
     * @param deviceNumber The CAN device ID of the Talon.
     */
    public HSTalon(int deviceNumber) {
    	super(deviceNumber);
    }
}