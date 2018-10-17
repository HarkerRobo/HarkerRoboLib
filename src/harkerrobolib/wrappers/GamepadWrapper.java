package harkerrobolib.wrappers;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import edu.wpi.first.wpilibj.Joystick;

/**
 * A Joystick wrapper for gamepads that include more accurate/useful names for the analogue stick axes
 * @author neymikajain
 * @author atierno
 */
public class GamepadWrapper extends Joystick {
	/**
	 * Represents the various types of gamepads.
	 * @author Finn Frankis
	 * @version 10/16/18
	 */
	public enum GamepadSetting {
		XBOX, LOGITECH;
	}
	public static final int SETTING_XBOX = 0;
	public static final int SETTING_LOGITECH = 1;
	
	/************************************************
	 * XBOX SETTINGS                                *
	 ************************************************/
    public static final int XBOX_A_PORT = 1;
    public static final int XBOX_B_PORT = 2;
    public static final int XBOX_X_PORT = 3;
    public static final int XBOX_Y_PORT = 4;
    public static final int XBOX_SELECT_PORT = 7;
    public static final int XBOX_START_PORT = 8;

    public static final int XBOX_STICK_LEFT_PORT = 9;
    public static final int XBOX_STICK_RIGHT_PORT = 10;

    public static final int XBOX_BUMPER_LEFT_PORT = 5;
    public static final int XBOX_BUMPER_RIGHT_PORT = 6;
    
    public static final int XBOX_AXIS_LEFT_X = 0;
    public static final int XBOX_AXIS_LEFT_Y = 1;
    public static final int XBOX_AXIS_RIGHT_X = 4;
    public static final int XBOX_AXIS_RIGHT_Y = 5;
    public static final int XBOX_AXIS_TRIGGER_LEFT = 2;
    public static final int XBOX_AXIS_TRIGGER_RIGHT = 3;
    public static final double XBOX_TRIGGER_DEADBAND = 0.1;
    
    /************************************************
	 * LOGITECH SETTINGS                            *
	 ************************************************/
    public static final int LOGITECH_A_PORT = 2;
    public static final int LOGITECH_B_PORT = 3;
    public static final int LOGITECH_X_PORT = 1;
    public static final int LOGITECH_Y_PORT = 4;
    public static final int LOGITECH_SELECT_PORT = 9;
    public static final int LOGITECH_START_PORT = 10;

    public static final int LOGITECH_STICK_LEFT_PORT = 11;
    public static final int LOGITECH_STICK_RIGHT_PORT = 12;

    public static final int LOGITECH_BUMPER_LEFT_PORT = 5;
    public static final int LOGITECH_BUMPER_RIGHT_PORT = 6;
    
    public static final int LOGITECH_AXIS_LEFT_X = 0;
    public static final int LOGITECH_AXIS_LEFT_Y = 1;
    public static final int LOGITECH_AXIS_RIGHT_X = 2;
    public static final int LOGITECH_AXIS_RIGHT_Y = 3;
    public static final int LOGITECH_TRIGGER_LEFT = 7;
    public static final int LOGITECH_TRIGGER_RIGHT = 8;
    
    private final JoystickButtonWrapper buttonA;
    private final JoystickButtonWrapper buttonB;
    private final JoystickButtonWrapper buttonX;
    private final JoystickButtonWrapper buttonY;
    private final JoystickButtonWrapper buttonStart;
    private final JoystickButtonWrapper buttonSelect;
    private final JoystickButtonWrapper buttonStickLeft;
    private final JoystickButtonWrapper buttonStickRight;
    private final JoystickButtonWrapper buttonBumperLeft;
    private final JoystickButtonWrapper buttonBumperRight;
    
    private GamepadSetting setting;
    
    private boolean aPressed, bPressed, xPressed, yPressed, sUpPressed, sDownPressed;
    private boolean sLeftPressed, sRightPressed, bLeftPressed, bRightPressed, sRRightPressed, sRLeftPressed;
    
    /**
     * Constructs a new GamepadWrapper.
     * @param port the port of the gamepad.
     */
    public GamepadWrapper(int port) {
    	super(port);
        buttonA = new JoystickButtonWrapper(this, XBOX_A_PORT);
        buttonB = new JoystickButtonWrapper(this, XBOX_B_PORT);
        buttonX = new JoystickButtonWrapper(this, XBOX_X_PORT);
        buttonY = new JoystickButtonWrapper(this, XBOX_Y_PORT);
        buttonStart = new JoystickButtonWrapper(this, XBOX_START_PORT);
        buttonSelect = new JoystickButtonWrapper(this, XBOX_SELECT_PORT);
        buttonStickLeft = new JoystickButtonWrapper(this, XBOX_STICK_LEFT_PORT);
        buttonStickRight = new JoystickButtonWrapper(this, XBOX_STICK_RIGHT_PORT);
        buttonBumperLeft = new JoystickButtonWrapper(this, XBOX_BUMPER_LEFT_PORT);
        buttonBumperRight = new JoystickButtonWrapper(this, XBOX_BUMPER_RIGHT_PORT);
        
        this.setting = GamepadSetting.XBOX;
        
        aPressed = bPressed = xPressed = yPressed = sUpPressed = sDownPressed = false;
        sLeftPressed = sRightPressed = bLeftPressed = bRightPressed = sRRightPressed = sRLeftPressed = false;
    }
    
