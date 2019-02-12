package harkerrobolib.util;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

/**
 * Contains commonly used methods applicable for pneumatics.
 * 
 * @author Angela Jia
 * @author Finn Frankis
 * @since 1/31/19
 */
public class Pneumatics {

    /**
    * Toggles the solenoid value based on the given value.
    * 
    * @param value DoubleSolenoid value to be toggled (either kReverse or kForward)
    *
    * @return the value that is not the given value
    */
    public static DoubleSolenoid.Value switchSolenoidValue(DoubleSolenoid.Value value) {
        return value == Value.kForward ? Value.kReverse : Value.kForward;
    }
}