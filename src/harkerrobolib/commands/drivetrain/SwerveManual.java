package harkerrobolib.commands.drivetrain;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.kinematics.SwerveModuleState;
import harkerrobolib.commands.IndefiniteCommand;
import harkerrobolib.subsystems.HSSwerveDrivetrain;
import harkerrobolib.util.Constants;
import harkerrobolib.util.MathUtil;
import harkerrobolib.wrappers.HSGamepad;

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
 * @author Chirag Kaushik
 * @author Angela Jia
 * @author Jatin Kohli
 * @author Anirudh Kotamraju
 * @author Arjun Dixit
 * @author Rohan Bhowmik
 * @since 11/4/19
 */
public class SwerveManual extends IndefiniteCommand {
    private double outputMultiplier;

    private double translateX, translateY, turnMagnitude;
    
    private static double prevPigeonHeading;
    private static double prevTime;
    private static double prevVel;
    private static boolean pigeonFlag; //True if the Driver Right X input is non-zero
    private static double pigeonAngle;

    private static double lastPigeonUpdateTime; // seconds
    private static double turnVel;
    private static double turnAccel;

    private static boolean joystickFlag;
    
    private HSSwerveDrivetrain drivetrain;
    private int driveVelocitySlot;
    private int anglePositionSlot;
    private HSGamepad gamepad;
    private double maxDriveVelocity;
    private double maxRotationVelocity;
    private double pigeon_kP;

    private boolean isPercentOutput;
    private double lowHeadingMultipier;
    private double highHeadingMultipler;
    private double turnVelThreshold;
    private double accelMultiplier;

    /**
     * Constructor for SwerveManual Command
     * 
     * @param drivetrain The drivetrain instance
     * @param driveVelocitySlot The drive velocity slot
     * @param anglePositionSlot The angle position slot
     * @param gamepad The driver gamepad to control the robot
     * @param maxDriveVelocity The max translational velocity of the robot
     * @param maxRotationVelocity The max rotational velocity in omega radians per second
     * @param pigeon_kP The 'P' constant to set a pigeon to an angle.
     * @param outputMultiplier The output multiplier for the velocity of the robot.
     * @param isPercentOutput True if the drive wheels should be driven in Percent Output mode, false for Velocity PID mode
     */
    public SwerveManual(HSSwerveDrivetrain drivetrain, int driveVelocitySlot, int anglePositionSlot, 
        HSGamepad gamepad, double maxDriveVelocity, double maxRotationVelocity, double pigeon_kP, double outputMultiplier,
        boolean isPercentOutput) {
        this(drivetrain, driveVelocitySlot, anglePositionSlot, gamepad, maxDriveVelocity, maxRotationVelocity,
        pigeon_kP, outputMultiplier, isPercentOutput, 0, 0, 0, 0);
    }
    
    /**
     * Constructor for SwerveManual Command
     * 
     * @param drivetrain The drivetrain instance
     * @param driveVelocitySlot The drive velocity slot
     * @param anglePositionSlot The angle position slot
     * @param gamepad The driver gamepad to control the robot
     * @param maxDriveVelocity The max translational velocity of the robot
     * @param maxRotationVelocity The max rotational velocity in omega radians per second
     * @param pigeon_kP The 'P' constant to set a pigeon to an angle.
     * @param outputMultiplier The output multiplier for the velocity of the robot.
     * @param isPercentOutput True if the drive wheels should be driven in Percent Output mode, false for Velocity PID mode
     * <h3>
     * If the below parameters are not set, they are set to 0 automatically:
     * </h3>
     * @param lowHeadingMultiplier Value to multiply the pigeon velocity by (if the velocity is less than or equal to turnVelThreshold) and add to the output
     * @param highHeadingMultiplier Value to multiply the pigeon velocity by (if the velocity is greater than turnVelThreshold) and add to the output
     * @param turnVelThreshold The minimum velocity needed so highHeadingMultiplier is used against the current velocity
     * @param accelMultiplier Value to multipy the pigeon acceleration by and add to the output
     */
    public SwerveManual(HSSwerveDrivetrain drivetrain, int driveVelocitySlot, int anglePositionSlot, 
        HSGamepad gamepad, double maxDriveVelocity, double maxRotationVelocity, double pigeon_kP, double outputMultiplier,
        boolean isPercentOutput, double lowHeadingMultiplier, double highHeadingMultiplier, double turnVelThreshold, double accelMultiplier) {
        addRequirements(drivetrain);

        this.driveVelocitySlot = driveVelocitySlot;
        this.anglePositionSlot = anglePositionSlot;
        this.gamepad = gamepad;
        this.maxDriveVelocity = maxDriveVelocity;
        this.maxRotationVelocity = maxRotationVelocity;
        this.pigeon_kP = pigeon_kP;
        this.outputMultiplier = outputMultiplier;
        this.isPercentOutput = isPercentOutput;
        this.lowHeadingMultipier = lowHeadingMultiplier;
        this.highHeadingMultipler = highHeadingMultiplier;
        this.turnVelThreshold = turnVelThreshold;
        this.accelMultiplier = accelMultiplier;

        prevTime = Timer.getFPGATimestamp();
        lastPigeonUpdateTime = Timer.getFPGATimestamp();
        prevPigeonHeading = drivetrain.getPigeon().getFusedHeading();
        prevVel = 0;
    }

