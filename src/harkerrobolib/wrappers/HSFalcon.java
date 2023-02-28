package harkerrobolib.wrappers;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.RemoteFeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import edu.wpi.first.util.sendable.SendableBuilder;
import harkerrobolib.util.Constants;

/**
 * Wraps a standard TalonSRX.
 *
 * @author Chirag Kaushik
 * @author Ada Praun-Petrovic
 */
public class HSFalcon extends WPI_TalonFX implements HSMotorController {

  /**
   * Constructs a TalonSRXWrapper with the default timeout {{@link Constants#DEFAULT_TIMEOUT}.
   *
   * @param deviceNumber The CAN device ID of the Talon.
   */
  public HSFalcon(final int deviceNumber) {
    this(deviceNumber, "rio");
  }

  /**
   * Constructs a TalonSRXWrapper with the default timeout {{@link Constants#DEFAULT_TIMEOUT}.
   *
   * @param deviceNumber The CAN device ID of the Talon.
   */
  public HSFalcon(final int deviceNumber, String busId) {
    super(deviceNumber, busId);
  }

  @Override
  public ErrorCode configSelectedFeedbackSensor(RemoteFeedbackDevice feedbackDevice, int pidIdx) {
    return super.configSelectedFeedbackSensor(feedbackDevice, pidIdx, Constants.DEFAULT_TIMEOUT);
  }

  @Override
  public ErrorCode configSelectedFeedbackSensor(FeedbackDevice feedbackDevice, int pidIdx) {
    return super.configSelectedFeedbackSensor(feedbackDevice, pidIdx, Constants.DEFAULT_TIMEOUT);
  }

  public void initSendable(SendableBuilder builder) {
    builder.setSmartDashboardType("HSFalcon");
    builder.addDoubleProperty("Output Voltage", () -> getMotorOutputVoltage(), null);
    builder.addDoubleProperty("Supply Current", () -> getSupplyCurrent(), null);
    builder.addDoubleProperty("Stator Current", () -> getStatorCurrent(), null);
    builder.addDoubleProperty("Encoder Velocity", () -> getSelectedSensorVelocity(), null);
    builder.addDoubleProperty("Encoder Position", () -> getSelectedSensorPosition(), null);
    builder.addDoubleProperty("Temperature", () -> getTemperature(), null);
  }
}
