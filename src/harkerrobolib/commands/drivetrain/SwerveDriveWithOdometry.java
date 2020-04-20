package harkerrobolib.commands.drivetrain;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.ProfiledPIDController;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import harkerrobolib.subsystems.HSSwerveDrivetrain;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Controls the drivetrain using WPILib's SwerveControllerCommand
 * 
 * @author Jatin Kohli
 * @author Chirag Kaushik
 * @author Shahzeb Lakhani
 * @author Anirudh Kotamraju
 * @author Arjun Dixit
 */
public class SwerveDriveWithOdometry extends HSSwerveDriveOdometry {

    private Trajectory trajectory;

    private final int ALLOWABLE_ERROR = 5;
    private final int TIMEOUT = 400;
    private HSSwerveDrivetrain drivetrain;

    public SwerveDriveWithOdometry(Trajectory trajectory, Rotation2d heading, HSSwerveDrivetrain drivetrain, 
            PIDController xController, PIDController yController, ProfiledPIDController thetaController) {
        super(trajectory,
            heading,
            drivetrain,
            drivetrain::getPose,
            drivetrain.getKinematics(), 
            xController, 
            yController, 
            thetaController,
            drivetrain::setDrivetrainModuleStates,
            drivetrain
        );

        this.drivetrain = drivetrain;
        this.trajectory = trajectory;
    }

    @Override
    public void initialize() {
        //Set to x and y from starting Pose2d of path but keep current rotation value from odometry
        Pose2d initialPose = new Pose2d(trajectory.getInitialPose().getTranslation(), 
                Rotation2d.fromDegrees(drivetrain.getPigeon().getFusedHeading()));

        Rotation2d currentRot = Rotation2d.fromDegrees(drivetrain.getPigeon().getFusedHeading());

        drivetrain.getOdometry().resetPosition(initialPose, currentRot);

        long initialTime = System.currentTimeMillis();
        
        boolean isAtSepoint = false;
        Rotation2d initialRotation = trajectory.getInitialPose().getRotation().minus(Rotation2d.fromDegrees(0));

        //Perhaps add some functionality to rotate robot to the heading as well
        while (System.currentTimeMillis() - initialTime < TIMEOUT && !isAtSepoint) {
            isAtSepoint = Math.abs(drivetrain.getTopLeft().getAngleErrorDegrees(initialRotation.getDegrees())) < ALLOWABLE_ERROR
                && Math.abs(drivetrain.getTopRight().getAngleErrorDegrees(initialRotation.getDegrees())) < ALLOWABLE_ERROR
                && Math.abs(drivetrain.getBackLeft().getAngleErrorDegrees(initialRotation.getDegrees())) < ALLOWABLE_ERROR
                && Math.abs(drivetrain.getBackRight().getAngleErrorDegrees(initialRotation.getDegrees())) < ALLOWABLE_ERROR;

            drivetrain.setDrivetrainVelocity(
                new SwerveModuleState(0, initialRotation), 
                new SwerveModuleState(0, initialRotation), 
                new SwerveModuleState(0, initialRotation), 
                new SwerveModuleState(0, initialRotation), false, true);
        }        

        super.initialize();
    }   
    
    @Override
    public void execute() {
        super.execute();
    }

    @Override
    public boolean isFinished() {
        return super.isFinished();
    }
    
    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);

        drivetrain.stopAllDrive();        
    }
}