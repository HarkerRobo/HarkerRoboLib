package harkerrobolib.auto;

import java.security.InvalidParameterException;
import java.util.Map;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.PathfinderJNI;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Trajectory.Config;
import jaci.pathfinder.Trajectory.FitMethod;
import jaci.pathfinder.Trajectory.Segment;
import jaci.pathfinder.Waypoint;

/**
 *
 * @author Finn Frankis
 *
 */
public abstract class Path {
    private Trajectory leftPath;
    private Trajectory rightPath;
    
	private Waypoint[] waypoints;
	private FitMethod fitMethod;
	private double dt;
	private double velMax;
	private double accelMax;
	private double jerkMax;
	private double wheelBase;
    
    public static final int SAMPLE_GENERATION = Config.SAMPLES_HIGH;
    
    public static final FitMethod FITMETHOD_DEFAULT = FitMethod.HERMITE_QUINTIC;
    public static final double DT_DEFAULT = 0.01;
    public static final double V_DEFAULT = 7.5;
    public static final double ACCEL_DEFAULT = 15;
    public static final double JERK_DEFAULT = 100;
    public static final double WHEELBASE_DEFAULT = 3.0;
    

    
    
    public enum SegmentPart {
    	dt, x, y, position, velocity, acceleration, jerk, heading
    }
    
    public Path (Map<SegmentPart, Double>[] leftPath, Map<SegmentPart, Double>[] rightPath) {
    	this.leftPath = generateTrajectory (leftPath);
    	this.rightPath = generateTrajectory (rightPath);
    }
    
    public Path (Waypoint[] waypoints, FitMethod fitMethod, double dt, double velMax, double accelMax, double jerkMax, double wheelBase) {
    	this.waypoints = waypoints;
		this.fitMethod = fitMethod;
		this.dt = dt;
		this.velMax = velMax;
		this.accelMax = accelMax;
		this.jerkMax = jerkMax;
		this.wheelBase = wheelBase;
		
		Trajectory[] generatedPath = 
    			PathfinderJNI.modifyTrajectoryTank(
    					Pathfinder.generate(waypoints, 
    							new Config(fitMethod, SAMPLE_GENERATION, dt, velMax, accelMax, jerkMax)), 
    					wheelBase);
    	leftPath = generatedPath[0];
    	rightPath = generatedPath[1];
    }
    public Path (Waypoint[] waypoints, double dt, double velMax, double accelMax, double jerkMax, double wheelBase) {
    	this (waypoints, FITMETHOD_DEFAULT, dt, velMax, accelMax, jerkMax, wheelBase);
    }
    
    public Path (Waypoint[] waypoints, double dt, double velMax, double accelMax, double jerkMax) {
    	this (waypoints, dt, velMax, accelMax, jerkMax, WHEELBASE_DEFAULT);
    }
    
    public Path (Waypoint[] waypoints, double dt) {
    	this (waypoints, dt, V_DEFAULT, ACCEL_DEFAULT, JERK_DEFAULT);
    }
    
    public Path (Waypoint[] waypoints) {
    	this (waypoints, DT_DEFAULT);
    }
    
    public Trajectory getLeftPath() {
    	return leftPath;
    }
    
    public Trajectory getRightPath() {
    	return rightPath;
    }
    
    private Trajectory generateTrajectory (Map<SegmentPart, Double>[] path) {
    	Segment[] segments = new Segment[path.length];
    	int i = 0;
    	for (Map<SegmentPart, Double> segment : path) {
    		segments[i] = new Segment(segment.get(SegmentPart.dt), 
    				segment.get(SegmentPart.x), 
    				segment.get(SegmentPart.y), 
    				segment.get(SegmentPart.position), 
    				segment.get(SegmentPart.velocity), 
    				segment.get(SegmentPart.acceleration),
    				segment.get(SegmentPart.jerk), 
    				segment.get(SegmentPart.heading));
    		i++;
    	}
    	return new Trajectory(segments);
    }
    
    /**
     * Generates an array of waypoints given the data in an array of double arrays.
     * @param points the array of points represented as an array of double arrays, where each internal array is 
     * of length 3, with order x, y, angle.
     * @return the converted array
     */
    public static Waypoint[] generateWaypoints (Double[][] points) {
    	Waypoint[] waypoints = new Waypoint[points.length];
    	for (int i = 0; i < points.length; i++) {
    		Double[] point = points[i];
    		if (point.length != 3)
    			throw new InvalidParameterException ("The array of points is not formatted correctly. See Javadoc for clarification.");
    		waypoints[i] = new Waypoint(points[i][0], points[i][1], points[i][2]);
    	}
		return waypoints;
    }

	public Waypoint[] getWaypoints() {
		return waypoints;
	}

	public void setWaypoints(Waypoint[] waypoints) {
		this.waypoints = waypoints;
	}

	public FitMethod getFitMethod() {
		return fitMethod;
	}

	public void setFitMethod(FitMethod fitMethod) {
		this.fitMethod = fitMethod;
	}

	public double getDt() {
		return dt;
	}

	public void setDt(double dt) {
		this.dt = dt;
	}

	public double getVelMax() {
		return velMax;
	}

	public void setVelMax(double velMax) {
		this.velMax = velMax;
	}

	public double getAccelMax() {
		return accelMax;
	}

	public void setAccelMax(double accelMax) {
		this.accelMax = accelMax;
	}

	public double getJerkMax() {
		return jerkMax;
	}

	public void setJerkMax(double jerkMax) {
		this.jerkMax = jerkMax;
	}

	public double getWheelBase() {
		return wheelBase;
	}

	public void setWheelBase(double wheelBase) {
		this.wheelBase = wheelBase;
	}
   
}