    @Override
    public void initialize() {
        drivetrain.applyToAllDrive(
            (driveMotor) -> driveMotor.selectProfileSlot(driveVelocitySlot, Constants.PID_PRIMARY)
        );

        drivetrain.applyToAllAngle(
            (angleMotor) -> angleMotor.selectProfileSlot(anglePositionSlot, Constants.PID_PRIMARY)
        );

        pigeonFlag = true;
        prevPigeonHeading = drivetrain.getPigeon().getFusedHeading();
        pigeonAngle = prevPigeonHeading;

        joystickFlag = true;
    }

    @Override
    public void execute() {
        translateX = MathUtil.mapJoystickOutput(gamepad.getLeftX(), Constants.JOYSTICK_DEADBAND);
        translateY = MathUtil.mapJoystickOutput(gamepad.getLeftY(), Constants.JOYSTICK_DEADBAND);
        turnMagnitude = MathUtil.mapJoystickOutput(gamepad.getRightX(), Constants.JOYSTICK_DEADBAND);
        
        if (Math.abs(translateX) > 0 || Math.abs(translateY) > 0 || Math.abs(turnMagnitude) > 0)
            joystickFlag = true;

        //scale input from joysticks
        translateX *= outputMultiplier * maxDriveVelocity;
        translateY *= outputMultiplier * maxDriveVelocity;
        turnMagnitude *= -1 * outputMultiplier * maxRotationVelocity;

        double currentPigeonHeading = drivetrain.getPigeon().getFusedHeading();

        if(pigeonFlag && turnMagnitude == 0) { //If there was joystick input but now there is not
            double velocityHeadingMultiplier = Math.abs(turnVel) > turnVelThreshold ? highHeadingMultipler : lowHeadingMultipier;

            // account for momentum when turning
            pigeonAngle = currentPigeonHeading + turnVel * velocityHeadingMultiplier + turnAccel * accelMultiplier;
        }

        pigeonFlag = Math.abs(turnMagnitude) > 0; //Update pigeon flag

        if(!pigeonFlag) //If there is no joystick input currently
            turnMagnitude = pigeon_kP * (pigeonAngle - currentPigeonHeading);

        ChassisSpeeds speeds = ChassisSpeeds.fromFieldRelativeSpeeds(
            translateX, translateY, turnMagnitude, Rotation2d.fromDegrees(drivetrain.getPigeon().getFusedHeading())
        );

        // Now use this in our kinematics
        SwerveModuleState[] moduleStates = drivetrain.getKinematics().toSwerveModuleStates(speeds);

        if (joystickFlag)
            drivetrain.setDrivetrainVelocity(moduleStates[0], moduleStates[1], moduleStates[2], moduleStates[3], isPercentOutput, false);

        if (Timer.getFPGATimestamp() - lastPigeonUpdateTime > 0.01) {
            double currentTime = Timer.getFPGATimestamp();
            double deltaTime = (double)(currentTime - prevTime);

            turnVel = (currentPigeonHeading - prevPigeonHeading) / deltaTime;
            
            turnAccel = (turnVel - prevVel) / deltaTime;

            prevPigeonHeading = currentPigeonHeading;
            prevVel = turnVel;
            prevTime = currentTime;
            lastPigeonUpdateTime = Timer.getFPGATimestamp();
        }
    }

    @Override
    public void end(boolean interrupted) {
        drivetrain.stopAllDrive();
    }
}