    /**
     * Construts a new GamepadWrapper with the specific type of gamepad specified.
     * @param port the port of the gamepad.
     * @param setting the setting of the gamepad.
     */
    public GamepadWrapper(int port, GamepadSetting setting) {
    	super(port);
    	if (setting == GamepadSetting.XBOX) {
    		buttonA = new JoystickButtonWrapper(this, XBOX_A_PORT);
            buttonB = new JoystickButtonWrapper(this, XBOX_B_PORT);
            buttonX = new JoystickButtonWrapper(this, XBOX_X_PORT);
            buttonY = new JoystickButtonWrapper(this, XBOX_Y_PORT);
            buttonStart = new JoystickButtonWrapper(this, XBOX_START_PORT);
            buttonSelect = new JoystickButtonWrapper(this, XBOX_SELECT_PORT);
            buttonStickLeft = new JoystickButtonWrapper(this, XBOX_STICK_LEFT_PORT);
            buttonStickRight = new JoystickButtonWrapper(this, XBOX_STICK_RIGHT_PORT);
            buttonBumperLeft = new JoystickButtonWrapper(this, XBOX_BUMPER_LEFT_PORT);
            buttonBumperRight = new JoystickButtonWrapper(this, XBOX_BUMPER_RIGHT_PORT);
    	}
    	else if (setting == GamepadSetting.LOGITECH) {
    		buttonA = new JoystickButtonWrapper(this, LOGITECH_A_PORT);
            buttonB = new JoystickButtonWrapper(this, LOGITECH_B_PORT);
            buttonX = new JoystickButtonWrapper(this, LOGITECH_X_PORT);
            buttonY = new JoystickButtonWrapper(this, LOGITECH_Y_PORT);
            buttonStart = new JoystickButtonWrapper(this, LOGITECH_START_PORT);
            buttonSelect = new JoystickButtonWrapper(this, LOGITECH_SELECT_PORT);
            buttonStickLeft = new JoystickButtonWrapper(this, LOGITECH_STICK_LEFT_PORT);
            buttonStickRight = new JoystickButtonWrapper(this, LOGITECH_STICK_RIGHT_PORT);
            buttonBumperLeft = new JoystickButtonWrapper(this, LOGITECH_BUMPER_LEFT_PORT);
            buttonBumperRight = new JoystickButtonWrapper(this, LOGITECH_BUMPER_RIGHT_PORT);
    	}
    	else {
    		throw new RuntimeException("Error, invalid setting given");
    	}
    	this.setting = setting;
    }

    /**
     * Gets the X value being input to the left joystick.
     * @return the left X value
     */
    public double getLeftX() {
    	if (sLeftPressed)
    		return -1;
    	else if (sRightPressed)
    		return 1;
    	if (setting == GamepadSetting.LOGITECH)
    		return getRawAxis(LOGITECH_AXIS_LEFT_X);
    	return getRawAxis(XBOX_AXIS_LEFT_X);
    }

    /**
     * Gets the Y value being input to the left joystick.
     * @return the left Y value
     */
    public double getLeftY() {
    	if (sUpPressed)
    		return 1;
    	else if (sDownPressed)
    		return -1;
    	if (setting == GamepadSetting.LOGITECH)
    		return -getRawAxis(LOGITECH_AXIS_LEFT_Y);
    	return -getRawAxis(XBOX_AXIS_LEFT_Y); //by default, forward returns a negative number, which is unintuitive
    }

    /**
     * Gets the Y value being input to the right joystick.
     * @return the left Y value
     */
    public double getRightX() {
    	if (sRLeftPressed)
    		return -1;
    	if (sRRightPressed)
    		return 1;
    	if (setting == GamepadSetting.LOGITECH)
    		return getRawAxis(LOGITECH_AXIS_RIGHT_X);
    	return getRawAxis(XBOX_AXIS_RIGHT_X);
    }

    /**
     * Gets the Y value being input to the right joystick.
     * @return the right Y value
     */
    public double getRightY() {
    	if (setting == GamepadSetting.LOGITECH) {
    		return -getRawAxis(LOGITECH_AXIS_RIGHT_Y);
    	}
    	return -getRawAxis(XBOX_AXIS_RIGHT_Y); //by default, forward returns a negative number, which is unintuitive
    }
    
    /**
     * Gets the amount the right trigger is currently being pressed.
     * @return the amount by which the right trigger is pressed
     */
    public double getRightTrigger() {
    	if (bRightPressed)
    		return 1;
    	if (setting == GamepadSetting.LOGITECH) {
    		return (getRawButton(LOGITECH_TRIGGER_RIGHT) == true) ? 1 : 0;
    	}
    	return getRawAxis(XBOX_AXIS_TRIGGER_RIGHT);
    }
    
