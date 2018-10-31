package harkerrobolib.util

import jaci.pathfinder.Waypoint

/**
 * Contains a series of useful mathematical functions.
 * @author  Finn Frankis
 * @author  Angela Jia
 * @version 10/16/18
 */
object MathUtil {

    /**
     * Constrains one value into a given range.
     * @param value the value to be constrained.
     * @param minValue the minimum value this value can take on.
     * @param maxValue the maximum value this value can take on.
     *
     * @precondition minValue < maxValue
     * @return the constrained value.
     */
    fun constrain(value: Double, minValue: Double, maxValue: Double): Double {
        return Math.max(Math.min(value, maxValue), minValue)
    }

    /**
     * Linearly maps a value currently within a given range to another range.
     * @param value the value to be mapped.
     * @param currentMin the current minimum possible value that value can take on.
     * @param currentMax the current maximum possible value that value can take on.
     * @param desiredMin the desired minimum possible value that value can take on.
     * @param desiredMax the desired maximum possible value that value can take on.
     * @return
     */
    fun map(value: Double, currentMin: Double, currentMax: Double, desiredMin: Double, desiredMax: Double): Double {
        return (value - currentMin) * (desiredMax - desiredMin) / (currentMax - currentMin) + desiredMin
    }

    /**
     * Maps a joystick input value between [-1, 1] to one where any input value between [-deadband, deadband] is zero
     * and anything outside of that range is mapped linearly from [0,1].
     * @param inputValue the measured input value.
     * @param deadband the joystick's deadband.
     * @return the mapped joystick input.
     */
    fun mapJoystickOutput(inputValue: Double, deadband: Double): Double {
        if (Math.abs(inputValue) <= deadband) {
            return 0.0
        }
        return if (inputValue > 0) map(inputValue, deadband, 1.0, 0.0, 1.0) else map(inputValue, -1.0, -deadband, -1.0, 0.0)
    }

    /**
     *  Reflects array of waypoints across the horizontal line at the specified y value.
     *
     *  @param  waypoints   waypoints used for modification
     *  @param  yVal        y value across which points will be reflected
     *  @return reflected array of waypoints
     */
    fun reflectY(waypoints : List<Waypoint>, yVal : Double ) : List<Waypoint> {
        return waypoints.map{waypoint -> Waypoint(waypoint.x, 2 * yVal - waypoint.y, - waypoint.angle)}
    }
}
