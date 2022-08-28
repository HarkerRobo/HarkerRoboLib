package harkerrobolib.util;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import java.awt.Color;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Utility class that contains various methods for controlling an LED strip.
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

  /**
   * Creates a new LEDController with a specific PWM port and length (number of individual LEDS).
   */
  public LEDController(int portIndex, int bufferLength) {
    led = new AddressableLED(portIndex);

    // Length is expensive to set, so only set it once, then just update data
    ledBuffer = new AddressableLEDBuffer(bufferLength);
    led.setLength(ledBuffer.getLength());

    led.setData(ledBuffer);
    led.start();
  }

  /** Sets the entire LED strip to a single color. */
  public void setColor(Color color) {
    for (int i = 0; i < ledBuffer.getLength(); i++) {
      // Sets the specified LED to the color
      ledBuffer.setRGB(i, color.getRed(), color.getGreen(), color.getBlue());
    }

    led.setData(ledBuffer);
  }

  /** Sets the LED strip to a rainbow, starting at a specific HSV value. */
  public void setRainbow(int firstPixelHue, int saturation, int value) {
    for (int i = 0; i < ledBuffer.getLength(); i++) {
      // Calculate the hue
      final int hue = (firstPixelHue + (i * 180 / ledBuffer.getLength())) % 180;
      ledBuffer.setHSV(i, hue, saturation, value);
    }
    led.setData(ledBuffer);
  }

  /** Moves the rainbow along the led strip by increasing the hue of the first led. */
  public void moveRainbowPeriodic() {
    // Set the leds to a rainbow
    setRainbow(rainbowFirstPixelHue, 100, 255);
    // Increase by to make the rainbow "move"
    rainbowFirstPixelHue += 3;
    // Check bounds
    rainbowFirstPixelHue %= 180;
  }

  /**
   * Blinks the LED strip for a specified period. The LED strip will continue blinking until stopped
   * by the stopBlinking method.
   */
  public void startBlinking(Color color, int period) {
    final Runnable blink =
        new Runnable() {
          public void run() {
            setColor(isBlinkInActivePhase ? color : Color.BLACK);
            isBlinkInActivePhase = !isBlinkInActivePhase;
          }
        };
    isBlinkInActivePhase = true;
    runBlinker = blinkScheduler.scheduleAtFixedRate(blink, 0, period, TimeUnit.MILLISECONDS);
  }

  /** Stops the blinking of the LEDS, if started with the startBlinking method. */
  public void stopBlinking() {
    if (runBlinker != null) {
      runBlinker.cancel(true);
      runBlinker = null;
    }
  }

  /** Blinks the LEDS when called periodically. */
  public void blinkPeriodic(Color color, int blinkPeriodMillis) {
    if (System.currentTimeMillis() - lastTimeBlinkPeriodicSwitched >= blinkPeriodMillis) {
      setColor(isBlinkInActivePhase ? color : Color.BLACK);
      isBlinkInActivePhase = !isBlinkInActivePhase;
      lastTimeBlinkPeriodicSwitched = System.currentTimeMillis();
    }
  }
}
