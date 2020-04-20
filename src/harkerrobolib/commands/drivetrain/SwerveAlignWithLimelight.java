package harkerrobolib.commands.drivetrain;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.MedianFilter;
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
 * @since April 19, 2020
 */
public class SwerveAlignWithLimelight extends CommandBase {
    private double outputMultiplier;

    private PIDController txController;
    
    private static final long spinTime = 100;
    private HSSwerveDrivetrain drivetrain;
    private long time;
    private int driveVelocitySlot;
    private int anglePositionSlot;
    private double driveRampRate;
    private double angleRampRate;
    private double maxDriveVelocity;
    private double maxRotationVelocity;
    private HSGamepad driverGamepad;

    public SwerveAlignWithLimelight(HSSwerveDrivetrain drivetrain, PIDController txController, int driveVelocitySlot, int anglePositionSlot, 
        double driveRampRate, double angleRampRate, double maxRotationVelocity, HSGamepad driverGamepad, double maxDriveVelocity, double outputMultiplier) {
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

        this.outputMultiplier = outputMultiplier;
    }
    
    @Override
    public void initialize() {
        drivetrain.applyToAllDrive((falcon) -> falcon.selectProfileSlot(driveVelocitySlot, Constants.PID_PRIMARY));
        drivetrain.applyToAllDrive((falcon) -> falcon.configClosedloopRamp(driveRampRate));
        
        drivetrain.applyToAllAngle((talon) -> talon.selectProfileSlot(anglePositionSlot, Constants.PID_PRIMARY));
        drivetrain.applyToAllAngle((talon) -> talon.configClosedloopRamp(angleRampRate));
        
        Limelight.setCamModeVision();
        Limelight.setLEDS(true);

        txController.setSetpoint(0);
        time = 0;
    }
    
    @Override
    public void execute() {
        Limelight.setLEDS(true);

        double turn = txController.calculate(Limelight.getTx(), txController.getSetpoint()) * maxRotationVelocity * (DriverStation.getInstance().isAutonomous() ? -1 : 1);

        double translateX = MathUtil.mapJoystickOutput(driverGamepad.getLeftX(), Constants.JOYSTICK_DEADBAND);
        double translateY = MathUtil.mapJoystickOutput(driverGamepad.getLeftY(), Constants.JOYSTICK_DEADBAND);
        
        double turnManual = -3 * MathUtil.mapJoystickOutput(driverGamepad.getRightX(), Constants.JOYSTICK_DEADBAND);
        
        translateX *= outputMultiplier * maxDriveVelocity;
        translateY *= outputMultiplier * maxDriveVelocity;
        
        ChassisSpeeds speeds = ChassisSpeeds.fromFieldRelativeSpeeds(
            translateX, translateY, Limelight.isTargetVisible() ? turn : turnManual, Rotation2d.fromDegrees(drivetrain.getPigeon().getFusedHeading())
        );

        SwerveModuleState[] moduleStates = drivetrain.getKinematics().toSwerveModuleStates(speeds);
        
        if (System.currentTimeMillis() - time > spinTime)
            drivetrain.setDrivetrainVelocity(moduleStates[0], moduleStates[1], moduleStates[2], moduleStates[3], false, false);
    }

    @Override
    public void end(boolean interrupted) {
        txController.reset();
        drivetrain.stopAllDrive();
        Limelight.setLEDS(false);
    }
}