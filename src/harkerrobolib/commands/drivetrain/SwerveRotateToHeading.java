package harkerrobolib.commands.drivetrain;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj2.command.CommandBase;
import harkerrobolib.subsystems.HSSwerveDrivetrain;

/**
 * Rotates the robot to a given heading.
 * 
 * @author Shahzeb Lakhani
 * @author Anirudh Kotamraju
 * @author Jatin Kohli
 * 
 * @version 5/3/2020
 */
public class SwerveRotateToHeading extends CommandBase {
    private double heading;
    private double turnMagnitude;
    private PIDController headingController;
    private HSSwerveDrivetrain drivetrain;
    private double allowableError;

    /**
     * Constructor for SwerveRotateToHeading
     * 
     * @param heading The angle in degrees to rotate to
     * @param drivetrain The drivetrain instance
     * @param headingController The PID controller to calculate the turn magnitude
     * @param allowableError The error threshold (in degrees) to end the command
     */
    public SwerveRotateToHeading(double heading, HSSwerveDrivetrain drivetrain, PIDController headingController, double allowableError) {
        this.drivetrain = drivetrain;
        this.heading = heading;
        this.headingController = headingController;
        this.allowableError = allowableError;
    }

    @Override
    public void initialize() {
        while (drivetrain.getPigeon().getFusedHeading() - heading > 180)
            heading += 360;
        while (drivetrain.getPigeon().getFusedHeading() - heading <- 180)
            heading -= 360;
        turnMagnitude = 0;
    }

    @Override
    public void execute() {
        turnMagnitude = headingController.calculate(drivetrain.getPigeon().getFusedHeading(), heading);
        ChassisSpeeds speeds = ChassisSpeeds.fromFieldRelativeSpeeds(
            0, 0, turnMagnitude, Rotation2d.fromDegrees(drivetrain.getPigeon().getFusedHeading())
        );

        // Now use this in our kinematics
        SwerveModuleState[] moduleStates = drivetrain.getKinematics().toSwerveModuleStates(speeds);

        drivetrain.setDrivetrainVelocity(moduleStates[0], moduleStates[1], moduleStates[2], moduleStates[3], false, false);
    }

    @Override
    public boolean isFinished() {
        return Math.abs(drivetrain.getPigeon().getFusedHeading() - heading) <= allowableError;
    }

    @Override
    public void end(boolean interrupted) {
        drivetrain.stopAllDrive();
    }
}