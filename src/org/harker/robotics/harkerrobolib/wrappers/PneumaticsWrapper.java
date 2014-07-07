package org.harker.robotics.harkerrobolib.wrappers;

import edu.wpi.first.wpilibj.Solenoid;

/**
 * Working on the assumption that a pneumatic system is effectively two solenoids, 
 * the wrapper sets the states of the two solenoids (input and output) to be 
 * opposite of one another for any given state.
 * @author Manan
 */
public class PneumaticsWrapper {
    private final Solenoid in, out;
    
    /**
     * Creates a new PneumaticsWrapper with input and output solenoids.
     * @param inChannel The channel of the input solenoid.
     * @param outChannel The channel of the output solenoid.
     */
    public PneumaticsWrapper(int inChannel, int outChannel) {
        this.in = new Solenoid(inChannel);
        this.out = new Solenoid(outChannel);
    }
    
    /**
     * Sets the state of the pneumatic system. The output will be the opposite
     * of whatever state is given.
     * @param state The new state of the pneumatic system.
     */
    public void set(boolean state) {
        in.set(state); 
        out.set(!state);
    }
    
    /**
     * Gets the state of the input solenoid.
     * @return The state of the input.
     */
    public boolean get() {
        return in.get();
    }
}
