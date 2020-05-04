package harkerrobolib.commands.drivetrain;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.kinematics.SwerveModuleState;
import harkerrobolib.commands.IndefiniteCommand;
import harkerrobolib.subsystems.HSSwerveDrivetrain;
import harkerrobolib.util.Constants;
import harkerrobolib.util.MathUtil;
import harkerrobolib.wrappers.HSGamepad;
import harkerrobolib.wrappers.HSJoystickButton;

/**
 * Controls the Swerve Modules using PercentOutput or Velociy for the drive motors and 
 * Position PID for the angle motors.
 * The left joystick controls translation (velocity direction and magnitude)
 * The right joystick's X axis controls rotation (angular velocity magnitude)
 * 
 * 'back' is defined as closest to the battery
 * 'left' is defined as left when standing at the back and looking forward
 * 
 * @author Shahzeb Lakhani
 * @author Jatin Kohli
 * @author Anirudh Kotamraju
 * @author Angela Jia
 * @author Chirag Kaushik
 * @author Arjun Dixit
 * @author Rohan Bhowmik
 * 
 * @version May 3, 2020
 */
public class SwerveManualHeadingControl extends IndefiniteCommand {
    private double translateX, translateY, headingX, headingY, headingAngle, turnMagnitude;
    
    private PIDController headingController;
    private static boolean joystickFlag, headingFlag, hadHeading, prevHeadingFlag;

    private static boolean counterClockwisePressed, counterClockwiseFlag, clockwisePressed, clockwiseFlag;
    private boolean hasSlowMode;
    private boolean hasToggleCentricity;
    private boolean centricity;
    private boolean prevToggleCentricity;
    private boolean currentToggleCentricity;

    private HSGamepad gamepad;
    private HSSwerveDrivetrain drivetrain;
    private HSJoystickButton turnClockwise;
    private HSJoystickButton turnCounterClockwise;
    private HSJoystickButton slowMode;
    private HSJoystickButton toggleCentricity;
    private int driveVelocitySlot;
    private int anglePositionSlot;
    
    private double maxDriveVelocity;
    
    private double maxRotationVelocity;
    
    private double slowModeMultiplier;
    private double outputMultiplier;
    private double rotationHeading;
    private double outputManualRotation;

    /**
     * Normal constructor excluding any additional features such as slowmode or 
     *.
     * @param headingController The PID Controller keeping track of the pigeon's heading.
     * @param gamepad The controller to read input from.
     * @param turnClockwise the button used to rotate the robot quickly clockwise.
     * @param turnCounterClockwise the button used to rotate the robot quickly counterclockwise.
     * @param rotationHeading The amount (in degrees) to rotate while the button is pressed (Ex. 2020's value was 130).
     * @param outputManualRotation The output in which to rotate when pressing the button.
     * @param driveVelocitySlot The drive motor velocity slot.
     * @param anglePositionSlot The angle motor position slot.
     * @param maxDriveVelocity The maximum drive velocity.
     * @param maxRotationVelocity The maximum rotation velocity.
     * @param outputMultiplier The output multiplier.
     */
    public SwerveManualHeadingControl(HSSwerveDrivetrain drivetrain, PIDController headingController, HSGamepad gamepad,
            HSJoystickButton turnClockwise, HSJoystickButton turnCounterClockwise, double rotationHeading, double outputManualRotation, int driveVelocitySlot, 
            int anglePositionSlot, double maxDriveVelocity, double maxRotationVelocity, double outputMultiplier) {
        this(drivetrain, headingController, gamepad, turnClockwise, turnCounterClockwise, rotationHeading, outputManualRotation,
        driveVelocitySlot, anglePositionSlot, maxDriveVelocity, maxRotationVelocity, outputMultiplier, null, 1, null);
    }   
    
    /**
     * Constructor with a specified button and multiplier for slow mode but no robot-field centric toggle button
     * 
     * @param drivetrain The Drivetrain instance.
     * @param headingController The PID Controller keeping track of the pigeon's heading.
     * @param gamepad The controller to read input from.
     * @param turnClockwise the button used to rotate the robot quickly clockwise.
     * @param turnCounterClockwise the button used to rotate the robot quickly counterclockwise.
     * @param rotationHeading The amount (in degrees) to rotate while the button is pressed (Ex. 2020's value was 130).
     * @param outputManualRotation The output in which to rotate when pressing the button.
     * @param driveVelocitySlot The drive motor velocity slot.
     * @param anglePositionSlot The angle motor position slot.
     * @param maxDriveVelocity The maximum drive velocity.
     * @param maxRotationVelocity The maximum rotation velocity.
     * @param outputMultiplier The output multiplier.
     * <h3>
     * The next parameters are optional, without it no button will be bound for utilizing slow mode:
     * </h3>
     * @param slowMode The button to activate slow mode when pressed.
     * @param slowModeMultipier The multiplier for the slowmode.
     */
    public SwerveManualHeadingControl(HSSwerveDrivetrain drivetrain, PIDController headingController, HSGamepad gamepad,
            HSJoystickButton turnClockwise, HSJoystickButton turnCounterClockwise, double rotationHeading, double outputManualRotation, int driveVelocitySlot, 
            int anglePositionSlot, double maxDriveVelocity, double maxRotationVelocity, double outputMultiplier, HSJoystickButton slowMode, double slowModeMultipier) {
        this(drivetrain, headingController, gamepad, turnClockwise, turnCounterClockwise, rotationHeading, outputManualRotation, driveVelocitySlot, 
            anglePositionSlot, maxDriveVelocity, maxRotationVelocity, outputMultiplier, slowMode, slowModeMultipier, null);
    }

