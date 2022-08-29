package harkerrobolib.joysticks;

import edu.wpi.first.wpilibj.Joystick;

/**
 * A Joystick wrapper for gamepads that include more accurate/useful names for the analogue stick
 * axes
 *
 * @author neymikajain
 * @author atierno
 */
public abstract class HSGamepad extends Joystick {
  private final HSJoystickButton buttonA;
  private final HSJoystickButton buttonB;
  private final HSJoystickButton buttonX;
  private final HSJoystickButton buttonY;
  private final HSJoystickButton buttonStart;
  private final HSJoystickButton buttonSelect;
  private final HSJoystickButton buttonStickLeft;
  private final HSJoystickButton buttonStickRight;
  private final HSJoystickButton buttonBumperLeft;
  private final HSJoystickButton buttonBumperRight;

  private final int axisLeftX;
  private final int axisLeftY;
  private final int axisRightX;
  private final int axisRightY;

  // DPad angles in degrees
  public static final int DPAD_UP_ANGLE = 0;
  public static final int DPAD_LEFT_ANGLE = 270;
  public static final int DPAD_RIGHT_ANGLE = 90;
  public static final int DPAD_DOWN_ANGLE = 180;

  public HSGamepad(
      int port,
      int buttonAPort,
      int buttonBPort,
      int buttonXPort,
      int buttonYPort,
      int buttonStartPort,
      int buttonSelectPort,
      int buttonStickLeftPort,
      int buttonStickRightPort,
      int buttonBumperLeftPort,
      int buttonBumperRightPort,
      int axisLeftX,
      int axisLeftY,
      int axisRightX,
      int axisRightY) {
    super(port);
    buttonA = new HSJoystickButton(this, buttonAPort);
    buttonB = new HSJoystickButton(this, buttonBPort);
    buttonX = new HSJoystickButton(this, buttonXPort);
    buttonY = new HSJoystickButton(this, buttonYPort);
    buttonStart = new HSJoystickButton(this, buttonStartPort);
    buttonSelect = new HSJoystickButton(this, buttonSelectPort);
    buttonStickLeft = new HSJoystickButton(this, buttonStickLeftPort);
    buttonStickRight = new HSJoystickButton(this, buttonStickRightPort);
    buttonBumperLeft = new HSJoystickButton(this, buttonBumperLeftPort);
    buttonBumperRight = new HSJoystickButton(this, buttonBumperRightPort);

    this.axisLeftX = axisLeftX;
    this.axisLeftY = axisLeftY;
    this.axisRightX = axisRightX;
    this.axisRightY = axisRightY;
  }

  /**
   * Gets the X value being input to the left joystick.
   *
   * @return the left X value
   */
  public double getLeftX() {
    return getRawAxis(axisLeftX);
  }

  /**
   * Gets the Y value being input to the left joystick.
   *
   * @return the left Y value
   */
  public double getLeftY() {
    return -getRawAxis(
        axisLeftY); // by default, forward returns a negative number, which is unintuitive
  }

  /**
   * Gets the Y value being input to the right joystick.
   *
   * @return the left Y value
   */
  public double getRightX() {
    return getRawAxis(axisRightX);
  }

  /**
   * Gets the Y value being input to the right joystick.
   *
   * @return the right Y value
   */
  public double getRightY() {
    return -getRawAxis(
        axisRightY); // by default, forward returns a negative number, which is unintuitive
  }

  /**
   * Gets the amount the right trigger is currently being pressed.
   *
   * @return the amount by which the right trigger is pressed
   */
  public abstract double getRightTrigger();

  /**
   * Gets the amount the left trigger is currently being pressed.
   *
   * @return the amount by which the left trigger is pressed
   */
  public abstract double getLeftTrigger();

  /**
   * Gets whether or not Button A is pressed
   *
   * @return The state of the button
   */
  public boolean getButtonAState() {
    return buttonA.get();
  }

