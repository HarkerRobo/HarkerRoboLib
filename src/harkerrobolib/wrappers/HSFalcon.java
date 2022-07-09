package harkerrobolib.wrappers;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.RemoteFeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.FilterConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.math.MathUtil;
import harkerrobolib.util.Constants;

/**
 * Wraps a standard TalonSRX.
 * 
 * @author Chirag Kaushik
 * @author Ada Praun-Petrovic
 */
public class HSFalcon extends WPI_TalonFX implements HSMotorController {
    /**
     * Constructs a TalonSRXWrapper with the default timeout
     * {{@link Constants#DEFAULT_TIMEOUT}.
     * 
     * @param deviceNumber The CAN device ID of the Talon.
     */
    public HSFalcon(final int deviceNumber) {
        super(deviceNumber);
    }

     /**
     * Constructs a TalonSRXWrapper with the default timeout
     * {{@link Constants#DEFAULT_TIMEOUT}.
     * 
     * @param deviceNumber The CAN device ID of the Talon.
     */
    public HSFalcon(final int deviceNumber, String busId) {
        super(deviceNumber, busId);
    }

    @Override
    public void setVoltage(double voltage) {
        super.setVoltage(MathUtil.clamp(voltage, -12.0, 12.0));
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
