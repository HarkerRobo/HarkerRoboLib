package harkerrobolib.util

/**
 * Designed to store PID constants in an easily accessible way.
 * @author Finn Frankis
 * @version 10/21/18
 */
data class Gains (val kF: Double = 0.0, val kP: Double = 0.0, val kI: Double = 0.0, val kD: Double = 0.0, val iZone: Double = 0.0)
