package harkerrobolib.wrappers;

/**
 * Contains a series of useful mathematical functions.
 * @author Finn Frankis
 * @version 10/16/18
 */
public class MathUtil {
	
	/**
	 * Constrains one value into a given range.
	 * @param value the value to be constrained.
	 * @param minValue the minimum value this value can take on.
	 * @param maxValue the maximum value this value can take on.
	 * 
	 * @precondition minValue < maxValue
	 * @return the constrained value.
	 */
	public double constrain (double value, double minValue, double maxValue) {
		return Math.max(Math.min(value, maxValue), minValue);
	}
	
	/**
	 * Linearly maps a value currently within a given range to another range.
	 * @param value the value to be mapped.
	 * @param currentMin the current minimum possible value that value can take on.
	 * @param currentMax the current maximum possible value that value can take on.
	 * @param desiredMin the desired minimum possible value that value can take on.
	 * @param desiredMax the desired maximum possible value that value can take on.
	 * @return
	 */
	public double map (double value, double currentMin, double currentMax, double desiredMin, double desiredMax) {
		return (value - currentMin) * (desiredMax - desiredMin) / (currentMax - currentMin) + desiredMin;
	}
	
	/**
	 * Maps a joystick input value between [-1, 1] to one where any input value between [-deadband, deadband] is zero 
	 * and anything outside of that range is mapped linearly from [0,1].
	 * @param inputValue the measured input value.
	 * @param deadband the joystick's deadband.
	 * @return the mapped joystick input.
	 */
	public double mapJoystickOutput (double inputValue, double deadband) {
		if (Math.abs(inputValue) <= deadband) {return 0;}
		return inputValue > 0 ? map (inputValue, deadband, 1, 0, 1) : map(inputValue, -1, -deadband, -1, 0);
	}
}
