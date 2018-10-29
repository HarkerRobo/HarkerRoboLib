/**
 *
 */
package harkerrobolib.util

import java.security.InvalidParameterException

/**
 * Wrapper class for a series of methods allowing for easy unit conversions.
 * @author Finn Frankis
 * @version 7/5/18
 */
object Conversions {

    /**
     * The diameter of the wheel to be used.
     */
    var WHEEL_DIAMETER = -1.0

    /**
     * The number of pigeon units in a single rotation.
     */
    val PIGEON_UNITS_PER_ROTATION = 8192.0

    /**
     * The number of degrees in a single rotation.
     */
    val DEGREES_PER_ROTATION = 360.0

    /**
     * The number of radians in a single rotation.
     */
    val RADIANS_PER_ROTATION = 2 * Math.PI

    /**
     * The number of milliseconds in a second.
     */
    val MS_PER_SEC = 1000.0

    /**
     * The number of microseconds in a second.
     */
    val MICROSECS_PER_SEC = Math.pow(10.0, 6.0)

    /**
     * The number of nanoseconds in a second.
     */
    val NANOSECS_PER_SEC = Math.pow(10.0, 9.0)

    /**
     * The number of ticks per revolution for a CTRE magnetic encoder.
     */
    val TICKS_PER_REV = 4096

    /**
     * The number of inches in a foot.
     */
    val INCHES_PER_FOOT = 12

    interface Unit
    /**
     * Represents the various possible units for an angle.
     * @author Finn Frankis
     * @version 7/5/18
     */
    enum class AngleUnit : Unit {
        /**
         * The angle unit of radians.
         */
        RADIANS,

        /**
         * The angle unit of degrees.
         */
        DEGREES,

        /**
         * The angle unit of pigeon units (for use with CTRE's Pigeon IMU).
         */
        PIGEON_UNITS
    }

    /**
     * Represents the various possible units for a position.
     * @author Finn Frankis
     * @version 7/5/18
     */
    enum class PositionUnit : Unit {
        /**
         * The position unit of feet.
         */
        FEET,

        /**
         * The position unit of encoder ticks.
         */
        ENCODER_UNITS
    }

    /**
     * Represents the various possible units for a speed.
     * @author Finn Frankis
     * @version 7/5/18
     */
    enum class SpeedUnit : Unit {
        /**
         * The speed unit of feet per second.
         */
        FEET_PER_SECOND,

        /**
         * The speed unit of encoder ticks per 100 ms.
         */
        ENCODER_UNITS
    }

    /**
     * Represents the various possible units for a time.
     * @author Finn Frankis
     * @version 7/5/18
     */
    enum class TimeUnit : Unit {
        /**
         * The time unit of seconds.
         */
        SECONDS,

        /**
         * The time unit of milliseconds.
         */
        MILLISECONDS,

        /**
         * The time unit of microseconds.
         */
        MICROSECONDS,

        /**
         * The time unit of nanoseconds.
         */
        NANOSECONDS
    }

    /**
     * Converts a value of one unit to a value of another unit.
     * @param startUnit the unit of the passed-in value (either AngleUnit, SpeedUnit, PositionUnit, TimeUnit).
     * @param startValue the value to convert.
     * @param desiredUnit the desired unit of the passed-in value.
     *
     * @precondition startUnit and desiredUnit both measure the same quantity.
     * @return the converted value.
     */
    fun convert(startUnit: Unit, startValue: Double, desiredUnit: Unit): Double {
        if (startUnit is AngleUnit && desiredUnit is AngleUnit) {
            return convertAngle(startUnit, startValue, desiredUnit)
        } else if (startUnit is SpeedUnit && desiredUnit is SpeedUnit) {
            return convertSpeed(startUnit, startValue, desiredUnit)
        } else if (startUnit is PositionUnit && desiredUnit is PositionUnit) {
            return convertPosition(startUnit, startValue, desiredUnit)
        } else if (startUnit is TimeUnit && desiredUnit is TimeUnit) {
            return convertTime(startUnit, startValue, desiredUnit)
        }

        throw InvalidParameterException("Unit classes are non-equivalent")
    }

    /**
     * Converts an angle in one unit to another.
     * @param startUnit the unit of the given value
     * @param startValue the value to be converted
     * @param desiredUnit the unit desired for the conversion
     * @return the converted value
     */
    fun convertAngle(startUnit: AngleUnit, startValue: Double, desiredUnit: AngleUnit): Double {
        return if (desiredUnit == AngleUnit.RADIANS) {
            if (startUnit == AngleUnit.RADIANS)
                startValue
            else if (startUnit == AngleUnit.DEGREES)
                startValue * RADIANS_PER_ROTATION / DEGREES_PER_ROTATION
            else
                startValue * RADIANS_PER_ROTATION / PIGEON_UNITS_PER_ROTATION
        } else if (desiredUnit == AngleUnit.DEGREES) {
            if (startUnit == AngleUnit.DEGREES)
                startValue
            else if (startUnit == AngleUnit.RADIANS)
                startValue * DEGREES_PER_ROTATION / RADIANS_PER_ROTATION
            else
                startValue * DEGREES_PER_ROTATION / PIGEON_UNITS_PER_ROTATION
        } else {
            if (startUnit == AngleUnit.PIGEON_UNITS)
                startValue
            else if (startUnit == AngleUnit.DEGREES)
                startValue * PIGEON_UNITS_PER_ROTATION / DEGREES_PER_ROTATION
            else
                startValue * PIGEON_UNITS_PER_ROTATION / RADIANS_PER_ROTATION
        }
    }

