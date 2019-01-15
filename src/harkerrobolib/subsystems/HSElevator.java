package harkerrobolib.subsystems;

import java.lang.reflect.Method;
import java.util.function.Consumer;

import com.ctre.phoenix.motorcontrol.IMotorController;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj.command.Subsystem;
import harkerrobolib.wrappers.HSTalon;

/**
 * Represents a general Elevator subsystem with a master talon 
 * and variable number of followers.
 * 
 * @author Angela Jia
 * @since 1/12/19
 */
public abstract class HSElevator extends Subsystem {

    private HSTalon elMaster;
    private IMotorController[] victors;
    private double feedForwardGrav; 

    public HSElevator(HSTalon talon, double feedForwardGrav, IMotorController ... victors) {
        elMaster = talon;
        for(int i = 0; i < victors.length; i++)
        {
            this.victors[i] = victors[i];
        }
        this.feedForwardGrav = feedForwardGrav;
    }

    public IMotorController[] getFollowers() {
        return victors;
    }

    public void setCurrentLimit(int peakTime, int peakLimit, int contLimit, int timeout) {
        elMaster.configPeakCurrentLimit(peakLimit, timeout);
        elMaster.configPeakCurrentDuration(peakTime, timeout);
        elMaster.configContinuousCurrentLimit(contLimit, timeout);
    }

    public void setCurrentLimit(int peakTime, int peakLimit, int contLimit) {
        setCurrentLimit(peakTime, peakLimit, contLimit, elMaster.getDefaultTimeout());
    }

    public void applyToAll(Consumer<IMotorController> consumer) {
        consumer.accept(elMaster);
        for(int i = 0; i < victors.length; i++) {
            consumer.accept(victors[i]);
        }
    }

    public void applyToFollowers(Consumer<IMotorController> consumer) {
        for(int i = 0; i < victors.length; i++) {
            consumer.accept(victors[i]);
        }
    }

    public void setNeutralMode(NeutralMode nMode) {
        applyToAll( (talon) -> talon.setNeutralMode(nMode) );
    }

    public void setInverted(boolean [] inverted, boolean talonInverted) {
        elMaster.setInverted(talonInverted);
        if(inverted.length == victors.length) {
            for(int i = 0; i < victors.length; i++) {
                victors[i].setInverted(inverted[i]);
            }
        }
    }

    public void followMaster() {
        applyToFollowers( (talon) -> talon.follow(elMaster));
    }

}