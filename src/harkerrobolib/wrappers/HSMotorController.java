package harkerrobolib.wrappers;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.VelocityMeasPeriod;

public interface HSMotorController {

    ErrorCode setStatusFramePeriod(StatusFrameEnhanced frame, int periodMs, int timeoutMs);

    ErrorCode setStatusFramePeriod(StatusFrameEnhanced frame, int periodMs);

    int getStatusFramePeriod(StatusFrameEnhanced frame, int timeoutMs);

    int getStatusFramePeriod(StatusFrameEnhanced frame);

    double getOutputCurrent();

    double getStatorCurrent();

    double getSupplyCurrent();

    ErrorCode configVelocityMeasurementPeriod(VelocityMeasPeriod period, int timeoutMs);

    ErrorCode configVelocityMeasurementPeriod(VelocityMeasPeriod period);

    ErrorCode configVelocityMeasurementWindow(int windowSize, int timeoutMs);

    ErrorCode configVelocityMeasurementWindow(int windowSize);

    ErrorCode configForwardLimitSwitchSource(LimitSwitchSource type, LimitSwitchNormal normalOpenOrClose, int timeoutMs);

    ErrorCode configForwardLimitSwitchSource(LimitSwitchSource type, LimitSwitchNormal normalOpenOrClose);

    ErrorCode configReverseLimitSwitchSource(LimitSwitchSource type, LimitSwitchNormal normalOpenOrClose, int timeoutMs);

    ErrorCode configReverseLimitSwitchSource(LimitSwitchSource type, LimitSwitchNormal normalOpenOrClose);

    int isFwdLimitSwitchClosed();

    int isRevLimitSwitchClosed();

}