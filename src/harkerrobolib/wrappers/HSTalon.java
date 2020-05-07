package harkerrobolib.wrappers;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.RemoteFeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatorCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.AbsoluteSensorRange;
import com.ctre.phoenix.sensors.SensorInitializationStrategy;

import harkerrobolib.util.Constants;

/**
 * Wraps a standard TalonSRX.
 * 
 * @author Chirag Kaushik
 * @author Ada Praun-Petrovic
 */
public class HSTalon extends TalonSRX implements HSMotorController {
    /**
     * Constructs a TalonSRXWrapper with the default timeout
     * {{@link Constants#DEFAULT_TIMEOUT}.
     * 
     * @param deviceNumber The CAN device ID of the Talon.
     */
    public HSTalon(int deviceNumber) {
        super(deviceNumber);
    }

    @Override
    public ErrorCode configStatorCurrentLimit(StatorCurrentLimitConfiguration currLimitCfg, int timeoutMs) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ErrorCode configStatorCurrentLimit(StatorCurrentLimitConfiguration currLimitCfg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ErrorCode configGetSupplyCurrentLimit(SupplyCurrentLimitConfiguration currLimitConfigsToFill,
            int timeoutMs) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ErrorCode configGetSupplyCurrentLimit(SupplyCurrentLimitConfiguration currLimitConfigsToFill) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ErrorCode configGetStatorCurrentLimit(StatorCurrentLimitConfiguration currLimitConfigsToFill,
            int timeoutMs) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ErrorCode configGetStatorCurrentLimit(StatorCurrentLimitConfiguration currLimitConfigsToFill) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ErrorCode configIntegratedSensorAbsoluteRange(AbsoluteSensorRange absoluteSensorRange, int timeoutMs) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ErrorCode configIntegratedSensorAbsoluteRange(AbsoluteSensorRange absoluteSensorRange) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ErrorCode configIntegratedSensorOffset(double offsetDegrees, int timeoutMs) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ErrorCode configIntegratedSensorOffset(double offsetDegrees) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ErrorCode configIntegratedSensorInitializationStrategy(SensorInitializationStrategy initializationStrategy,
            int timeoutMs) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ErrorCode configIntegratedSensorInitializationStrategy(SensorInitializationStrategy initializationStrategy) {
        // TODO Auto-generated method stub
        return null;
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