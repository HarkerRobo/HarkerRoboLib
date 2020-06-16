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
 * Controls the Swerve Modules using PercentOutput or Velocity for the drive motors and 
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
 * @since April 19, 2020
 */
public class SwerveManualHeadingControl extends IndefiniteCommand {
    private double translateX, translateY, headingX, headingY, headingAngle, turnMagnitude;
    
    public PIDController headingController;
    public static boolean joystickFlag, headingFlag, flag, prevHeadingFlag;

    private static boolean counterClockwisePressed, counterClockwiseFlag, clockwisePressed, clockwiseFlag;
    private boolean hasSlowMode;
    private boolean hasToggleCentricity;
    private boolean centricity;
    private boolean prevToggleCentricity;
    private boolean currentToggleCentricity;
    public static boolean isNotOptimized;

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

    /**
     * Has no optional parameters
     */
    public SwerveManualHeadingControl(HSSwerveDrivetrain drivetrain, PIDController headingController, HSGamepad gamepad,
            HSJoystickButton turnClockwise, HSJoystickButton turnCounterClockwise, int driveVelocitySlot, int anglePositionSlot,
            double maxDriveVelocity, double maxRotationVelocity, double outputMultiplier) {
        this(drivetrain, headingController, gamepad, turnClockwise, turnCounterClockwise, driveVelocitySlot, 
            anglePositionSlot, maxDriveVelocity, maxRotationVelocity, outputMultiplier, null, 1, null);
    }   
    
    /**
     * Has Slow Mode functionality 
     */
    public SwerveManualHeadingControl(HSSwerveDrivetrain drivetrain, PIDController headingController, HSGamepad gamepad,
            HSJoystickButton turnClockwise, HSJoystickButton turnCounterClockwise, int driveVelocitySlot, int anglePositionSlot,
            double maxDriveVelocity, double maxRotationVelocity, double outputMultiplier, HSJoystickButton slowMode, double slowModeMultipier) {
        this(drivetrain, headingController, gamepad, turnClockwise, turnCounterClockwise, driveVelocitySlot, 
            anglePositionSlot, maxDriveVelocity, maxRotationVelocity, outputMultiplier, slowMode, slowModeMultipier, null);
    }

    /** 
      * Has Toggling centricity
     */
    public SwerveManualHeadingControl(HSSwerveDrivetrain drivetrain, PIDController headingController, HSGamepad gamepad,
            HSJoystickButton turnClockwise, HSJoystickButton turnCounterClockwise, int driveVelocitySlot, int anglePositionSlot,
            double maxDriveVelocity, double maxRotationVelocity, double outputMultiplier, HSJoystickButton toggleCentricity) {
        this(drivetrain, headingController, gamepad, turnClockwise, turnCounterClockwise, driveVelocitySlot, 
                anglePositionSlot, maxDriveVelocity, maxRotationVelocity, outputMultiplier, null, 1, toggleCentricity);
    }

    /**
     * Toggling field centric and slowmode
     */
    public SwerveManualHeadingControl(HSSwerveDrivetrain drivetrain, PIDController headingController, HSGamepad gamepad,
            HSJoystickButton turnClockwise, HSJoystickButton turnCounterClockwise, int driveVelocitySlot, int anglePositionSlot,
            double maxDriveVelocity, double maxRotationVelocity, double outputMultiplier, HSJoystickButton slowMode, double slowModeMultiplier, 
            HSJoystickButton toggleCentricity) {
        addRequirements(drivetrain);

        this.drivetrain = drivetrain;
        this.driveVelocitySlot = driveVelocitySlot;
        this.anglePositionSlot = anglePositionSlot;
        this.turnClockwise = turnClockwise;
        this.turnCounterClockwise = turnCounterClockwise;
        this.headingController = headingController;
        this.maxDriveVelocity = maxDriveVelocity;
        this.gamepad = gamepad;
        this.slowMode = slowMode;
        this.maxRotationVelocity = maxRotationVelocity;
        this.outputMultiplier = outputMultiplier;
        this.slowModeMultiplier = slowModeMultiplier;
        this.toggleCentricity = toggleCentricity;

        hasSlowMode = (slowMode != null);
        hasToggleCentricity = (toggleCentricity != null); 
        currentToggleCentricity = false;
        prevToggleCentricity = false;
        centricity = false;
        counterClockwisePressed = false;
        counterClockwiseFlag = false;
        clockwisePressed = false;
        clockwiseFlag = false;
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
        isNotOptimized = false;
        flag = false;
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
            centricity = !centricity;
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
            flag = true;
        } 
        else if (headingFlag) {
            flag = false;
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
            headingAngle = drivetrain.getPigeon().getFusedHeading() - 130;

        clockwiseFlag = clockwisePressed && !turnClockwise.get();
        clockwisePressed = turnClockwise.get();

        if (clockwiseFlag)
            headingAngle = drivetrain.getPigeon().getFusedHeading() + 130;

        if (counterClockwisePressed) 
            turnMagnitude = -0.7 * outputMultiplier * maxRotationVelocity;
        else if (clockwisePressed)
            turnMagnitude = 0.7 * outputMultiplier * maxRotationVelocity;

        ChassisSpeeds speeds;

        if (centricity) {
            speeds = ChassisSpeeds.fromFieldRelativeSpeeds(
                translateX, translateY, (headingFlag || flag) ? turnMagnitude : 0, Rotation2d.fromDegrees(drivetrain.getPigeon().getFusedHeading())
            );
        } else {
            speeds = new ChassisSpeeds(translateX, translateY, headingX * maxRotationVelocity);
        }

        // Now use this in our kinematics
        SwerveModuleState[] moduleStates = drivetrain.getKinematics().toSwerveModuleStates(speeds);

        if (joystickFlag)
            drivetrain.setDrivetrainVelocity(moduleStates[0], moduleStates[1], moduleStates[2], moduleStates[3], false, isNotOptimized);

        prevHeadingFlag = headingFlag;
        prevToggleCentricity = currentToggleCentricity;
    }

    @Override
    public void end(boolean interrupted) {
        drivetrain.stopAllDrive();

        headingFlag = false;
        joystickFlag = false;
    }
}