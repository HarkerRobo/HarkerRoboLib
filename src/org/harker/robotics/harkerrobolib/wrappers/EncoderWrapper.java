package org.harker.robotics.harkerrobolib.wrappers;

import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;

/**
 *
 *
 * @author Brian Chan
 */
public class EncoderWrapper extends Encoder {

    private final double rateScale;
    //Rate and acceleration should be in meters per second and meters per second squared respectively
    private double rate = 0;
    private double acceleration = 0;
    //previousCallTime denotes the last time updateRate() was called to be used for acceleration code (See below)
    private double previousCallTime = System.currentTimeMillis() / 1000;

    public EncoderWrapper(int aChannel, int bChannel) {
	super(aChannel, bChannel);
	this.rateScale = 1;
    }

    public EncoderWrapper(int aChannel, int bChannel, double rateScale) {
	super(aChannel, bChannel);
	this.rateScale = rateScale;
    }

    public EncoderWrapper(int aSource, int bSource, final CounterBase.EncodingType encodingType) {
	super(aSource, bSource, false, encodingType);
	this.rateScale = 1;
    }
    
    public EncoderWrapper(int aSource, int bSource, final CounterBase.EncodingType encodingType, double rateScale) {
	super(aSource, bSource, false, encodingType);
	this.rateScale = rateScale;
    }

    public double getRate() {
	return rate * rateScale;
    }

    public double getAcceleration() {
        return acceleration;
    }

    public void updateRate() {
	double curRate = super.getRate();
        double currTime;
	if(!Double.isNaN(curRate))
	    rate += (curRate - rate) / rateScale;
        currTime = System.currentTimeMillis() / 1000; //Convert to seconds
        //Kind of hacky piggyback updating the acceleration as a function of the change in rate
        //versus the time difference between method calls.
        updateAcceleration(currTime - previousCallTime, curRate, rate);
        previousCallTime = currTime; //Update timestep
    }

    /**
     * Finds acceleration as the change in velocity over the change in time
     * @param timeStep The time between the current and previous call of the function.
     * @param oldRate The previous rate of the encoder.
     * @param newRate The current rate of the encoder.
     */
    public void updateAcceleration(double timeStep, double oldRate, double newRate) {
        acceleration = (newRate - oldRate) / timeStep;
    }
}