    /**
     * Normal constructor excluding any additional features such as slowmode or 
     *.
     * @param headingController The PID Controller keeping track of the pigeon's heading.
     * @param gamepad The controller to read input from.
     * @param turnClockwise the button used to rotate the robot quickly clockwise.
     * @param turnCounterClockwise the button used to rotate the robot quickly counterclockwise.
     * @param rotationHeading The amount (in degrees) to rotate while the button is pressed (Ex. 2020's value was 130).
     * @param outputManualRotation The output in which to rotate when pressing the button.
     * @param driveVelocitySlot The drive motor velocity slot.
     * @param anglePositionSlot The angle motor position slot.
     * @param maxDriveVelocity The maximum drive velocity.
     * @param maxRotationVelocity The maximum rotation velocity.
     * @param outputMultiplier The output multiplier.
     * <h3>
     * The next parameter is optional, without it no button will be bound for toggling centricity.
     * </h3>
     * @param toggleCentricity Button to toggle between robot and field centric driving modes.
     */
    public SwerveManualHeadingControl(HSSwerveDrivetrain drivetrain, PIDController headingController, HSGamepad gamepad,
            HSJoystickButton turnClockwise, HSJoystickButton turnCounterClockwise, double rotationHeading, double outputManualRotation, int driveVelocitySlot, 
            int anglePositionSlot, double maxDriveVelocity, double maxRotationVelocity, double outputMultiplier, HSJoystickButton toggleCentricity) {
        this(drivetrain, headingController, gamepad, turnClockwise, turnCounterClockwise, rotationHeading, outputManualRotation, 
        driveVelocitySlot, anglePositionSlot, maxDriveVelocity, maxRotationVelocity, outputMultiplier, null, 1, toggleCentricity);
    }

    /**
     * Constructor with both a button and multiplier for slow mode and a button for toggling between field and robot centric mode
     * 
     * @param drivetrain The Drivetrain instance.
     * @param headingController The PID Controller keeping track of the pigeon's heading.
     * @param gamepad The controller to read input from.
     * @param turnClockwise the button used to rotate the robot quickly clockwise.
     * @param turnCounterClockwise the button used to rotate the robot quickly counterclockwise.
     * @param rotationHeading The amount (in degrees) to rotate while the button is pressed (Ex. 2020's value was 130).
     * @param outputManualRotation The output in which to rotate when pressing the button.
     * @param driveVelocitySlot The drive motor velocity slot.
     * @param anglePositionSlot The angle motor position slot.
     * @param maxDriveVelocity The maximum drive velocity.
     * @param maxRotationVelocity The maximum rotation velocity.
     * @param outputMultiplier The output multiplier.
     * <h3>
     *  The following parameters are all optional. They will default to null, 0.0, and null respectively. 
     * </h3>
     * @param slowMode The button to activate slow mode when pressed.
     * @param slowModeMultipier The multiplier for the slowmode.
     * @param toggleCentricity The button to toggle the centricity of the robot(Field relative/Robot-Centric). 
     */
    public SwerveManualHeadingControl(HSSwerveDrivetrain drivetrain, PIDController headingController, HSGamepad gamepad,
            HSJoystickButton turnClockwise, HSJoystickButton turnCounterClockwise, double rotationHeading, double outputManualRotation, int driveVelocitySlot, 
            int anglePositionSlot, double maxDriveVelocity, double maxRotationVelocity, double outputMultiplier, HSJoystickButton slowMode, double slowModeMultiplier, 
            HSJoystickButton toggleCentricity) {
        addRequirements(drivetrain);

        this.drivetrain = drivetrain;
        this.driveVelocitySlot = driveVelocitySlot;
        this.anglePositionSlot = anglePositionSlot;
        this.turnClockwise = turnClockwise;
        this.turnCounterClockwise = turnCounterClockwise;
        this.rotationHeading = rotationHeading;
        this.headingController = headingController;
        this.maxDriveVelocity = maxDriveVelocity;
        this.gamepad = gamepad;
        this.slowMode = slowMode;
        this.maxRotationVelocity = maxRotationVelocity;
        this.outputMultiplier = outputMultiplier;
        this.slowModeMultiplier = slowModeMultiplier;
        this.toggleCentricity = toggleCentricity;
        this.rotationHeading = rotationHeading;
        this.outputManualRotation = outputManualRotation;
        
        hasSlowMode = (slowMode != null);
        hasToggleCentricity = (toggleCentricity != null); 
    }

