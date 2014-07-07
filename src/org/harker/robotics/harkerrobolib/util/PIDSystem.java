package org.harker.robotics.harkerrobolib.util;

/**
 * A PID Feedback Loop System. This should be integrated with a {@link PIDSystemCommand}
 * for continuous feedback. This should not be confused with the WPILibJ {@link PIDController}
 * as that has been historically proven to have issues in implementation.
 *
 * @author Manan Shah
 * @author atierno
 */
public class PIDSystem {
    private double P, I, D, prevError, intError, control;
    private final double minIntSaturation, maxIntSaturation;
    private final boolean doesApplySaturation;
    private final String systemName;

    /**
     * A new PIDSystem with no checks for saturation.
     * @param name The name of the system.
     * @param p The proportional gain.
     * @param i The integral gain.
     * @param d The derivative gain.
     */
    public PIDSystem(String name, double p, double i, double d) {
        systemName = name;
        P = p;
        I = i;
        D = d;
        //Default values for saturation
        minIntSaturation = 0;
        maxIntSaturation = 0;
        doesApplySaturation = false;
    }

    /**
     * A new PIDSystem with checks for saturation.
     * @param name The name of the system.
     * @param p The proportional gain.
     * @param i The integral gain.
     * @param d The derivative gain.
     * @param minSaturation The minimum output of the integral term for error calculation.
     * @param maxSaturation The maximum output of the integral term for error calculation.
     * @see PIDSystem#updatePID(double, double)
     */
    public PIDSystem(String name, double p, double i, double d, double minSaturation, double maxSaturation) {
        systemName = name;
        P = p;
        I = i;
        D = d;
        minIntSaturation = minSaturation;
        maxIntSaturation = maxSaturation;
        doesApplySaturation = true;
    }

    /**
     * This function updates the feedback loop given the current error and the time since the last call.
     * It should be called frequently on a regular basis to make sure that the outputs are current. Ideally
     * this should be used with a {@link PIDSystemCommand}. The essential idea is that it takes the current
     * error term from the ideal output (i.e. the difference between the velocity of the drivetrain and the
     * target velocity). It then calculates the integral error (the sum of all errors over the lifetime of the
     * system), the current error (passed as a parameter, the error relative to the current output) and the
     * difference error (the difference between the current and past error). These errors are summed into the
     * new output, {@link PIDSystem#control}. Control should be used directly for whatever output source is
     * being utilized. If {@link PIDSystem#doesApplySaturation} is set to true, the PIDSystem will also check
     * to see if the integral error has grown beyond what is possible for the output or if it has gotten too
     * noisy (i.e. returning a number greater than 1 or less than -1 for an analog motor which has a range of
     * -1.0 to 1.0).
     *
     * @param currError The current error from the ideal target
     * @param timeStep The time since the last call of the function
     */
    public void updatePID(double currError, double timeStep) {
        double diff;

        double pTerm, iTerm, dTerm;

        intError += currError * timeStep;
        if (doesApplySaturation) {
            if (intError > maxIntSaturation) {
                intError = maxIntSaturation;
            } else if (intError < minIntSaturation) {
                intError = minIntSaturation;
            }
        }

        diff = (currError - prevError) / timeStep;

        pTerm = P * currError;
        iTerm = I * intError;
        dTerm = D * diff;

        control = pTerm + iTerm + dTerm;
        prevError = currError;
    }

    /**
     * Gets the feedback from the PID Loop (this should be used directly as output)
     * @return The result of the PID Loop.
     */
    public double getOutput() {
        return control;
    }

    /**
     * Gets the proportional gain of the PIDSystem.
     * @return The proportional gain.
     */
    public double getP() {
        return P;
    }

    /**
     * Sets the proportional gain of the PIDSystem.
     * @param p The new proportional gain.
     */
    public void setP(double p) {
        P = p;
    }

    /**
     * Gets the integral gain of the PIDSystem.
     * @return The integral gain.
     */
    public double getI() {
        return I;
    }

    /**
     * Sets the integral gain of the PIDSystem.
     * @param i The new integral gain.
     */
    public void setI(double i) {
        I = i;
    }

    /**
     * Gets the derivative gain of the PIDSystem.
     * @return The derivative gain.
     */
    public double getD() {
        return D;
    }

    /**
     * Sets the derivative gain of the PIDSystem.
     * @param d The new derivative gain.
     */
    public void setD(double d) {
        D = d;
    }

    /**
     * Gets the PIDSystem's name.
     * @return The system name.
     */
    public String getName() {
        return systemName;
    }
}