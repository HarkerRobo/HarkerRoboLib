package harkerrobolib.commands.drivetrain;

import java.util.function.Consumer;
import java.util.function.Supplier;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.ProfiledPIDController;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.Trajectory.State;
import edu.wpi.first.wpilibj2.command.CommandBase;
import harkerrobolib.subsystems.HSSwerveDrivetrain;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.kinematics.SwerveDriveKinematics;
import edu.wpi.first.wpilibj.kinematics.SwerveModuleState;

/**
 * Controls the drivetrain to follow a Motion Profile Trajectory using Odometry control, 
 * mostly copied from WPILib's SwerveControllerCommand
 * (https://github.com/wpilibsuite/allwpilib/blob/master/wpilibNewCommands/src/main/java/edu/wpi/first/wpilibj2/command/SwerveControllerCommand.java)
 * 
 * @author Jatin Kohli
 * @author Chirag Kaushik
 * @author Shahzeb Lakhani
 * @author Anirudh Kotamraju
 * @author Arjun Dixit
 */
public class SwerveDriveWithOdometry extends CommandBase {
    private final int ALLOWABLE_ERROR = 5;
    private final int TIMEOUT = 400;
    private HSSwerveDrivetrain drivetrain;

    private final Timer timer = new Timer();

    private final Trajectory trajectory;
    private final Rotation2d heading;
    private final Supplier<Pose2d> pose;
    private final SwerveDriveKinematics kinematics;
    private final PIDController xController;
    private final PIDController yController;
    private final ProfiledPIDController thetaController;
    private final Consumer<SwerveModuleState[]> outputModuleStates;

    public SwerveDriveWithOdometry(Trajectory trajectory, Rotation2d heading, HSSwerveDrivetrain drivetrain, 
            PIDController xController, PIDController yController, ProfiledPIDController thetaController) {

        this.trajectory = trajectory;
        this.heading = heading;
        this.drivetrain = drivetrain;

        this.pose = drivetrain::getPose;
        this.kinematics = drivetrain.getKinematics();

        this.xController = xController;
        this.yController = yController;
        this.thetaController = thetaController;

        this.outputModuleStates = drivetrain::setDrivetrainModuleStates;
        addRequirements(drivetrain);
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

        timer.reset();
        timer.start();
    }   
    
    @Override
    public void execute() {
        double curTime = timer.get();

        State desiredState = trajectory.sample(curTime);
        Pose2d desiredPose = desiredState.poseMeters;

        Pose2d poseError = desiredPose.relativeTo(pose.get());

        double targetXVel = xController.calculate(pose.get().getTranslation().getX(),
                desiredPose.getTranslation().getX());

        double targetYVel = yController.calculate(pose.get().getTranslation().getY(),
                desiredPose.getTranslation().getY());

        // The robot will go to the desired rotation of the final pose in the
        // trajectory, not following the poses at individual states.
        double targetAngularVel = thetaController.calculate(Math.toRadians(-drivetrain.getPigeon().getFusedHeading()),
                heading.getRadians());

        double vRef = desiredState.velocityMetersPerSecond;
        targetXVel += vRef * poseError.getRotation().getCos();
        targetYVel += vRef * poseError.getRotation().getSin();

        ChassisSpeeds targetChassisSpeeds = new ChassisSpeeds(targetXVel, targetYVel, targetAngularVel);
        SwerveModuleState[] targetModuleStates = kinematics.toSwerveModuleStates(targetChassisSpeeds);

        outputModuleStates.accept(targetModuleStates);
    }

    @Override
    public boolean isFinished() {
        return timer.hasPeriodPassed(trajectory.getTotalTimeSeconds());
    }
    
    @Override
    public void end(boolean interrupted) {
        timer.stop();
        drivetrain.stopAllDrive();        
    }
}