  /**
   * Gets whether or not Button B is pressed
   *
   * @return The state of the button
   */
  public boolean getButtonBState() {
    return buttonB.get();
  }

  /**
   * Gets whether or not Button X is pressed
   *
   * @return The state of the button
   */
  public boolean getButtonXState() {
    return buttonX.get();
  }

  /**
   * Gets whether or not Button Y is pressed
   *
   * @return The state of the button
   */
  public boolean getButtonYState() {
    return buttonY.get();
  }

  /**
   * Gets whether or not the Start Button is pressed
   *
   * @return The state of the button
   */
  public boolean getButtonStartState() {
    return buttonStart.get();
  }

  /**
   * Gets whether or not the Select Button is pressed
   *
   * @return The state of the button
   */
  public boolean getButtonSelectState() {
    return buttonSelect.get();
  }

  /**
   * Gets whether or not the Left Stick Button is pressed
   *
   * @return The state of the button
   */
  public boolean getButtonStickLeftState() {
    return buttonStickLeft.get();
  }

  /**
   * Gets whether or not the Right Stick Button is pressed
   *
   * @return The state of the button
   */
  public boolean getButtonStickRightState() {
    return buttonStickRight.get();
  }

  /**
   * Gets whether or not the Left Bumper is pressed
   *
   * @return The state of the button
   */
  public boolean getButtonBumperLeftState() {
    return buttonBumperLeft.get();
  }

  /**
   * Gets whether or not the Right Bumper is pressed
   *
   * @return The state of the button
   */
  public boolean getButtonBumperRightState() {
    return buttonBumperRight.get();
  }

  /**
   * Gets an instance of Button A
   *
   * @return An instance of the button
   */
  public HSJoystickButton getButtonA() {
    return buttonA;
  }

  /**
   * Gets an instance of Button B
   *
   * @return An instance of the button
   */
  public HSJoystickButton getButtonB() {
    return buttonB;
  }

  /**
   * Gets an instance of Button X
   *
   * @return An instance of the button
   */
  public HSJoystickButton getButtonX() {
    return buttonX;
  }

  /**
   * Gets an instance of Button Y
   *
   * @return An instance of the button
   */
  public HSJoystickButton getButtonY() {
    return buttonY;
  }

  /**
   * Gets an instance of the Start Button
   *
   * @return An instance of the button
   */
  public HSJoystickButton getButtonStart() {
    return buttonStart;
  }

  /**
   * Gets an instance of the Select Button
   *
   * @return An instance of the button
   */
  public HSJoystickButton getButtonSelect() {
    return buttonSelect;
  }

  /**
   * Gets an instance of the Left Stick Button
   *
   * @return An instance of the button
   */
  public HSJoystickButton getButtonStickLeft() {
    return buttonStickLeft;
  }

  /**
   * Gets an instance of the Right Stick Button
   *
   * @return An instance of the button
   */
  public HSJoystickButton getButtonStickRight() {
    return buttonStickRight;
  }

  /**
   * Gets an instance of the Left Bumper
   *
   * @return An instance of the button
   */
  public HSJoystickButton getButtonBumperLeft() {
    return buttonBumperLeft;
  }

  /**
   * Gets an instance of the Right Bumper
   *
   * @return An instance of the button
   */
  public HSJoystickButton getButtonBumperRight() {
    return buttonBumperRight;
  }

  public HSDPadButton getUpDPadButton() {
    return new HSDPadButton(this, DPAD_UP_ANGLE);
  }

  public HSDPadButton getDownDPadButton() {
    return new HSDPadButton(this, DPAD_DOWN_ANGLE);
  }

  public HSDPadButton getLeftDPadButton() {
    return new HSDPadButton(this, DPAD_LEFT_ANGLE);
  }

  public HSDPadButton getRightDPadButton() {
    return new HSDPadButton(this, DPAD_RIGHT_ANGLE);
  }
}
