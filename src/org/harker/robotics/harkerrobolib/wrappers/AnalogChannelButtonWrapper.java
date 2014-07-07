package org.harker.robotics.harkerrobolib.wrappers;

import edu.wpi.first.wpilibj.AnalogChannel;

/**
 * A wrapper for Analog Channel Buttons to provide boolean outputs.
 * @author 1072
 */
public class AnalogChannelButtonWrapper extends ButtonWrapper {

    private double threshold = 0.5;
    private final int port;
    private final AnalogChannel channel;

    public AnalogChannelButtonWrapper(int port) {
        this.port = port;
        channel = new AnalogChannel(port);
    }
    
    public AnalogChannelButtonWrapper(int port, double threshold) {
        this.port = port;
        this.threshold = threshold;
        channel = new AnalogChannel(port);
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
