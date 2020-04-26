package harkerrobolib.util;

/**
 * Designed to store PID constants in an easily accessible way.
 * @author Finn Frankis
 * @author Shahzeb Lakhani
 * @author Anirudh Kotamraju
 * 
 * @version April 25, 2020
 */
public class Gains {
    private double kF;
    private double kP;
    private double kI;
    private double kD;
    private int iZone;
	
    public Gains kF (double kF) { this.kF = kF; return this;}
    public Gains kP (double kP) {this.kP = kP; return this;} 
    public Gains kI (double kI) {this.kI = kI; return this;}
    public Gains kD (double kD) {this.kD = kD; return this;}
    public Gains iZone (int iZone) {this.iZone = iZone; return this;}
	
    public double getkF() {return kF;}

	public double getkP() {return kP;} 

	public double getkI() {return kI;}

	public double getkD() {return kD;}
	
	public int getIZone() {return iZone;}
}