    @Override
    public void initialize() {
        drivetrain.applyToAllDrive(
            (driveMotor) -> driveMotor.selectProfileSlot(driveVelocitySlot, Constants.PID_PRIMARY)
        );

        drivetrain.applyToAllAngle(
            (angleMotor) -> angleMotor.selectProfileSlot(anglePositionSlot, Constants.PID_PRIMARY)
        );
        
        joystickFlag = false;
        headingFlag = false;
        hadHeading = false;
        prevHeadingFlag = false;
    }

    @Override
    public void execute() {
        translateX = MathUtil.mapJoystickOutput(gamepad.getLeftX(), Constants.JOYSTICK_DEADBAND);
        translateY = MathUtil.mapJoystickOutput(gamepad.getLeftY(), Constants.JOYSTICK_DEADBAND);
        headingX = MathUtil.mapJoystickOutput(gamepad.getRightX(), 0.25);
        headingY = MathUtil.mapJoystickOutput(gamepad.getRightY(), 0.25);
        
        currentToggleCentricity = hasToggleCentricity && toggleCentricity.get();
        if (currentToggleCentricity && !prevToggleCentricity) {
            centricity = !centricity; //switch modes
        }

        if (headingY != 0 || headingX != 0) {
            headingAngle = Math.toDegrees(Math.atan2(headingY, headingX));
            headingAngle -= 90;
            
            while (drivetrain.getPigeon().getFusedHeading() - headingAngle > 180) {
                headingAngle += 360;
            }

            while (drivetrain.getPigeon().getFusedHeading() - headingAngle <- 180) {
                headingAngle -= 360;
            }

            headingFlag = true;
        } else {
            headingFlag = false;
        }

        if (!headingFlag && prevHeadingFlag) {
            hadHeading = true;
        } 
        else if (headingFlag) {
            hadHeading = false;
        }
        
        turnMagnitude = -1 * headingController.calculate(drivetrain.getPigeon().getFusedHeading(), headingAngle);

        if (Math.abs(translateX) > 0 || Math.abs(translateY) > 0 || Math.abs(headingX) > 0 || Math.abs(headingY) > 0) {
            joystickFlag = true;
        }
        
        //scale input from joysticks
        translateX *= outputMultiplier * maxDriveVelocity * ((hasSlowMode && slowMode.get()) ? slowModeMultiplier : 1);
        translateY *= outputMultiplier * maxDriveVelocity * ((hasSlowMode && slowMode.get()) ? slowModeMultiplier : 1);
        turnMagnitude *= -1 * outputMultiplier * maxRotationVelocity;
        
        counterClockwiseFlag = counterClockwisePressed && !turnCounterClockwise.get();
        counterClockwisePressed = turnCounterClockwise.get();

        if (counterClockwiseFlag) 
            headingAngle = drivetrain.getPigeon().getFusedHeading() - rotationHeading;

        clockwiseFlag = clockwisePressed && !turnClockwise.get();
        clockwisePressed = turnClockwise.get();

        if (clockwiseFlag)
            headingAngle = drivetrain.getPigeon().getFusedHeading() + rotationHeading;

        if (counterClockwisePressed) 
            turnMagnitude = -outputManualRotation * outputMultiplier * maxRotationVelocity;
        else if (clockwisePressed)
            turnMagnitude = outputManualRotation * outputMultiplier * maxRotationVelocity;

        ChassisSpeeds speeds;

        if (centricity) { //Field Centric
            speeds = ChassisSpeeds.fromFieldRelativeSpeeds(
                translateX, translateY, (headingFlag || hadHeading) ? turnMagnitude : 0, Rotation2d.fromDegrees(drivetrain.getPigeon().getFusedHeading())
            );
        } else { //Robot Centric
            speeds = new ChassisSpeeds(translateX, translateY, headingX * maxRotationVelocity);
        }

        SwerveModuleState[] moduleStates = drivetrain.getKinematics().toSwerveModuleStates(speeds);

        if (joystickFlag)
            drivetrain.setDrivetrainVelocity(moduleStates[0], moduleStates[1], moduleStates[2], moduleStates[3], false, false);

        prevHeadingFlag = headingFlag;
        prevToggleCentricity = currentToggleCentricity;
    }

    @Override
    public void end(boolean interrupted) {
        drivetrain.stopAllDrive();
    }
}