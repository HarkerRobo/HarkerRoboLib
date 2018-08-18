package harkerrobolib.subsystems;

import java.util.function.Consumer;

import com.ctre.phoenix.motorcontrol.IMotorController;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;
import harkerrobolib.wrappers.TalonSRXWrapper;

/**
 * Represents a general Drivetrain subsystem.
 * @author Finn Frankis
 * @version Aug 17, 2018
 */
public abstract class DrivetrainSubsystem extends Subsystem {
    private TalonSRXWrapper leftMaster;
    private TalonSRXWrapper rightMaster;
    private IMotorController leftFollower;
    private IMotorController rightFollower;
    private boolean hasFollowers;

    private DrivetrainSubsystem (TalonSRXWrapper leftMaster, TalonSRXWrapper rightMaster, IMotorController leftFollower,
            IMotorController rightFollower) {
        this.leftMaster = leftMaster;
        this.rightMaster = rightMaster;
        this.leftFollower = leftFollower;
        this.rightFollower = rightFollower;
        hasFollowers = true;
    }

    private DrivetrainSubsystem (TalonSRXWrapper leftMaster, TalonSRXWrapper rightMaster) {
        this(leftMaster, rightMaster, null, null);
        hasFollowers = false;
    }

    
    public void applyToMasters (Consumer<TalonSRXWrapper> consumer) {
        consumer.accept(leftMaster);
        consumer.accept(rightMaster);
    }


    public void applyToAll (Consumer<IMotorController> consumer) {
        consumer.accept(leftMaster);
        consumer.accept(rightMaster);
        consumer.accept(leftFollower);
        consumer.accept(rightFollower);
    }

    public void initializeSubsystem() {
        followMasters();
    }
    public void followMasters () {
        if (hasFollowers) {
            leftFollower.follow(leftMaster);
            rightFollower.follow(rightMaster);
        }
    }
    
    public void setNeutralMode (NeutralMode nm) {
        if (hasFollowers) applyToAll ((talon) -> {talon.setNeutralMode(nm);});
        else applyToMasters((talon) -> {talon.setNeutralMode(nm);});

    }

    public void setCurrentLimit (int peakLimit, int peakTime, int continuousLimit, int timeout) {
        Consumer<TalonSRXWrapper> currentLimit = (talon) -> {
            int newTimeout = (timeout == -1) ? talon.getDefaultTimeout() : timeout;
            talon.configPeakCurrentLimit(peakLimit, newTimeout);
            talon.configPeakCurrentDuration(peakTime, newTimeout);
            talon.configContinuousCurrentLimit(continuousLimit, newTimeout);
        };
        applyToMasters (currentLimit);
    }
    
    public void setCurrentLimit (int peakLimit, int peakTime, int continuousLimit) {
        setCurrentLimit(peakLimit, peakTime, continuousLimit, -1);
    }
    
    public abstract DrivetrainSubsystem getInstance();

}
