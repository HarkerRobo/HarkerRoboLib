package harkerrobolib.util;

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

    private final AddressableLED led;
    private final AddressableLEDBuffer ledBuffer;

    private int rainbowFirstPixelHue = 0;

    private final ScheduledExecutorService blinkScheduler = Executors.newScheduledThreadPool(1);
    ScheduledFuture<?> runBlinker;
    private boolean isBlinkInActivePhase;

    private long lastTimeBlinkPeriodicSwitched;

    public LEDController(int portIndex, int bufferLength) {
        led = new AddressableLED(portIndex);

        // Reuse buffer
        // Length is expensive to set, so only set it once, then just update data
        ledBuffer = new AddressableLEDBuffer(bufferLength);
        led.setLength(ledBuffer.getLength());

        // Set the data
        led.setData(ledBuffer);
        led.start();
    }

    // Sets the whole led strip to a single color.
    public void setColor(Color color) {
        for (int i = 0; i < ledBuffer.getLength(); i++) {
            // Sets the specified LED to the color
            ledBuffer.setRGB(i, color.getRed(), color.getGreen(), color.getBlue());
         }
         
         led.setData(ledBuffer);
    }
    
    private void setRainbow(int firstPixelHue, int saturation, int value) {
        for (int i = 0; i < ledBuffer.getLength(); i++) {
            //calculate the hue
            final int hue = (firstPixelHue + (i * 180 / ledBuffer.getLength())) % 180;
            ledBuffer.setHSV(i, hue, saturation, value);
        }
        // Set the LEDs
        led.setData(ledBuffer);
    }

    // Moves the rainbow along the led strip by increasing the hue of the first led.
    public void moveRainbowPeriodic() {
        // Set the leds to a rainbow
        setRainbow(rainbowFirstPixelHue, 100, 255);
        // Increase by to make the rainbow "move"
        rainbowFirstPixelHue += 3;
        // Check bounds
        rainbowFirstPixelHue %= 180;
    }
 
    // Sets the led strip to blink every second
    public void startBlinking(Color color) {
        final Runnable blink = new Runnable() {
            public void run() {
                setColor(isBlinkInActivePhase ? color : Color.BLACK);
                isBlinkInActivePhase = !isBlinkInActivePhase;
            }
        };
        isBlinkInActivePhase = true;
        runBlinker = blinkScheduler.scheduleAtFixedRate(blink, 0, 1, TimeUnit.SECONDS);
    }

    // Cancels the method startBlinking
    public void stopBlinking() {
        if (runBlinker != null) {
            runBlinker.cancel(true);
            runBlinker = null;
        }
    }

    // Sets the led strip to blink if called periodically
    public void blinkPeriodic(Color color, int blinkPeriodMillis) {
        if (System.currentTimeMillis() - lastTimeBlinkPeriodicSwitched >= blinkPeriodMillis) {
            setColor(isBlinkInActivePhase ? color : Color.BLACK);
            isBlinkInActivePhase = !isBlinkInActivePhase;
            lastTimeBlinkPeriodicSwitched = System.currentTimeMillis();
        }
    }
}