package harkerrobolib.util;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatorCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.kinematics.SwerveModuleState;

import harkerrobolib.util.Conversions;
import harkerrobolib.util.Conversions.SpeedUnit;
import harkerrobolib.wrappers.HSTalon;

import com.ctre.phoenix.motorcontrol.ControlMode;

/**
 * A swerve module on the drivetrain.
 * A swerve module contains one Falcon 500 to rotate the wheel and one TalonSRX/775pro combo to angle the wheel.
 * 
 * @author Jatin Kohli
 * @author Shahzeb Lakhani
 * @author Angela Jia
 * @author Anirudh Kotamraju
 * @author Chirag Kaushik
 * @author Arjun Dixit
 * @author Aimee Wang
 * 
 * @since 01/13/20
 */
public class SwerveModule {
    public static final int ENCODER_TICKS = 4096;

    //Voltage/Current Constants
    private static final double VOLTAGE_COMP = 10;

    private static final int DRIVE_CURRENT_CONTINUOUS = 40;
    private static final int DRIVE_CURRENT_PEAK = 60;
    private static final int DRIVE_CURRENT_PEAK_DUR = 400;
    private static final int ANGLE_CURRENT_CONTINUOUS = 15;
    private static final int ANGLE_CURRENT_PEAK = 20;
    private static final int CURRENT_PEAK_DUR = 20;

    private static final int DRIVE_TICKS_PER_REV = 2048;

    private static final double EPSILON_OUTPUT = 1e-4;

    // Motor inversions
    private final TalonFXInvertType DRIVE_INVERTED;
    private final boolean ANGLE_INVERTED;

    private final boolean DRIVE_SENSOR_PHASE;
    private final boolean ANGLE_SENSOR_PHASE;
    
    private TalonFX driveMotor;
    private HSTalon angleMotor;

    private final double WHEEL_DIAMETER;
    private final double GEAR_RATIO; //Gear ratio from drive wheel to drive encoder

    /**
     * Creates a SwerveModule object
     * 
     * @param driveId The CAN id of the drive motor controller.
     * @param invertDrive Whether or not to invert the output of the drive motor controller.
     * @param driveSensorPhase Whether or not to invert the sensor phase of the drive motor controller's encoder.
     * @param angleId The CAN id of the angle motor controller.
     * @param invertAngle Whether or not to invert the output of the angle motor controller.
     * @param angleSensorPhase Whether or not to invert the sensor phase of the angle motor controller's encoder.
     * @param wheelDiameter The wheel diameter (in inches) of the drive wheels
     * @param gearRatio The gear ratio from the drive encoder to the drive wheel (6 for a 6:1, where 6 revs of the encoder mean 1 rev of the drive wheel)
     */
    public SwerveModule(int driveId, TalonFXInvertType invertDrive, boolean driveSensorPhase, int angleId, boolean invertAngle, boolean angleSensorPhase, double wheelDiameter, double gearRatio) {
        driveMotor = new TalonFX(driveId);
        angleMotor = new HSTalon(angleId);

        DRIVE_INVERTED = invertDrive;
        ANGLE_INVERTED = invertAngle;
        
        DRIVE_SENSOR_PHASE = driveSensorPhase;
        ANGLE_SENSOR_PHASE = angleSensorPhase;
        
        WHEEL_DIAMETER = wheelDiameter;
        GEAR_RATIO = gearRatio;

        driveFalconInit(driveMotor);
        angleTalonInit(angleMotor);
    }
    
    /**
     * Resets the drive motor controller to its factory default and configures all relevant settings.
     * 
     * @param falcon The drive motor controller (Falcon 500) of this SwerveModule.
     */
    public void driveFalconInit(TalonFX falcon) {
        falcon.configFactoryDefault();

        falcon.configSelectedFeedbackSensor(TalonFXFeedbackDevice.IntegratedSensor, Constants.PID_PRIMARY, Constants.DEFAULT_TIMEOUT);

        falcon.setNeutralMode(NeutralMode.Brake);

        falcon.setInverted(DRIVE_INVERTED);
        falcon.setSensorPhase(DRIVE_SENSOR_PHASE);
        
        falcon.configForwardSoftLimitEnable(false);
        falcon.configReverseSoftLimitEnable(false);
        falcon.overrideLimitSwitchesEnable(false);

        falcon.setSelectedSensorPosition(0);
        
        falcon.configStatorCurrentLimit(new StatorCurrentLimitConfiguration(true, DRIVE_CURRENT_CONTINUOUS, DRIVE_CURRENT_PEAK, DRIVE_CURRENT_PEAK_DUR));

        falcon.configVoltageCompSaturation(VOLTAGE_COMP);
        falcon.enableVoltageCompensation(true);
    }

