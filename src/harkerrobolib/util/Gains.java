package harkerrobolib.util;

/**
 * Designed to store PID constants in an easily accessible way.
 * @author Finn Frankis
 * @version 10/21/18
 */
public class Gains {
	private double kF;
	private double kP;
	private double kI;
	private double kD;
	private double iZone;
	
	public Gains(double kF, double kP, double kI, double kD, double iZone) {
		this.kF = kF;
		this.kP = kP;
		this.kI = kI;
		this.kD = kD;
		this.iZone = iZone;
	}
	
	public Gains(double kF, double kP, double kI, double kD) {
		this(kF, kP, kI, kD, 0);
	}
	
	public Gains(double kP, double kI, double kD) {
		this(0, kP, kI, kD, 0);
	}
	
	
	public double getkF() {
		return kF;
	}

	public double getkP() {
		return kP;
	}

	public double getkI() {
		return kI;
	}

	public double getkD() {
		return kD;
	}
	
	public double getIZone() {
		return iZone;
	}
}