    /**
     * Gets whether or not the right trigger is pressed.
     * @return true if the right trigger is pressed; false otherwise
     */
    public boolean getRightTriggerPressed() {
    	if (setting == GamepadSetting.LOGITECH) {
    		return getRawButton(LOGITECH_TRIGGER_RIGHT);
    	}
    	return getRawAxis(XBOX_AXIS_TRIGGER_RIGHT) > XBOX_TRIGGER_DEADBAND;
    }
    
    /**
     * Gets the amount the left trigger is currently being pressed.
     * @return the amount by which the left trigger is pressed
     */
    public double getLeftTrigger() {
    	if (bLeftPressed)
    		return 1;
    	if (setting == GamepadSetting.LOGITECH) {
    		return (getRawButton(LOGITECH_TRIGGER_LEFT) == true) ? 1 : 0;
    	}
    	return getRawAxis(XBOX_AXIS_TRIGGER_LEFT);
    }
    
    /**
     * Gets whether or not the left trigger is pressed.
     * @return true if the left trigger is pressed; false otherwise
     */
    public boolean getLeftTriggerPressed() {
    	if (setting == GamepadSetting.LOGITECH) {
    		return getRawButton(LOGITECH_TRIGGER_LEFT);
    	}
    	return getRawAxis(XBOX_AXIS_TRIGGER_LEFT) > XBOX_TRIGGER_DEADBAND;
    }
    
    /**
     * Gets whether or not Button A is pressed
     * @return The state of the button
     */
    public boolean getButtonAState() {
    	if (aPressed)
    		return true;
        return buttonA.get();
    }
    
    /**
     * Gets whether or not Button B is pressed
     * @return The state of the button
     */
    public boolean getButtonBState() {
    	if (bPressed)
    		return true;
        return buttonB.get();
    }
    
    /**
     * Gets whether or not Button X is pressed
     * @return The state of the button
     */
    public boolean getButtonXState() {
    	if (xPressed)
    		return true;
        return buttonX.get();
    }
    
    /**
     * Gets whether or not Button Y is pressed
     * @return The state of the button
     */
    public boolean getButtonYState() {
    	if (yPressed)
    		return true;
        return buttonY.get();
    }
    
    /**
     * Gets whether or not the Start Button is pressed
     * @return The state of the button
     */
    public boolean getButtonStartState() {
        return buttonStart.get();
    }
    
    /**
     * Gets whether or not the Select Button is pressed
     * @return The state of the button
     */
    public boolean getButtonSelectState() {
        return buttonSelect.get();
    }
    
    /**
     * Gets whether or not the Left Stick Button is pressed
     * @return The state of the button
     */
    public boolean getButtonStickLeftState() {
        return buttonStickLeft.get();
    }
    
    /**
     * Gets whether or not the Right Stick Button is pressed
     * @return The state of the button
     */
    public boolean getButtonStickRightState() {
        return buttonStickRight.get();
    }
    
    /**
     * Gets whether or not the Left Bumper is pressed
     * @return The state of the button
     */
    public boolean getButtonBumperLeftState() {
        return buttonBumperLeft.get();
    }
    
    /**
     * Gets whether or not the Right Bumper is pressed
     * @return The state of the button
     */
    public boolean getButtonBumperRightState() {
        return buttonBumperRight.get();
    }
    
    /**
     * Gets an instance of Button A
     * @return An instance of the button
     */
    public JoystickButtonWrapper getButtonA() {
        return buttonA;
    }
    
    /**
     * Gets an instance of Button B
     * @return An instance of the button
     */
    public JoystickButtonWrapper getButtonB() {
        return buttonB;
    }
    
    /**
     * Gets an instance of Button X
     * @return An instance of the button
     */
    public JoystickButtonWrapper getButtonX() {
        return buttonX;
    }
    
    /**
     * Gets an instance of Button Y
     * @return An instance of the button
     */
    public JoystickButtonWrapper getButtonY() {
        return buttonY;
    }
    
    /**
     * Gets an instance of the Start Button
     * @return An instance of the button
     */
    public JoystickButtonWrapper getButtonStart() {
        return buttonStart;
    }
    
    /**
     * Gets an instance of the Select Button
     * @return An instance of the button
     */
    public JoystickButtonWrapper getButtonSelect() {
        return buttonSelect;
    }
    
    /**
     * Gets an instance of the Left Stick Button
     * @return An instance of the button
     */
    public JoystickButtonWrapper getButtonStickLeft() {
        return buttonStickLeft;
    }
    
    /**
     * Gets an instance of the Right Stick Button
     * @return An instance of the button
     */
    public JoystickButtonWrapper getButtonStickRight() {
        return buttonStickRight;
    }
    
    /**
     * Gets an instance of the Left Bumper
     * @return An instance of the button
     */
    public JoystickButtonWrapper getButtonBumperLeft() {
        return buttonBumperLeft;
    }
    
    /**
     * Gets an instance of the Right Bumper
     * @return An instance of the button
     */
    public JoystickButtonWrapper getButtonBumperRight() {
        return buttonBumperRight;
    }
}
