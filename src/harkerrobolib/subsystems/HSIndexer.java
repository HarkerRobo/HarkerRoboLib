package harkerrobolib.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatorCurrentLimitConfiguration;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import harkerrobolib.wrappers.HSMotorController;

/**
 * Represents an indexer subsystem on the robot.
 * 
 * @author Kunal Jain
 * @author Ada Praun-Petrovic
 * @author Angela Jia
 * 
 * @since 7/14/20
 */
public abstract class HSIndexer<Motor extends HSMotorController> {
    public Motor agitator;
    public Motor spine;
    public DoubleSolenoid solenoid;
    
    public HSIndexer(Motor agitator, Motor spine, DoubleSolenoid solenoid) {
        this.agitator = agitator;
        this.spine = spine;
        this.solenoid = solenoid;
    }

    public Motor getAgitator() {
        return agitator;
    }
    
    public Motor getSpine() {
        return spine;
    }

    public DoubleSolenoid getSolenoid() {
        return solenoid;
    }

    public void resetMotors(){
        agitator.configFactoryDefault();
        spine.configFactoryDefault();
    }

    public void setupVelocityPID(int velocityPIDSlot, double[] pidConstants){
        spine.config_kF(velocityPIDSlot, pidConstants[0]);
        spine.config_kP(velocityPIDSlot, pidConstants[1]);
        spine.config_kI(velocityPIDSlot, pidConstants[2]);
        spine.config_kD(velocityPIDSlot, pidConstants[3]);
        if(pidConstants.length>4)
            spine.config_IntegralZone(velocityPIDSlot, (int)pidConstants[4]);
    }

    public void setupCurrentLimiting(double peakCurrent, double sustainedCurrent, double peakTime){
        agitator.configStatorCurrentLimit(new StatorCurrentLimitConfiguration(true, peakCurrent, sustainedCurrent, peakTime));
        spine.configStatorCurrentLimit(new StatorCurrentLimitConfiguration(true, peakCurrent, sustainedCurrent, peakTime));
    }

    public void setupVoltageComp(double maxVoltage) {
        agitator.configVoltageCompSaturation(maxVoltage);
        spine.configVoltageCompSaturation(maxVoltage);
        agitator.enableVoltageCompensation(true);
        spine.enableVoltageCompensation(true);
    }

    public void toggleSolenoid(DoubleSolenoid solenoid) {
        solenoid.set( (solenoid.get() == Value.kForward) ? Value.kReverse : Value.kForward);
    }

    public void setupNeutralMode(NeutralMode mode)
    {
        spine.setNeutralMode(mode);
        agitator.setNeutralMode(mode);
    }

    public void setupInverts(boolean agitatorInvert, boolean spineInvert) 
    {
        spine.setInverted(spineInvert);
        agitator.setInverted(agitatorInvert);
    }

    public void setupSensorPhase(boolean sensorPhase){
        spine.setSensorPhase(sensorPhase);
        spine.setSelectedSensorPosition(0);
    }
    
    public void setUpMotorControllers(int velocityPIDSlot, double[] pidConstants,double peakCurrent, double sustainedCurrent, double peakTime,double maxVoltage,boolean agitatorInvert, boolean spineInvert, boolean sensorPhase) {
        resetMotors();
        setupNeutralMode(NeutralMode.Brake);
        setupVelocityPID(velocityPIDSlot, pidConstants);
        setupCurrentLimiting(peakCurrent, sustainedCurrent, peakTime);
        setupVoltageComp(maxVoltage);
        setupInverts(agitatorInvert, spineInvert);
        setupSensorPhase(sensorPhase);
    }
}