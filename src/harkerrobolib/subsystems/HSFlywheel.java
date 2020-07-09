package harkerrobolib.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import harkerrobolib.util.Conversions;
import harkerrobolib.util.Conversions.SpeedUnit;
import harkerrobolib.wrappers.HSFalcon;
import harkerrobolib.wrappers.HSMotorController;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatorCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

/**
 * Represents a general Flywheel shooter with a master talon and a follower talon, and optional solenoid.
 * 
 * @author Aimee Wang
 * @author Kunal Jain
 * @author Ada Praun-Petrovic
 * @author Angela Jia
 * @since 7/2/20
 */
public abstract class HSFlywheel<Motor extends HSMotorController> extends SubsystemBase {

    private Motor master;
    private Motor follower;
    private final DoubleSolenoid solenoid;

    private static double WHEEL_DIAMETER;
    private static int TICKS_PER_REVOLUTION;
    private static double GEAR_RATIO;

    public static double STALL_CURRENT = -1;
    public static double STALL_MIN_VELOCITY = -1;

    /**
     * Constructs a flywheel with master and follower motorcontrollers and a solenoid. 
     * Must pass in stall current and stall velocity.
     * 
     * @param shooterMaster master motorcontroller
     * @param shooterFollower follower motorcontroller
     * @param shooterSolenoid solenoid to toggle shooter position
     * @param wheelDiameter flywheel diametter
     * @param ticksPerRevolution conversion for ticks to 1 revolution
     * @param gearRatio gear ratio
     * @param stallCurrent minimum current indicating stall
     * @param stallMinVelocity minimum velocity indicating stall
     */
    public HSFlywheel(Motor shooterMaster, Motor shooterFollower, DoubleSolenoid shooterSolenoid, double wheelDiameter, int ticksPerRevolution, double gearRatio, double stallCurrent, double stallMinVelocity) {
        master = shooterMaster;
        follower = shooterFollower;
        solenoid = shooterSolenoid;
        WHEEL_DIAMETER = wheelDiameter;
        TICKS_PER_REVOLUTION = ticksPerRevolution;
        GEAR_RATIO = gearRatio;
        STALL_CURRENT = stallCurrent;
        STALL_MIN_VELOCITY = stallMinVelocity;
    }
    
    /**
     * Constructs a flywheel with master and follower motorcontrollers and a solenoid. 
     * 
     * @param shooterMaster master motorcontroller
     * @param shooterFollower follower motorcontroller
     * @param shooterSolenoid solenoid to toggle shooter position
     * @param wheelDiameter flywheel diametter
     * @param ticksPerRevolution conversion for ticks to 1 revolution
     * @param gearRatio gear ratio
     */
    public HSFlywheel(Motor shooterMaster, Motor shooterFollower, DoubleSolenoid solenoid, double wheelDiameter, int ticksPerRevolution, double gearRatio) {
        this(shooterMaster, shooterFollower, solenoid, wheelDiameter, ticksPerRevolution, gearRatio, -1, -1);
    }
    
    /**
     * Constructs a flywheel with master and follower motorcontrollers, and no solenoid.
     * 
     * @param shooterMaster master motorcontroller
     * @param shooterFollower follower motorcontroller
     * @param wheelDiameter flywheel diametter
     * @param ticksPerRevolution conversion for ticks to 1 revolution
     * @param gearRatio gear ratio
     */
    public HSFlywheel(Motor shooterMaster, Motor shooterFollower, double wheelDiameter, int ticksPerRevolution, double gearRatio) {
        this(shooterMaster, shooterFollower, null, wheelDiameter, ticksPerRevolution, gearRatio, -1, -1);
    }

    /**
     * Sets up the master and follower motorcontrollers by resetting defaults, setting master's sensor phase, setting inverts,
     * setting up velocity PID, set voltage compensation.
     * 
     * @param sensorPhase
     * @param masterInvert
     * @param followerInvert
     * @param velocitySlot
     * @param velocityPIDConstants
     * @param voltageComp
     * @param peakCurrent peak current for the current limiting
     * @param sustainedCurrent sustained threshold current for current limiting
     * @param peakTime maximum time for peak current to be in effect (seconds)
     */
    public void setupFlywheel(boolean sensorPhase, boolean masterInvert, boolean followerInvert, int velocitySlot, double[] velocityPIDConstants, double voltageComp, double peakCurrent, double sustainedCurrent, double peakTime) {

        master.configFactoryDefault();
        follower.configFactoryDefault();

        follower.follow(master);

        setupInverts(masterInvert, followerInvert); 

        master.setSensorPhase(sensorPhase);

        setupNeutralMode(NeutralMode.Coast);

        setupVoltageComp(voltageComp);
        
        setupCurrentLimiting(peakCurrent, sustainedCurrent, peakTime);

        setUpVelocityPID(velocitySlot, velocityPIDConstants);
    }

