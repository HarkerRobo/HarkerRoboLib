package harkerrobolib.subsystems;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModuleState;      
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import harkerrobolib.util.SwerveModule;    
import harkerrobolib.wrappers.HSPigeon;
import harkerrobolib.wrappers.HSTalon;

import java.util.function.Consumer;

import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.sensors.PigeonIMU_StatusFrame;

/**
 * Simulates the drivetrain subsystem on the robot. A Swerve Drivetrain contains
 * four SwerveModules.
 * 
 * Acronyms: TL: Top Left TR: Top Right BL: Back Left BR: Back Right
 * 
 * @author Shahzeb Lakhani
 * @author Angela Jia
 * @author Jatin Kohli
 * @author Anirudh Kotamraju
 * @author Chirag Kaushik
 * @author Arjun Dixit
 * @author Ada Praun-Petrovic
 * 
 * @since 11/1/19
 */
public abstract class HSSwerveDrivetrain extends SubsystemBase {
    private SwerveModule topLeft;
    private SwerveModule topRight;
    private SwerveModule backLeft;
    private SwerveModule backRight;

    private HSPigeon pigeon;
    private SwerveDriveOdometry odometry;
    private SwerveDriveKinematics kinematics;

    /**
     * Default constructor for Drivetrain
     * 
     * Initializes SwerveModules with inverts for drive and angle motors 
     * and sensor phases, resets the encoders to the offset position and 
     * zeroes the angle motors so that all modules are facing forward at
     * 90 degrees, zeroes the pigeon to 90 degrees, and initializes 
     * SwerveDriveKinematics and Odometry.
     */
    public HSSwerveDrivetrain(SwerveModule[] modules, HSPigeon pigeon, double chassisWidth, double chassisLength) {
        topLeft = modules[0];
        topRight = modules[1];
        backLeft = modules[2];
        backRight = modules[3];
        this.pigeon = pigeon;

        applyToAllDrive((motor) -> motor.setSelectedSensorPosition(0));

        pigeon.configFactoryDefault();
        pigeon.zero();
        pigeon.setFusedHeading(0);
        pigeon.setStatusFramePeriod(PigeonIMU_StatusFrame.BiasedStatus_2_Gyro, 5);

        kinematics = new SwerveDriveKinematics(
                                    new Translation2d(-chassisWidth / 2, chassisLength / 2),
                                    new Translation2d(chassisWidth / 2, chassisLength / 2),
                                    new Translation2d(-chassisWidth / 2, -chassisLength / 2),
                                    new Translation2d(chassisWidth / 2, -chassisLength / 2)
                                    );

        odometry = new SwerveDriveOdometry(kinematics, new Rotation2d(pigeon.getFusedHeading()), new Pose2d(new Translation2d(), Rotation2d.fromDegrees(90)));
        
        Pose2d initialPose = new Pose2d(new Translation2d(), 
                Rotation2d.fromDegrees(pigeon.getFusedHeading()));

        Rotation2d currentRot = Rotation2d.fromDegrees(pigeon.getFusedHeading());

        odometry.resetPosition(initialPose, currentRot);
    }

    public static SwerveModule[] initSwerveModules(int[] driveIds, int[] angleIds, int[] offsets, 
        TalonFXInvertType[] driveInverts, boolean[] driveSensorPhases, boolean[] angleInverts, 
        boolean[] angleSensorPhases, double wheelDiameter, double gearRatio) {
        SwerveModule[] modules = new SwerveModule[offsets.length];

        for(int i = 0; i < offsets.length; i++) {
            modules[i] = new SwerveModule(
                                driveIds[i], driveInverts[i], driveSensorPhases[i], 
                                angleIds[i], angleInverts[i], angleSensorPhases[i], 
                                wheelDiameter, gearRatio, offsets[i]);
        }
        return modules;
    }

    /**
     * Updates odometry periodically based on the pigeon heading and
     * the states of the SwerveModules.
     */
    @Override
    public void periodic() {
        odometry.update(Rotation2d.fromDegrees(pigeon.getFusedHeading()),
                topLeft.getState(), topRight.getState(),
                backLeft.getState(), backRight.getState());
    }

    /**
     * Returns the currently-estimated pose of the robot based on odometry.
     */
    public Pose2d getPose() {
        return odometry.getPoseMeters();
    }

    /**
     * Binds PID slots and ramp rate for angle motors.
     */
    public void setupPositionPID(int slot, double kP, double kI, double kD, double rampRate) {
        applyToAllAngle((angleMotor) -> angleMotor.config_kP(slot, kP));
        applyToAllAngle((angleMotor) -> angleMotor.config_kI(slot, kI));
        applyToAllAngle((angleMotor) -> angleMotor.config_kD(slot, kD));
        applyToAllAngle((angleMotor) -> angleMotor.configClosedloopRamp(rampRate));
    }

    /**
     * Binds PID slots and ramp rate for drive motors.
     */
    public void setupVelocityPID(int slot, double kF, double kP, double kI, double kD, double rampRate) {
        applyToAllDrive((driveMotor) -> driveMotor.config_kF(slot, kF));
        applyToAllDrive((driveMotor) -> driveMotor.config_kP(slot, kP));
        applyToAllDrive((driveMotor) -> driveMotor.config_kI(slot, kI));
        applyToAllDrive((driveMotor) -> driveMotor.config_kD(slot, kD));
        applyToAllDrive((driveMotor) -> driveMotor.configClosedloopRamp(rampRate));
    }

