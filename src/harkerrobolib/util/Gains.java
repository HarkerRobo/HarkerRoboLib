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
	
	public Gains kF (double kF) { this.kF = kF; return this;}
	public Gains kP (double kP) {this.kP = kP; return this;} 
	public Gains kI (double kI) {this.kI = kI; return this;}
	public Gains kD (double kD) {this.kD = kD; return this;}
	public Gains iZone (double iZone) {this.iZone = iZone; return this;}
	
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