    /**
     * Converts a position in one unit to another.
     * @param startUnit the unit of the given value
     * @param startValue the value to be converted
     * @param desiredUnit the unit desired for the conversion
     * @return the converted value
     */
    fun convertPosition(startUnit: PositionUnit, startValue: Double, desiredUnit: PositionUnit): Double {
        if (WHEEL_DIAMETER != -1.0) {
            return if (desiredUnit == PositionUnit.FEET) {
                if (startUnit == PositionUnit.FEET)
                    startValue
                else
                    (startValue / Conversions.TICKS_PER_REV // convert to revolutions
                            * (WHEEL_DIAMETER * Math.PI) // convert to inches
                            / Conversions.INCHES_PER_FOOT.toDouble()) // convert to feet
            } else {
                if (startUnit == PositionUnit.ENCODER_UNITS)
                    startValue
                else
                    (startValue * Conversions.INCHES_PER_FOOT // convert to inches
                            / (WHEEL_DIAMETER * Math.PI)
                            * Conversions.TICKS_PER_REV.toDouble())// convert to ticks
            }
        }
        try {
            throw InterruptedException("You must specify a valid wheel diameter in setWheelDiameter()")
        } catch (e: InterruptedException) {
            e.printStackTrace()
            return -1.0
        }

    }

    /**
     * Converts a speed in one unit to another.
     * @param startUnit the unit of the given value
     * @param startValue the value to be converted
     * @param desiredUnit the unit desired for the conversion
     * @return the converted value
     */
    fun convertSpeed(startUnit: SpeedUnit, startValue: Double, desiredUnit: SpeedUnit): Double {
        if (WHEEL_DIAMETER >= 0) {
            return if (desiredUnit == SpeedUnit.FEET_PER_SECOND) {
                if (startUnit == SpeedUnit.FEET_PER_SECOND)
                    startValue
                else
                    ((startValue * 10.0 // convert to ticks per second
                            / Conversions.TICKS_PER_REV) // convert to revolutions per second
                            * (WHEEL_DIAMETER * Math.PI) // convert to inches per second
                            / Conversions.INCHES_PER_FOOT.toDouble()) // convert to feet per second
            } else {
                if (startUnit == SpeedUnit.ENCODER_UNITS)
                    startValue
                else
                    ((startValue / 10.0 // convert to feet per 100 ms
                            * Conversions.INCHES_PER_FOOT) // convert to inches per 100 ms
                            / (WHEEL_DIAMETER * Math.PI) // convert to revolutions per 100ms
                            * Conversions.TICKS_PER_REV.toDouble()) // convert to ticks per 100ms
            }
        }
        try {
            throw InterruptedException("You must specify a valid wheel diameter in setWheelDiameter()")
        } catch (e: InterruptedException) {
            e.printStackTrace()
            return -1.0
        }

    }

    /**
     * Converts a time in one unit to another.
     * @param startUnit the unit of the given value
     * @param startValue the value to be converted
     * @param desiredUnit the unit desired for the conversion
     * @return the converted value
     */
    fun convertTime(startUnit: TimeUnit, startValue: Double, desiredUnit: TimeUnit): Double {
        return if (desiredUnit == TimeUnit.SECONDS) {
            if (startUnit == TimeUnit.SECONDS)
                startValue
            else if (startUnit == TimeUnit.MILLISECONDS)
                startValue / Conversions.MS_PER_SEC
            else if (startUnit == TimeUnit.MICROSECONDS)
                startValue / Conversions.MICROSECS_PER_SEC
            else
                startValue / Conversions.NANOSECS_PER_SEC
        } else if (desiredUnit == TimeUnit.MILLISECONDS) {
            if (startUnit == TimeUnit.MILLISECONDS)
                startValue
            else if (startUnit == TimeUnit.SECONDS)
                startValue * Conversions.MS_PER_SEC
            else if (startUnit == TimeUnit.MICROSECONDS)
                startValue / Conversions.MICROSECS_PER_SEC * Conversions.MS_PER_SEC
            else
                startValue / Conversions.NANOSECS_PER_SEC * Conversions.MS_PER_SEC
        } else if (desiredUnit == TimeUnit.MICROSECONDS) {
            if (startUnit == TimeUnit.MICROSECONDS)
                startValue
            else if (startUnit == TimeUnit.SECONDS)
                startValue * Conversions.MICROSECS_PER_SEC
            else if (startUnit == TimeUnit.MILLISECONDS)
                startValue / Conversions.MS_PER_SEC * Conversions.MICROSECS_PER_SEC
            else
                startValue / Conversions.NANOSECS_PER_SEC * Conversions.MICROSECS_PER_SEC
        } else
        // nanoseconds
        {
            if (startUnit == TimeUnit.NANOSECONDS)
                startValue
            else if (startUnit == TimeUnit.SECONDS)
                startValue * Conversions.NANOSECS_PER_SEC
            else if (startUnit == TimeUnit.MILLISECONDS)
                startValue / Conversions.MS_PER_SEC * Conversions.NANOSECS_PER_SEC
            else
                startValue / Conversions.MICROSECS_PER_SEC * Conversions.NANOSECS_PER_SEC
        }
    }

    /**
     * Sets the wheel diameter for use in position and velocity computations.
     * @param newDiameter the diameter with which the current one will be replaced
     */
    fun setWheelDiameter(newDiameter: Double) {
        WHEEL_DIAMETER = newDiameter
    }


}
