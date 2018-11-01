package harkerrobolib.auto

import java.security.InvalidParameterException

import jaci.pathfinder.Pathfinder
import jaci.pathfinder.PathfinderJNI
import jaci.pathfinder.Trajectory
import jaci.pathfinder.Trajectory.Config
import jaci.pathfinder.Trajectory.FitMethod
import jaci.pathfinder.Waypoint

/**
 *
 * @author Finn Frankis
 */
abstract class Path (val waypoints: List<Waypoint>,
                     val fitMethod: jaci.pathfinder.Trajectory.FitMethod = FITMETHOD_DEFAULT,
                     val dt: Double = DT_DEFAULT,
                     val velMax: Double = V_DEFAULT,
                     val accelMax: Double = ACCEL_DEFAULT,
                     val jerkMax: Double = JERK_DEFAULT,
                     val wheelBase: Double = WHEELBASE_DEFAULT){
    var leftPath: Trajectory
    var rightPath: Trajectory

    init {
        val generatedPath = PathfinderJNI.modifyTrajectoryTank(
                Pathfinder.generate(waypoints.toTypedArray(),
                        Config(fitMethod, SAMPLE_GENERATION, dt, velMax, accelMax, jerkMax)),
                wheelBase)
        leftPath = generatedPath[0]
        rightPath = generatedPath[1]
    }

    companion object {

        var SAMPLE_GENERATION = Config.SAMPLES_HIGH
        var FITMETHOD_DEFAULT = FitMethod.HERMITE_QUINTIC
        var DT_DEFAULT = 0.01
        var V_DEFAULT = 7.5
        var ACCEL_DEFAULT = 15.0
        var JERK_DEFAULT = 100.0
        var WHEELBASE_DEFAULT = 3.0

        /**
         * Generates an array of waypoints given the data in an array of double arrays.
         * @param points the array of points represented as an array of double arrays, where each internal array is
         * of length 3, with order x, y, angle.
         * @return the converted array
         */
        fun generateWaypoints(points: Array<Array<Double>>): Array<Waypoint?> {
            val waypoints = arrayOfNulls<Waypoint>(points.size)
            for (i in points.indices) {
                val point = points[i]
                if (point.size != 3)
                    throw InvalidParameterException("The array of points is not formatted correctly. See Javadoc for clarification.")
                waypoints[i] = Waypoint(points[i][0], points[i][1], points[i][2])
            }
            return waypoints
        }
    }

}