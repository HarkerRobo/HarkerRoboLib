package harkerrobolib.wrappers;

import harkerrobolib.wrappers.HSGamepad;

/**
 * Represents a standard Xbox controller.
 * @author Finn Frankis
 * @version 10/16/18
 */
public class XboxGamepad extends HSGamepad {
	public static final int A = 1;
    public static final int B = 2;
    public static final int X = 3;
    public static final int Y = 4;
    public static final int SELECT = 7;
    public static final int START = 8;

    public static final int STICK_LEFT = 9;
    public static final int STICK_RIGHT = 10;

    public static final int BUMPER_LEFT = 5;
    public static final int BUMPER_RIGHT = 6;
    
    public static final int LEFT_X = 0;
    public static final int LEFT_Y = 1;
    public static final int RIGHT_X = 4;
    public static final int RIGHT_Y = 5;
    public static final int LEFT_TRIGGER = 2;
    public static final int RIGHT_TRIGGER = 3;
    
	public XboxGamepad(int port) {
		super(port, 
				A, B, X, Y, 
				START, SELECT, 
				STICK_LEFT, STICK_RIGHT, 
				BUMPER_LEFT, BUMPER_RIGHT,
				LEFT_X, LEFT_Y, RIGHT_X, RIGHT_Y);
	}
	
    public double getRightTrigger() {
    	return getRawAxis(RIGHT_TRIGGER);
    }
    
    public double getLeftTrigger() {
    	return getRawAxis(LEFT_TRIGGER);
    }
}
