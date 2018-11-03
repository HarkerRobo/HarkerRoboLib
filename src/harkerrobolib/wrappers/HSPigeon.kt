package harkerrobolib.wrappers

import com.ctre.phoenix.ErrorCode
import com.ctre.phoenix.ParamEnum
import com.ctre.phoenix.motorcontrol.can.TalonSRX
import com.ctre.phoenix.sensors.PigeonIMU
import com.ctre.phoenix.sensors.PigeonIMU_ControlFrame
import com.ctre.phoenix.sensors.PigeonIMU_Faults
import com.ctre.phoenix.sensors.PigeonIMU_StatusFrame
import com.ctre.phoenix.sensors.PigeonIMU_StickyFaults

import harkerrobolib.util.Constants

/**
 * Represents a basic PigeonIMU with additional useful features.
 * @author Finn Frankis
 * @version 10/21/18
 */
class HSPigeon : PigeonIMU {

    private var timeout: Int = 0

    /**
     * Gets the current yaw value of the pigeon.
     * @return the yaw
     */
    var yaw: Double
        get() {
            val ypr = DoubleArray(3)
            getYawPitchRoll(ypr)
            return ypr[0]
        }
        set(angle:Double) {
            super.setYaw(angle * 64, timeout) // CTRE's error where added angle is off by a factor of 64
        }

    /**
     * Gets the current pitch value of the pigeon.
     * @return the pitch
     */
    val pitch: Double
        get() {
            val ypr = DoubleArray(3)
            getYawPitchRoll(ypr)
            return ypr[1]
        }

    /**
     * Gets the current roll value of the pigeon.
     * @return the roll
     */
    val pigeonRoll: Double
        get() {
            val ypr = DoubleArray(3)
            getYawPitchRoll(ypr)
            return ypr[2]
        }

    @JvmOverloads constructor(deviceNumber: Int, defaultTimeout: Int = Constants.DEFAULT_TIMEOUT) : super(deviceNumber) {
        this.timeout = defaultTimeout
    }

    @JvmOverloads constructor(talon: TalonSRX, defaultTimeout: Int = Constants.DEFAULT_TIMEOUT) : super(talon) {
        this.timeout = defaultTimeout
    }



    fun setYawToCompass(): ErrorCode {
        // TODO Auto-generated method stub
        return super.setYawToCompass(timeout)
    }


    fun setFusedHeading(angleDeg: Double): ErrorCode {
        // TODO Auto-generated method stub
        return super.setFusedHeading(angleDeg, timeout)
    }


    fun addFusedHeading(angleDeg: Double): ErrorCode {
        // TODO Auto-generated method stub
        return super.addFusedHeading(angleDeg, timeout)
    }


    fun setFusedHeadingToCompass(): ErrorCode {
        // TODO Auto-generated method stub
        return super.setFusedHeadingToCompass(timeout)
    }


    fun setAccumZAngle(angleDeg: Double): ErrorCode {
        // TODO Auto-generated method stub
        return super.setAccumZAngle(angleDeg, timeout)
    }


    fun configTemperatureCompensationEnable(bTempCompEnable: Boolean): ErrorCode {
        // TODO Auto-generated method stub
        return super.configTemperatureCompensationEnable(bTempCompEnable, timeout)
    }


    fun setCompassDeclination(angleDegOffset: Double): ErrorCode {
        // TODO Auto-generated method stub
        return super.setCompassDeclination(angleDegOffset, timeout)
    }


    fun setCompassAngle(angleDeg: Double): ErrorCode {
        // TODO Auto-generated method stub
        return super.setCompassAngle(angleDeg, timeout)
    }


    fun enterCalibrationMode(calMode: PigeonIMU.CalibrationMode): ErrorCode {
        // TODO Auto-generated method stub
        return super.enterCalibrationMode(calMode, timeout)
    }


    fun configSetCustomParam(newValue: Int, paramIndex: Int): ErrorCode {
        // TODO Auto-generated method stub
        return super.configSetCustomParam(newValue, paramIndex, timeout)
    }


    override fun configGetCustomParam(paramIndex: Int, timoutMs: Int): Int {
        // TODO Auto-generated method stub
        return super.configGetCustomParam(paramIndex, timoutMs)
    }


    fun configSetParameter(param: ParamEnum, value: Double, subValue: Int, ordinal: Int): ErrorCode {
        // TODO Auto-generated method stub
        return super.configSetParameter(param, value, subValue, ordinal, timeout)
    }


    fun configSetParameter(param: Int, value: Double, subValue: Int, ordinal: Int): ErrorCode {
        // TODO Auto-generated method stub
        return super.configSetParameter(param, value, subValue, ordinal, timeout)
    }


    fun configGetParameter(param: ParamEnum, ordinal: Int): Double {
        // TODO Auto-generated method stub
        return super.configGetParameter(param, ordinal, timeout)
    }


    fun configGetParameter(param: Int, ordinal: Int): Double {
        // TODO Auto-generated method stub
        return super.configGetParameter(param, ordinal, timeout)
    }


    fun setStatusFramePeriod(statusFrame: PigeonIMU_StatusFrame, periodMs: Int): ErrorCode {
        // TODO Auto-generated method stub
        return super.setStatusFramePeriod(statusFrame, periodMs, timeout)
    }


    fun setStatusFramePeriod(statusFrame: Int, periodMs: Int): ErrorCode {
        // TODO Auto-generated method stub
        return super.setStatusFramePeriod(statusFrame, periodMs, timeout)
    }


    fun getStatusFramePeriod(frame: PigeonIMU_StatusFrame): Int {
        // TODO Auto-generated method stub
        return super.getStatusFramePeriod(frame, timeout)
    }


    fun clearStickyFaults(): ErrorCode {
        // TODO Auto-generated method stub
        return super.clearStickyFaults(timeout)
    }


    /**
     * Adds a given value to the pigeon yaw.
     * @param angle the angle value which should be added to the pigeon yaw value, in pigeon units
     * where 1 rotation is 8192 units
     */
    fun addYaw(angle: Double) {
        yaw += angle
    }

    /**
     * Zeros the pigeon.
     */
    @Deprecated("Replaced with more intuitive zero(). Same functionality.")
    fun zeroPigeon() {
        zero()
    }

    fun zero() {
        yaw = 0.0
        setAccumZAngle(0.0)
    }

}
