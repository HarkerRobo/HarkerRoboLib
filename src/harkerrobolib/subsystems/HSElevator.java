package harkerrobolib.subsystems;

import com.ctre.phoenix.motorcontrol.IMotorController;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatorCurrentLimitConfiguration;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import harkerrobolib.util.MathUtil;
import harkerrobolib.wrappers.HSMotorController;
import java.util.function.Consumer;

/**
 * Represents a general Elevator subsystem with a master talon and variable number of followers.
 *
 * @author Angela Jia
 * @since 1/12/19
 */
public abstract class HSElevator<Motor extends HSMotorController> extends SubsystemBase {

  private Motor elMaster;
  private IMotorController[] victors;
  private double feedForwardGrav;
  private int LOOP_INDEX = -1;
  private int SOFT_LIMIT_POSITION = -1;
  private double SLOW_DOWN_PERCENT = -1;
  private double MAX_LESS_OUTPUT_FACTOR = -1;
  private double MIN_LESS_OUTPUT_FACTOR = -1;
  private double MAX_MORE_OUTPUT_FACTOR = -1;
  private double MIN_MORE_OUTPUT_FACTOR = -1;
  private double MAX_SPEED = -1;

  public HSElevator(Motor talon, double feedForwardGrav, IMotorController... victors) {
    elMaster = talon;
    for (int i = 0; i < victors.length; i++) {
      this.victors[i] = victors[i];
    }
    this.feedForwardGrav = feedForwardGrav;
  }

  /**
   * Use to ramp down elevator as it moves down past a soft limit.
   *
   * @param talon master talon
   * @param feedForwardGrav constant added to output to alleviate gravity
   * @param maxSpeed max speed the elevator should move past the soft limit
   * @param slowDownPercent standard percent beyond which output factor
   * @param loopIndex PID loop index for sensor reading positions
   * @param softLimitPosition where soft limit (position on elevator to slow down in downward
   *     direction) is located
   * @param maxLessOutputFactor upper bound of output factor when speed percent is less than slow
   *     down percent
   * @param minLessOutputFactor lower bound of output factor when speed percent is less than slow
   *     down percent
   * @param maxMoreOutputFactor upper bound of output factor when speed percent is more than slow
   *     down percent
   * @param minMoreOutputFactor upper bound of output factor when speed percent is more than slow
   *     down percent
   */
  public HSElevator(
      Motor talon,
      double feedForwardGrav,
      int maxSpeed,
      double slowDownPercent,
      int loopIndex,
      int softLimitPosition,
      int maxLessOutputFactor,
      int minLessOutputFactor,
      int maxMoreOutputFactor,
      int minMoreOutputFactor,
      IMotorController... victors) {
    this(talon, feedForwardGrav, victors);
    MAX_SPEED = maxSpeed;
    SLOW_DOWN_PERCENT = slowDownPercent;
    SOFT_LIMIT_POSITION = softLimitPosition;
    MAX_LESS_OUTPUT_FACTOR = maxLessOutputFactor;
    MAX_MORE_OUTPUT_FACTOR = maxMoreOutputFactor;
    MIN_LESS_OUTPUT_FACTOR = minLessOutputFactor;
    MIN_MORE_OUTPUT_FACTOR = minMoreOutputFactor;
  }

  public IMotorController getMaster() {
    return elMaster;
  }

  public IMotorController[] getFollowers() {
    return victors;
  }

  public void setCurrentLimit(double peakTime, double peakLimit, double contLimit) {
    elMaster.configStatorCurrentLimit(
        new StatorCurrentLimitConfiguration(true, contLimit, peakLimit, peakTime));
  }

  public void applyToAll(Consumer<IMotorController> consumer) {
    consumer.accept(elMaster);
    for (int i = 0; i < victors.length; i++) {
      consumer.accept(victors[i]);
    }
  }

  public void applyToFollowers(Consumer<IMotorController> consumer) {
    for (int i = 0; i < victors.length; i++) {
      consumer.accept(victors[i]);
    }
  }

  public void setNeutralMode(NeutralMode nMode) {
    applyToAll((talon) -> talon.setNeutralMode(nMode));
  }

  public void setInverted(boolean[] inverted, boolean talonInverted) {
    elMaster.setInverted(talonInverted);
    if (inverted.length == victors.length) {
      for (int i = 0; i < victors.length; i++) {
        victors[i].setInverted(inverted[i]);
      }
    }
  }

  public void set(double desiredSpeed) {
    if (LOOP_INDEX != -1) {
      if (Math.abs(desiredSpeed) > 0) {
        boolean rampDown = desiredSpeed < 0;
        int position = (int) elMaster.getSelectedSensorPosition(LOOP_INDEX);
        boolean reversePastLimit = position <= SOFT_LIMIT_POSITION;
        double currVelocity = elMaster.getSelectedSensorVelocity(LOOP_INDEX);
        double distFromSoft = position - SOFT_LIMIT_POSITION;
        double outputFactor = 1.0;
        if (rampDown
            && reversePastLimit
            && Math.abs(currVelocity) / MAX_SPEED < SLOW_DOWN_PERCENT) {
          outputFactor =
              MathUtil.map(
                  distFromSoft,
                  0,
                  SOFT_LIMIT_POSITION,
                  MIN_LESS_OUTPUT_FACTOR,
                  MAX_LESS_OUTPUT_FACTOR);
        } else if (rampDown && reversePastLimit) {
          outputFactor =
              MathUtil.map(
                  distFromSoft,
                  0,
                  SOFT_LIMIT_POSITION,
                  MIN_MORE_OUTPUT_FACTOR,
                  MAX_MORE_OUTPUT_FACTOR);
        }
        desiredSpeed *= outputFactor;
      } else {
        desiredSpeed = 0;
      }
    }
  }

  public void followMaster() {
    applyToFollowers((talon) -> talon.follow(elMaster));
  }
}