    /**
     * Sets the velocity and angle of the drivetrain motors based on the properties 
     * of the passed SwerveModuleStates, with feedforward set to 0, percent output 
     * as false, and motion profile as true.
     */
    public void setDrivetrainModuleStates(SwerveModuleState[] states) {
        setDrivetrainVelocity(states[0], states[1], states[2], states[3], false, true);
    }

    /**
     * Sets the output of the drivetrain based on desired output vectors for each
     * swerve module
     */
    public void setDrivetrainVelocity(SwerveModuleState tl, SwerveModuleState tr, SwerveModuleState bl,
            SwerveModuleState br, boolean isPercentOutput, boolean isMotionProfile) {
        double tlOutput = tl.speedMetersPerSecond;
        double trOutput = tr.speedMetersPerSecond;
        double blOutput = bl.speedMetersPerSecond;
        double brOutput = br.speedMetersPerSecond;
        
        setSwerveModuleVelocity(topLeft, tlOutput, convertAngle(topLeft, tl.angle.getDegrees()), isPercentOutput,
                isMotionProfile);
        setSwerveModuleVelocity(topRight, trOutput, convertAngle(topRight, tr.angle.getDegrees()), isPercentOutput,
                isMotionProfile);
        setSwerveModuleVelocity(backLeft, blOutput, convertAngle(backLeft, bl.angle.getDegrees()), isPercentOutput,
                isMotionProfile);
        setSwerveModuleVelocity(backRight, brOutput, convertAngle(backRight, br.angle.getDegrees()), isPercentOutput,
                isMotionProfile);
    }

    public void setSwerveModuleVelocity(SwerveModule module, double output, double angle, boolean isPercentOutput,
            boolean isMotionProfile) {
        
        module.setAngleAndDriveVelocity(angle, output, isPercentOutput, isMotionProfile);
    }

    /**
     * Converts the target angle from the desired Vectors into the actual angle for
     * the motors to hold. Angle modification for Field sensitive drive should have
     * already been handled.
     * 
     * Steps:
     * 
     * 1. Subtract 90 degrees. 0 degrees on the Joysticks and desired Vectors points
     * to the right (positive x axis) while 0 degrees on the robot points forward
     * (positive y axis). The subtraction deals with this offset. 2.
     * Increase/Decrease the targetAngle by 360 degrees until it is within +- 180
     * degrees of the current angle
     * 
     * @return The desired angle after all modifications
     */
    public static double convertAngle(SwerveModule module, double targetAngle) {
        // Step 1

        double currDegrees = module.getAngleDegrees();

        // Step 2
        while (currDegrees - targetAngle > 180) {
            targetAngle += 360;
        }
        while (currDegrees - targetAngle < -180) {
            targetAngle -= 360;
        }

        return targetAngle;
    }

    /**
     * Stops all drive motors while holding the current angle
     */
    public void stopAllDrive() {
        setSwerveModuleVelocity(topLeft, 0, topLeft.getAngleDegrees(), true, false);
        setSwerveModuleVelocity(topRight, 0, topRight.getAngleDegrees(), true, false);
        setSwerveModuleVelocity(backLeft, 0, backLeft.getAngleDegrees(), true, false);
        setSwerveModuleVelocity(backRight, 0, backRight.getAngleDegrees(), true, false);
    }

    /**
     * Calls a method on all swerve modules on the drivetrain. For example:
     * Drivetrain.getInstance().applyToAll((motor) -> motor.doSomething());
     */
    public void applyToAll(Consumer<SwerveModule> consumer) {
        consumer.accept(topLeft);
        consumer.accept(topRight);
        consumer.accept(backLeft);
        consumer.accept(backRight);
    }

    /**
     * Calls a method on the angle motor of each swerve module.
     */
    public void applyToAllAngle(Consumer<HSTalon> consumer) {
        consumer.accept(topLeft.getAngleMotor());
        consumer.accept(topRight.getAngleMotor());
        consumer.accept(backLeft.getAngleMotor());
        consumer.accept(backRight.getAngleMotor());
    }

    /**
     * Calls a method on the drive motor of each swerve module.
     */
    public void applyToAllDrive(Consumer<TalonFX> consumer) {
        consumer.accept(topLeft.getDriveMotor());
        consumer.accept(topRight.getDriveMotor());
        consumer.accept(backLeft.getDriveMotor());
        consumer.accept(backRight.getDriveMotor());
    }

    public SwerveModule getTopLeft() {
        return topLeft;
    }

    public SwerveModule getTopRight() {
        return topRight;
    }

    public SwerveModule getBackLeft() {
        return backLeft;
    }

    public SwerveModule getBackRight() {
        return backRight;
    }

    public HSPigeon getPigeon() {
        return pigeon;
    }

    public SwerveDriveKinematics getKinematics() {
        return kinematics;
    }

    public SwerveDriveOdometry getOdometry() {
        return odometry;
    }
}