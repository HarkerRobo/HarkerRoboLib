package harkerrobolib.util

/**
 * Designed to store PID constants in an easily accessible way.
 * @author Finn Frankis
 * @version 10/21/18
 */
class Gains @JvmOverloads constructor(private val kF: Double, private val kP: Double, private val kI: Double, private val kD: Double, val iZone: Double = 0.0) {

    constructor(kP: Double, kI: Double, kD: Double) : this(0.0, kP, kI, kD, 0.0) {}


    fun getkF(): Double {
        return kF
    }

    fun getkP(): Double {
        return kP
    }

    fun getkI(): Double {
        return kI
    }

    fun getkD(): Double {
        return kD
    }
}
