package harkerrobolib.wrappers;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.RemoteFeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

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
    public HSFalcon(int deviceNumber) {
        super(deviceNumber);
    }

    @Override
    public ErrorCode configPeakCurrentLimit(int amps, int timeoutMs) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ErrorCode configPeakCurrentLimit(int amps) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ErrorCode configPeakCurrentDuration(int milliseconds, int timeoutMs) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ErrorCode configPeakCurrentDuration(int milliseconds) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ErrorCode configContinuousCurrentLimit(int amps, int timeoutMs) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ErrorCode configContinuousCurrentLimit(int amps) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void enableCurrentLimit(boolean enable) {
        // TODO Auto-generated method stub

    }

    @Override
    public void BaseMotorController(int arbId, String model) {
        // TODO Auto-generated method stub

    }

	@Override
	public ErrorCode configSelectedFeedbackSensor(RemoteFeedbackDevice feedbackDevice, int pidIdx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ErrorCode configSelectedFeedbackSensor(FeedbackDevice feedbackDevice, int pidIdx) {
		// TODO Auto-generated method stub
		return null;
	}
}