    /**
     * Resets the angle motor controller to its factory default and configures all relevant settings.
     * 
     * @param falcon The angle motor controller (TalonSRX) of this SwerveModule.
     */
    public void angleTalonInit(HSTalon talon) {
        talon.configFactoryDefault();

        talon.setNeutralMode(NeutralMode.Brake);

        talon.setInverted(ANGLE_INVERTED);
        talon.setSensorPhase(ANGLE_SENSOR_PHASE);

        talon.configForwardSoftLimitEnable(false);
        talon.configReverseSoftLimitEnable(false);
        talon.overrideLimitSwitchesEnable(false);

        talon.configContinuousCurrentLimit(ANGLE_CURRENT_CONTINUOUS);
        talon.configPeakCurrentLimit(ANGLE_CURRENT_PEAK);
        talon.configPeakCurrentDuration(CURRENT_PEAK_DUR);
        talon.enableCurrentLimit(true);
        
        talon.configVoltageCompSaturation(VOLTAGE_COMP);
        talon.enableVoltageCompensation(true);
    }
 
    /**
     * Sets the drive output of the swerve module in either percent output or velocity in feet per second.
     * 
     * @param output The output of the swerve module, with units corresponding to isPercentOutput.
     * @param isPercentOutput True if the output's units are a percentage of maximum output, false if the units are meters per second.
     */
    public void setDriveOutput(double output, boolean isPercentOutput) {
        if(isPercentOutput) {
            driveMotor.set(TalonFXControlMode.PercentOutput, output);
        } else {
            driveMotor.set(TalonFXControlMode.Velocity, Conversions.convertSpeed(SpeedUnit.FEET_PER_SECOND, output * Constants.FEET_PER_METER, SpeedUnit.ENCODER_UNITS, WHEEL_DIAMETER, DRIVE_TICKS_PER_REV) * GEAR_RATIO);
        }
    }
    
    /**
     * Sets the module's output to have the desired angle and output. 90 degree optimizations are performed if needed.
     * 
     * @param targetAngle The angle in degrees for the module's angle motor to have. Zero degrees is in the positive x direction.
     * @param output The output for the module's drive motor to have, with units corresponding to isPercentOutput.
     * @param isPercentOutput True if the output's units are a percentage of maximum output, false if the units are meters per second.
     * @param isMotionProfile True if the robot is currently in a motion profile and should not perform 90 degree optimizations, false if otherwise.
     */
    public void setAngleAndDriveVelocity(double targetAngle, double output, boolean isPercentOutput, boolean isMotionProfile) {
        boolean shouldReverse = !isMotionProfile && Math.abs(targetAngle - getAngleDegrees()) > 90;
        
        if (shouldReverse) {
            setDriveOutput(-output, isPercentOutput);
            if (targetAngle - getAngleDegrees() > 90) {
                targetAngle -= 180;
            }
            else {
                targetAngle += 180;
            }
        } else {
            setDriveOutput(output, isPercentOutput);
        }
        
        int targetPos = (int)((targetAngle / 360) * 4096);

        if(output > EPSILON_OUTPUT || isMotionProfile) 
            angleMotor.set(ControlMode.Position, targetPos);
    }

    /**
     * Returns the current angle in degrees
     */
    public double getAngleDegrees() {
        return angleMotor.getSelectedSensorPosition() * 360.0 / SwerveModule.ENCODER_TICKS; //Convert encoder ticks to degrees
    }

    /**
     * Returns the SwerveModule's drive motor (Falcon 500).
     */
    public TalonFX getDriveMotor() {
        return driveMotor;
    }
    
    /**
     * Returns the SwerveModule's angle motor (TalonSRX).
     */
    public HSTalon getAngleMotor() {
        return angleMotor;    
    }            

     /**
      * Returns the current SwerveModuleState of the module, which contains the angle (Rotation2d) and speed (m/s) of the module.
      */
    public SwerveModuleState getState() {
        return new SwerveModuleState(Conversions.convertSpeed(SpeedUnit.ENCODER_UNITS, driveMotor.getSelectedSensorVelocity() / GEAR_RATIO, SpeedUnit.FEET_PER_SECOND, WHEEL_DIAMETER, DRIVE_TICKS_PER_REV) * Constants.METERS_PER_FOOT, 
            Rotation2d.fromDegrees(getAngleDegrees()));
    }

    public static double mod360(double degrees) {
        return (degrees % 360 + 360) % 360;
    }

    /**
     * Gets the error of the angle motor given desired degrees of rotation.
     */
    public double getAngleErrorDegrees(double desiredDegrees) {
        while (getAngleDegrees() - desiredDegrees > 180) {
            desiredDegrees += 360;
        }
        while (getAngleDegrees() - desiredDegrees < -180) {
            desiredDegrees -= 360;
        }

        return getAngleDegrees() - desiredDegrees;
    }
}