    /**
     * Sets up the master and follower falcons by resetting defaults, setting master's sensor phase, setting inverts,
     * setting up velocity PID, set voltage compensation.
     * 
     * @param sensorPhase
     * @param masterInvert Clockwise or counterclockwise
     * @param followerInvert Clockwise or counterclockwise
     * @param velocitySlot
     * @param velocityPIDConstants
     * @param voltageComp
     * @param peakCurrent peak current for the current limiting
     * @param sustainedCurrent sustained threshold current for current limiting
     * @param peakTime maximum time for peak current to be in effect (seconds)
     */
    public void setupFlywheel(boolean sensorPhase, TalonFXInvertType masterInvert, TalonFXInvertType followerInvert, int velocitySlot, double[] velocityPIDConstants, double voltageComp, double peakCurrent, double sustainedCurrent, double peakTime) {

        master.configFactoryDefault();
        follower.configFactoryDefault();

        follower.follow(master);

        setupInverts(masterInvert, followerInvert); 

        master.setSensorPhase(sensorPhase);

        setupNeutralMode(NeutralMode.Coast);

        setupVoltageComp(voltageComp);
        
        setupCurrentLimiting(peakCurrent, sustainedCurrent, peakTime);
        
        setUpVelocityPID(velocitySlot, velocityPIDConstants);
    }

    public void setupVoltageComp(double voltageComp){
        master.configVoltageCompSaturation(voltageComp);
        master.enableVoltageCompensation(true);
    }

    /**
     * Sets up inverts for the two motors.
     * 
     * @param masterInvert the invert for the master motor
     * @param followerInvert the invert for the follower motor
     */
    public void setupInverts(boolean masterInvert, boolean followerInvert){
        master.setInverted(masterInvert);
        follower.setInverted(followerInvert);
    }

    /**
     * Sets up inverts for two falcons.
     * 
     * @param masterInvert the invert for the master falcon
     * @param followerInvert the invert for the follower falcon
     */
    public void setupInverts(TalonFXInvertType masterInvert, TalonFXInvertType followerInvert){
        master.setInverted(masterInvert.toInvertType());
        follower.setInverted(followerInvert.toInvertType());
    }

    /**
     * Sets up current limiting for the motors.
     * 
     * @param peakCurrent peak current for the current limiting
     * @param sustainedCurrent sustained threshold current for current limiting
     * @param peakTime maximum time for peak current to be in effect (seconds)
     */
    public void setupCurrentLimiting(double peakCurrent, double sustainedCurrent, double peakTime){
        master.configGetStatorCurrentLimit(new StatorCurrentLimitConfiguration(true, peakCurrent, sustainedCurrent, peakTime));
    }

    public void setupNeutralMode(NeutralMode mode){
        master.setNeutralMode(mode);
        follower.setNeutralMode(mode);
    }

    /**
     * Toggles the solenoid between forward and reverse state.
     */
    public void toggleSolenoid() {
        if (solenoid != null) {
            solenoid.set((solenoid.get() == DoubleSolenoid.Value.kForward) ? Value.kReverse : Value.kForward);
        }
    }

    /**
     * Sets the shooter to be spun at the inputed percent output.
     * 
     * @param percentOutput the desired percent output
     */
    public void spinShooterPercentOutput(double percentOutput) {

        if(percentOutput == 0)
            master.set(ControlMode.Disabled, 0);
        else
            master.set(ControlMode.PercentOutput, percentOutput);

    }

    /**
     * Spins the shooter at the desired velocity (f/s).
     * 
     * @param velocity velocity in feet per second
     */
    public void spinShooterVelocity(double velocity) {
    
        double velocityTicks = Conversions.convertSpeed(SpeedUnit.FEET_PER_SECOND, velocity * GEAR_RATIO, SpeedUnit.ENCODER_UNITS, WHEEL_DIAMETER, TICKS_PER_REVOLUTION);
        master.set(ControlMode.Velocity, velocityTicks);
    }

    /**
     * Sets up velocity PID constants with a given slot.
     * 
     * @param velocitySlot slot for the velocity PID
     * @param veloccityPIDConstants the constants for the PID in the order
     *                              0: kF, 1: kP, 2: kI, 3: kD, and optionally, 4: Izone
     */
    public void setUpVelocityPID(int velocitySlot, double[] velocityPIDConstants) {
        master.config_kF(velocitySlot, velocityPIDConstants[0]);
        master.config_kP(velocitySlot, velocityPIDConstants[1]);
        master.config_kI(velocitySlot, velocityPIDConstants[2]);
        master.config_kD(velocitySlot, velocityPIDConstants[3]);

        if (velocityPIDConstants.length > 4)
            master.config_IntegralZone(velocitySlot, (int) velocityPIDConstants[4]);
    }

    public boolean checkStalling() throws Exception {
        if(STALL_CURRENT != -1 && STALL_MIN_VELOCITY != -1)
            return (master.getStatorCurrent() > STALL_CURRENT && master.getSelectedSensorVelocity() < STALL_MIN_VELOCITY);
        else {
            throw new Exception("use checkStalling with params, constants not set");
        }
    }

    /**
     * Checks if the motor is stalling. 
     * 
     * @param stallCurrent minimum current indicating stall
     * @param stallMinVelocity minimum velocity indicating stall
     * @return true if motor is stalling;otherwise
     *         false
     */
    public boolean checkStalling(double stallCurrent, double stallMinVelocity){
        return (master.getStatorCurrent() > stallCurrent && master.getSelectedSensorVelocity() < stallMinVelocity);
    }

    public Motor getMaster() {
        return master;
    }

    public Motor getFollower() {

        return follower;
    }

    public DoubleSolenoid getSolenoid() {
        return solenoid;
    }

    @Override
    public void periodic() {
 
    }
    
}