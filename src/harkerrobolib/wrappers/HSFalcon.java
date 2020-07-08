package harkerrobolib.wrappers;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.RemoteFeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import harkerrobolib.util.Constants;

/**
 * Wraps a standard TalonSRX.
 * 
 * @author Chirag Kaushik
 * @author Ada Praun-Petrovic
 */
public class HSFalcon extends TalonFX implements HSMotorController {
    /**
     * Constructs a TalonSRXWrapper with the default timeout
     * {{@link Constants#DEFAULT_TIMEOUT}.
     * 
     * @param deviceNumber The CAN device ID of the Talon.
     */
    public HSFalcon(final int deviceNumber) {
        super(deviceNumber);
    }

    @Override
    public ErrorCode configSelectedFeedbackSensor(RemoteFeedbackDevice feedbackDevice, int pidIdx) {
        return super.configSelectedFeedbackSensor(feedbackDevice, pidIdx, Constants.DEFAULT_TIMEOUT);
    }

    @Override
    public ErrorCode configSelectedFeedbackSensor(FeedbackDevice feedbackDevice, int pidIdx) {
        return super.configSelectedFeedbackSensor(feedbackDevice, pidIdx, Constants.DEFAULT_TIMEOUT);
    }
}
