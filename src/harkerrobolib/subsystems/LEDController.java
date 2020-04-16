package harkerrobolib.subsystems;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import java.awt.Color;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/** 
 * Instantiates methods for basic functionalities of an led strip
 * 
 * @author Ada Praun-Petrovic
 * @since April 12, 2020
 */
public class LEDController {

    private final AddressableLED mLED;
    private final AddressableLEDBuffer mLEDBuffer;
    private int mRainbowFirstPixelHue = 0;
    private boolean mIsBlinkInActivePhase;
    ScheduledFuture<?> runBlinker;
    private long mLastTimeBlinkPeriodicSwitched;

    private static final int BLINK_PERIOD_MILLIS = 1 * 1000;

    public LEDController(int portIndex, int bufferLength) {
        mLED = new AddressableLED(portIndex);

        // Reuse buffer
        // Length is expensive to set, so only set it once, then just update data
        mLEDBuffer = new AddressableLEDBuffer(bufferLength);
        mLED.setLength(mLEDBuffer.getLength());

        // Set the data
        mLED.setData(mLEDBuffer);
        mLED.start();
    }

    // Sets the whole led strip to a single color.
    public void setColor(Color color) {
        for (int i = 0; i < mLEDBuffer.getLength(); i++) {
            // Sets the specified LED to the color
            mLEDBuffer.setRGB(i, color.getRed(), color.getGreen(), color.getBlue());
         }
         
         mLED.setData(mLEDBuffer);
    }
    
    private void setRainbow(int firstPixelHue, int saturation, int value) {
        for (int i = 0; i < mLEDBuffer.getLength(); i++) {
            //calculate the hue
            final int hue = (firstPixelHue + (i * 180 / mLEDBuffer.getLength())) % 180;
            mLEDBuffer.setHSV(i, hue, saturation, value);
        }
        // Set the LEDs
        mLED.setData(mLEDBuffer);
    }

    // Moves the rainbow along the led strip by increasing the hue of the first led.
    public void moveRainbow() {
        // Set the leds to a rainbow
        setRainbow(mRainbowFirstPixelHue, 100, 255);
        // Increase by to make the rainbow "move"
        mRainbowFirstPixelHue += 3;
        // Check bounds
        mRainbowFirstPixelHue %= 180;
    }


    //creates a new thread for the execution of blinkColor
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
 
    /**
     * Sets the led strip to blink every second for a minute and takes the RBG values of the desired color as arguments
     */
    public void startBlinking(Color color) {
        final Runnable blink = new Runnable() {
            public void run() {
                setColor(mIsBlinkInActivePhase ? color : Color.BLACK);
                mIsBlinkInActivePhase = !mIsBlinkInActivePhase;
            }
        };
        mIsBlinkInActivePhase = true;
        runBlinker = scheduler.scheduleAtFixedRate(blink, 0, 1, TimeUnit.SECONDS);
    }

    public void stopBlinking() {
        if (runBlinker != null) {
            runBlinker.cancel(true);
            runBlinker = null;
        }
    }

    public void blinkPeriodic(Color color) {
        if (System.currentTimeMillis() >= mLastTimeBlinkPeriodicSwitched + BLINK_PERIOD_MILLIS) {
            setColor(mIsBlinkInActivePhase ? color : Color.BLACK);
            mIsBlinkInActivePhase = !mIsBlinkInActivePhase;
            mLastTimeBlinkPeriodicSwitched = System.currentTimeMillis();
        }
    }
}