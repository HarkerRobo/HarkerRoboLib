package harkerrobolib.wrappers;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.ParamEnum;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;
import com.ctre.phoenix.sensors.PigeonIMU_StatusFrame;

import harkerrobolib.util.Constants;

/**
 * Represents a basic PigeonIMU with additional useful features.
 * @author Finn Frankis 
 * @version 10/21/18
 */
public class HSPigeon extends PigeonIMU {

	private int timeout; 
	 
	public HSPigeon (int deviceNumber) {
		this(deviceNumber, Constants.DEFAULT_TIMEOUT);
	}
	
	public HSPigeon(int deviceNumber, int defaultTimeout) {
		super(deviceNumber);
		this.timeout = defaultTimeout;
	}
	
	public HSPigeon (TalonSRX talon) {
		this(talon, Constants.DEFAULT_TIMEOUT);
	}
	
	public HSPigeon (TalonSRX talon, int defaultTimeout) {
		super(talon);
		this.timeout = defaultTimeout;
	}
	
	/**
     * Gets the current yaw value of the pigeon.
     * @return the yaw
     */
    public double getYaw()
    {
        double[] ypr = new double[3];
        getYawPitchRoll(ypr);
        return ypr[0];
    }
    
    /**
     * Gets the current pitch value of the pigeon.
     * @return the pitch
     */
    public double getPitch()
    {
        double[] ypr = new double[3];
        getYawPitchRoll(ypr);
        return ypr[1];
    }
    
    /**
     * Gets the current roll value of the pigeon.
     * @return the roll
     */
    public double getRoll()
    {
        double[] ypr = new double[3];
        getYawPitchRoll(ypr);
        return ypr[2];
    }
    
    /**
     * Sets the pigeon yaw to a given value.
     * @param angle the angle value to which the pigeon should be set, in pigeon units 
     * where 1 rotation is 8192 units
     */
	@Override
    public ErrorCode setYaw(double angle)
    {
        return super.setYaw(angle * 64, timeout); // CTRE's error where replaced angle is off by a factor of 64
    }
    
    /**
     * Adds a given value to the pigeon yaw.
     * @param angle the angle value which should be added to the pigeon yaw value, in pigeon units 
     * where 1 rotation is 8192 units
     */
    public ErrorCode addYaw (double angle)
    {
        return super.addYaw(angle * 64, timeout); // CTRE's error where replaced angle is off by a factor of 64
    }
 
    /**
     * Zeros the pigeon.
     */
    public void zero()
    {
        setYaw(0);
        setAccumZAngle(0);
    }

	public ErrorCode setYawToCompass() {
		// TODO Auto-generated method stub
		return super.setYawToCompass(timeout);
	}

	
	public ErrorCode setFusedHeading(double angleDeg) {
		// TODO Auto-generated method stub
		return super.setFusedHeading(angleDeg, timeout);
	}

	
	public ErrorCode addFusedHeading(double angleDeg) {
		// TODO Auto-generated method stub
		return super.addFusedHeading(angleDeg, timeout);
	}

	
	public ErrorCode setFusedHeadingToCompass() {
		// TODO Auto-generated method stub
		return super.setFusedHeadingToCompass(timeout);
	}

	
	public ErrorCode setAccumZAngle(double angleDeg) {
		// TODO Auto-generated method stub
		return super.setAccumZAngle(angleDeg, timeout);
	}

	
	public ErrorCode configTemperatureCompensationEnable(boolean bTempCompEnable) {
		// TODO Auto-generated method stub
		return super.configTemperatureCompensationEnable(bTempCompEnable, timeout);
	}

	
	public ErrorCode setCompassDeclination(double angleDegOffset) {
		// TODO Auto-generated method stub
		return super.setCompassDeclination(angleDegOffset, timeout);
	}

	
	public ErrorCode setCompassAngle(double angleDeg) {
		// TODO Auto-generated method stub
		return super.setCompassAngle(angleDeg, timeout);
	}

	
	public ErrorCode enterCalibrationMode(CalibrationMode calMode) {
		// TODO Auto-generated method stub
		return super.enterCalibrationMode(calMode, timeout);
	}

	
	public ErrorCode configSetCustomParam(int newValue, int paramIndex) {
		// TODO Auto-generated method stub
		return super.configSetCustomParam(newValue, paramIndex, timeout);
	}

	
	public int configGetCustomParam(int paramIndex, int timoutMs) {
		// TODO Auto-generated method stub
		return super.configGetCustomParam(paramIndex, timoutMs);
	}

	
	public ErrorCode configSetParameter(ParamEnum param, double value, int subValue, int ordinal) {
		// TODO Auto-generated method stub
		return super.configSetParameter(param, value, subValue, ordinal, timeout);
	}

	
	public ErrorCode configSetParameter(int param, double value, int subValue, int ordinal) {
		// TODO Auto-generated method stub
		return super.configSetParameter(param, value, subValue, ordinal, timeout);
	}

	
	public double configGetParameter(ParamEnum param, int ordinal) {
		// TODO Auto-generated method stub
		return super.configGetParameter(param, ordinal, timeout);
	}

	
	public double configGetParameter(int param, int ordinal) {
		// TODO Auto-generated method stub
		return super.configGetParameter(param, ordinal, timeout);
	}

	
	public ErrorCode setStatusFramePeriod(PigeonIMU_StatusFrame statusFrame, int periodMs) {
		// TODO Auto-generated method stub
		return super.setStatusFramePeriod(statusFrame, periodMs, timeout);
	}

	
	public ErrorCode setStatusFramePeriod(int statusFrame, int periodMs) {
		// TODO Auto-generated method stub
		return super.setStatusFramePeriod(statusFrame, periodMs, timeout);
	}

	
	public int getStatusFramePeriod(PigeonIMU_StatusFrame frame) {
		// TODO Auto-generated method stub
		return super.getStatusFramePeriod(frame, timeout);
	}

	
	public ErrorCode clearStickyFaults() {
		// TODO Auto-generated method stub
		return super.clearStickyFaults(timeout);
	}

	public void setDefaultTimeout (int newTimeout) {
		this.timeout = newTimeout;
	}
}