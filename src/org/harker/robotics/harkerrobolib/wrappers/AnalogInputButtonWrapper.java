package org.harker.robotics.harkerrobolib.wrappers;

import edu.wpi.first.wpilibj.AnalogInput;

/**
 * A wrapper for Analog Channel Buttons to provide boolean outputs.
 * @author 1072
 */
public class AnalogInputButtonWrapper extends ButtonWrapper {

    private double threshold = 0.5;
    private final int port;
    private final AnalogInput channel;

    public AnalogInputButtonWrapper(int port) {
        this.port = port;
        channel = new AnalogInput(port);
    }
    
    public AnalogInputButtonWrapper(int port, double threshold) {
        this.port = port;
        this.threshold = threshold;
        channel = new AnalogInput(port);
    }

    public boolean get() {
        channel.updateTable();
        return channel.getValue() > threshold;
    }
    
    public void setThreshold(double newVal) { 
        threshold = newVal; 
    }
    
    public double getThreshold() {
        return threshold;
    }
}
