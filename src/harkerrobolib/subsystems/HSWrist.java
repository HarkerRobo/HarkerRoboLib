package harkerrobolib.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.IMotorController;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatorCurrentLimitConfiguration;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import harkerrobolib.util.Gains;
import harkerrobolib.wrappers.HSMotorController;
import java.util.function.Consumer;

/**
 * TODO: good description
 *
 * @author Chirag Kaushik
 * @since January 17, 2019
 */
public abstract class HSWrist<Motor extends HSMotorController> extends SubsystemBase {
  public enum WristDirection {
    TO_BACK(1),
    TO_FRONT(-1);
    private final int direction;

    private WristDirection(int direction) {
      this.direction = direction;
    }

    public int getSign() {
      return direction;
    }
  }

  private Motor master;
  private IMotorController follower;

  private boolean hasFollower;

  /** Creates an HSWrist with only a master talon */
  public HSWrist(Motor master) {
    this.master = master;
    hasFollower = false;
  }

  /** Creates an HSWrist with a master talon and a follower */
  public HSWrist(Motor master, IMotorController follower) {
    this.master = master;
    this.follower = follower;
    hasFollower = true;
  }

  public void applyToAll(Consumer<IMotorController> consumer) {
    consumer.accept(master);
    if (hasFollower) consumer.accept(follower);
  }

  public void followMasters() {
    if (hasFollower) {
      follower.follow(master);
    }
  }

  public void setNeutralMode(NeutralMode neutralMode) {
    master.setNeutralMode(neutralMode);
    if (hasFollower) follower.setNeutralMode(neutralMode);
  }

  public void setInverted(boolean masterInverted) {
    master.setInverted(masterInverted);
  }

  public void setInverted(boolean masterInverted, boolean followerInverted) {
    master.setInverted(masterInverted);
    if (hasFollower) follower.setInverted(followerInverted);
  }

  /**
   * Sets the current limits of the master motor
   *
   * @param peakLimit the peak limit
   * @param peakTime the time when the peak limit is active
   * @param continuousLimit the continuous limit, which occurs after the peak limit
   * @param timeout the
   */
  public void setCurrentLimit(double peakLimit, double peakTime, double continuousLimit) {
    master.configStatorCurrentLimit(
        new StatorCurrentLimitConfiguration(true, continuousLimit, peakLimit, peakTime));
  }

  /**
   * Configures the PID constants for a given loop index.
   *
   * @param loopIndex the loop index where the PID constants are to be stored
   * @param constant a Gains object containing the constants
   */
  public void configureClosedLoopConstants(int loopIndex, Gains constants) {
    master.configClosedLoopConstants(loopIndex, constants);
  }

  /** */
  public void setWrist(double percentOutput, WristDirection direction) {
    master.set(ControlMode.PercentOutput, percentOutput * direction.getSign());
  }

  /**
   * Checks whether the error for the master motor is within a value.
   *
   * @param loopIndex the loop index of the PID
   * @param error the permitible error
   * @return if the absolute value of the error is less than the value
   */
  public boolean isClosedLoopErrorWithin(int loopIndex, double error) {
    return Math.abs(master.getClosedLoopError(loopIndex)) < error;
  }

  /**
   * Returns the master talon
   *
   * @return the master talon
   */
  public Motor getMaster() {
    return master;
  }

  /**
   * Returns the follower motor
   *
   * @return the follower motor
   */
  public IMotorController getFollower() {
    return follower;
  }
}
