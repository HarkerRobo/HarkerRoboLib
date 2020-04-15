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

    public LEDController(final int portIndex, final int bufferLength) {
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
    
    private void setRainbow(final int firstPixelHue, final int saturation, final int value) {
        int m_rainbowFirstPixelHue = firstPixelHue;
        for (var i = 0; i < mLEDBuffer.getLength(); i++) {
            //calculate the hue
            final var hue = (m_rainbowFirstPixelHue + (i * 180 / mLEDBuffer.getLength())) % 180;
            mLEDBuffer.setHSV(i, hue, saturation, value);
        }
        // Increase by to make the rainbow "move"
        m_rainbowFirstPixelHue += 3;
        // Check bounds
        m_rainbowFirstPixelHue %= 180;
    }

    // Sets the led strip to a rainbow by keeping the same saturation and value and cycling through the hue.
    public void setRainbowPeriodic(final int firstPixelHue) {
        // Fill the buffer with a rainbow
        setRainbow(firstPixelHue, 0, 0);
        // Set the LEDs
        mLED.setData(mLEDBuffer);
    }

    //creates a new thread for the execution of blinkColor
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    // Sets the led strip to blink every second for a minute and takes the RBG values of the desired color as arguments
    public void blinkColor(Color color) {
        final Runnable blink = new Runnable() {
            public void run() {
                setColor(color);
            }
        };
        
        final ScheduledFuture<?> runBlinker = scheduler.scheduleAtFixedRate(blink, 1, 1, TimeUnit.SECONDS);

        scheduler.schedule(new Runnable() { 
            public void run() { runBlinker.cancel(true); }
        }, 60, TimeUnit.SECONDS);
    }
}