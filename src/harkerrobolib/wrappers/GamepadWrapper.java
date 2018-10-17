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
public abstract class GamepadWrapper extends Joystick {
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
    
    private final int axisLeftX;
    private final int axisLeftY;
    private final int axisRightX;
    private final int axisRightY;

    public GamepadWrapper(int port, 
    		int buttonAPort, int buttonBPort, int buttonXPort, int buttonYPort, 
    		int buttonStartPort, int buttonSelectPort, 
			int buttonStickLeftPort, int buttonStickRightPort, 
			int buttonBumperLeftPort, int buttonBumperRightPort,
			int axisLeftX, int axisLeftY, int axisRightX, int axisRightY) {
    	super(port);
        buttonA = new JoystickButtonWrapper(this, buttonAPort);
        buttonB = new JoystickButtonWrapper(this, buttonBPort);
        buttonX = new JoystickButtonWrapper(this, buttonXPort);
        buttonY = new JoystickButtonWrapper(this, buttonYPort);
        buttonStart = new JoystickButtonWrapper(this, buttonStartPort);
        buttonSelect = new JoystickButtonWrapper(this, buttonSelectPort);
        buttonStickLeft = new JoystickButtonWrapper(this, buttonStickLeftPort);
        buttonStickRight = new JoystickButtonWrapper(this, buttonStickRightPort);
        buttonBumperLeft = new JoystickButtonWrapper(this, buttonBumperLeftPort);
        buttonBumperRight = new JoystickButtonWrapper(this, buttonBumperRightPort);
        
        this.axisLeftX = axisLeftX;
        this.axisLeftY = axisLeftY;
        this.axisRightX = axisRightX;
        this.axisRightY = axisRightY;
	}

    /**
     * Gets the X value being input to the left joystick.
     * @return the left X value
     */
    public double getLeftX() {
    	return getRawAxis(axisLeftX);
    }

    /**
     * Gets the Y value being input to the left joystick.
     * @return the left Y value
     */
    public double getLeftY() {
    	return -getRawAxis(axisLeftY); //by default, forward returns a negative number, which is unintuitive
    }

    /**
     * Gets the Y value being input to the right joystick.
     * @return the left Y value
     */
    public double getRightX() {
    	return getRawAxis(axisRightX);
    }

    /**
     * Gets the Y value being input to the right joystick.
     * @return the right Y value
     */
    public double getRightY() {
    	return -getRawAxis(axisRightY); //by default, forward returns a negative number, which is unintuitive
    }
    
    /**
     * Gets the amount the right trigger is currently being pressed.
     * @return the amount by which the right trigger is pressed
     */
    public abstract double getRightTrigger();
    
    /**
     * Gets the amount the left trigger is currently being pressed.
     * @return the amount by which the left trigger is pressed
     */
    public abstract double getLeftTrigger();
    
    /**
     * Gets whether or not Button A is pressed
     * @return The state of the button
     */
    public boolean getButtonAState() {
        return buttonA.get();
    }
    
    /**
     * Gets whether or not Button B is pressed
     * @return The state of the button
     */
    public boolean getButtonBState() {
        return buttonB.get();
    }
    
    /**
     * Gets whether or not Button X is pressed
     * @return The state of the button
     */
    public boolean getButtonXState() {
        return buttonX.get();
    }
    
    /**
     * Gets whether or not Button Y is pressed
     * @return The state of the button
     */
    public boolean getButtonYState() {
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
