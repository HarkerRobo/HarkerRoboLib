package harkerrobolib.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatorCurrentLimitConfiguration;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import harkerrobolib.util.Gains;
import harkerrobolib.wrappers.HSMotorController;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import harkerrobolib.wrappers.HSTalon;

/**
 * Represents an indexer subsystem on the robot.
 * 
 * @author Kunal Jain
 * @author Ada Praun-Petrovic
 * @author Angela Jia
 * 
 * @since 7/14/20
 */
public abstract class HSIndexer<Motor extends HSMotorController> extends SubsystemBase {
    public VictorSPX agitator;
    public Motor spine;
    public DoubleSolenoid solenoid;
    
    public HSIndexer(Motor spine, VictorSPX agitator, DoubleSolenoid solenoid) {
        this.spine = spine;
        this.agitator = agitator;
        this.solenoid = solenoid;
    }

    public VictorSPX getAgitator() {
        return agitator;
    }
    
    public Motor getSpine() {
        return spine;
    }

    public DoubleSolenoid getSolenoid() {
        return solenoid;
    }

    public void resetMotors() {
        agitator.configFactoryDefault();
        spine.configFactoryDefault();
    }

    public void setupVelocityPID(int velocityPIDSlot, Gains constants){
        spine.configClosedLoopConstants(velocityPIDSlot, constants);
    }

    public void setupCurrentLimiting(StatorCurrentLimitConfiguration currentConfig){
        spine.configStatorCurrentLimit(currentConfig);
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
    
    public void setUpMotorControllers(int velocityPIDSlot, Gains constants, 
                                    StatorCurrentLimitConfiguration currentConfig, 
                                    double maxVoltage, boolean agitatorInvert, 
                                    boolean spineInvert, boolean sensorPhase) { 
        resetMotors();
        setupNeutralMode(NeutralMode.Brake);
        setupVelocityPID(velocityPIDSlot, constants);
        setupCurrentLimiting(currentConfig);
        setupVoltageComp(maxVoltage);
        setupInverts(agitatorInvert, spineInvert);
        setupSensorPhase(sensorPhase);
    }

    /**
     * Checks if the motor is stalling. 
     * 
     * @param stallCurrent minimum current indicating stall
     * @param stallMinVelocity minimum velocity indicating stall
     * @return true if motor is stalling;otherwise
     *         false
     */
    public boolean isStalling(double stallCurrent, double stallMinVelocity){
        return spine.isStalling(stallCurrent, stallMinVelocity);
    }
}