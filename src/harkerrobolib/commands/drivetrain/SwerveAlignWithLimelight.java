package harkerrobolib.commands.drivetrain;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.controller.PIDController;

import edu.wpi.first.wpilibj.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.CommandBase;
import harkerrobolib.subsystems.HSSwerveDrivetrain;
import harkerrobolib.util.Constants;
import harkerrobolib.util.Limelight;
import harkerrobolib.util.MathUtil;
import harkerrobolib.wrappers.HSGamepad;

/**
 * Aligns the Robot with the target, using the Limelight's tX to turn towards the target
 * and its thor to get close enough to the target
 * 
 * @author Shahzeb Lakhani
 * @author Angela Jia
 * @author Jatin Kohli
 * @author Anirudh Kotamraju
 * @version 5/3/20
 */
public class SwerveAlignWithLimelight extends CommandBase {
    private double outputMultiplier;

    private PIDController txController;
    
    private HSSwerveDrivetrain drivetrain;
    private int driveVelocitySlot;
    private int anglePositionSlot;
    private double driveRampRate;
    private double angleRampRate;
    private double maxDriveVelocity;
    private double maxRotationVelocity;
    private HSGamepad driverGamepad;
    private double txSetpoint;
    private double rotationMagnitude;

    /**
     * Constructor for SwerveAlignWithLimelight
     * 
     * @param drivetrain The drivetrain instance.
     * @param txController The PID Controller to keep track of hte Limelight tX (horizontal offset).
     * @param driveVelocitySlot The drive motor velocity slot.
     * @param anglePositionSlot The angle motor position slot.
     * @param driveRampRate The ramp rate for the drive motor.
     * @param angleRampRate The ramp rate for the angle motor.
     * @param maxRotationVelocity The maximum velocity to spin the drive motors at.
     * @param driverGamepad The driver gamepad/controller.
     * @param maxDriveVelocity The max drive velocity of the drivetrain.
     * @param outputMultiplier The value (between 0 and 1) to multiply the joystick input by before calculating translation output.
     * @param txSetpoint The endpoint of the tx alignment.
     * @param rotationMagnitude The manual rotation speed of the robot when no target is in sight.
     */
    public SwerveAlignWithLimelight(HSSwerveDrivetrain drivetrain, PIDController txController, int driveVelocitySlot, int anglePositionSlot, 
        double driveRampRate, double angleRampRate, double maxRotationVelocity, HSGamepad driverGamepad, double maxDriveVelocity, double outputMultiplier,
        double txSetpoint, double rotationMagnitude) {
        addRequirements(drivetrain);

        this.txController = txController;
        this.drivetrain = drivetrain;
        this.driveVelocitySlot = driveVelocitySlot;
        this.driveRampRate = driveRampRate;
        this.anglePositionSlot = anglePositionSlot;
        this.angleRampRate = angleRampRate;
        this.maxRotationVelocity = maxRotationVelocity;

        this.driverGamepad = driverGamepad;
        this.maxDriveVelocity = maxDriveVelocity;
        this.rotationMagnitude = rotationMagnitude;
        this.outputMultiplier = outputMultiplier;

        this.txSetpoint = txSetpoint;
    }
    
    @Override
    public void initialize() {
        drivetrain.applyToAllDrive((falcon) -> falcon.selectProfileSlot(driveVelocitySlot, Constants.PID_PRIMARY));
        drivetrain.applyToAllDrive((falcon) -> falcon.configClosedloopRamp(driveRampRate));
        
        drivetrain.applyToAllAngle((talon) -> talon.selectProfileSlot(anglePositionSlot, Constants.PID_PRIMARY));
        drivetrain.applyToAllAngle((talon) -> talon.configClosedloopRamp(angleRampRate));
        
        Limelight.setCamModeVision();
        Limelight.setLEDS(true);

        txController.setSetpoint(txSetpoint);
    }
    
    @Override
    public void execute() {
        Limelight.setLEDS(true);

        double turn = txController.calculate(Limelight.getTx(), txController.getSetpoint()) * maxRotationVelocity * (DriverStation.getInstance().isAutonomous() ? -1 : 1);

        double translateX = MathUtil.mapJoystickOutput(driverGamepad.getLeftX(), Constants.JOYSTICK_DEADBAND);
        double translateY = MathUtil.mapJoystickOutput(driverGamepad.getLeftY(), Constants.JOYSTICK_DEADBAND);
        
        double turnManual = -1 * rotationMagnitude * MathUtil.mapJoystickOutput(driverGamepad.getRightX(), Constants.JOYSTICK_DEADBAND);
        
        translateX *= outputMultiplier * maxDriveVelocity;
        translateY *= outputMultiplier * maxDriveVelocity;
        
        ChassisSpeeds speeds = ChassisSpeeds.fromFieldRelativeSpeeds(
            translateX, translateY, Limelight.isTargetVisible() ? turn : turnManual, Rotation2d.fromDegrees(drivetrain.getPigeon().getFusedHeading())
        );

        SwerveModuleState[] moduleStates = drivetrain.getKinematics().toSwerveModuleStates(speeds);
        
        drivetrain.setDrivetrainVelocity(moduleStates[0], moduleStates[1], moduleStates[2], moduleStates[3], false, false);
    }

    @Override
    public void end(boolean interrupted) {
        txController.reset();
        drivetrain.stopAllDrive();
        Limelight.setLEDS(false);
    }
}