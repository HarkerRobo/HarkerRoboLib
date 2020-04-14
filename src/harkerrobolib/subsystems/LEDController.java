package harkerrobolib.subsystems;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;

/** 
 * A class that encapsulates dealing with a strip of leds
 * 
 * @author Ada Praun-Petrovic
 * @since April 12, 2020
 */
public class LEDController {

    private AddressableLED m_led;
    private AddressableLEDBuffer m_ledBuffer;

    //led PMW port
    private int portIndex = 0;

    public LEDController() {
        m_led = new AddressableLED(portIndex);

        // Reuse buffer
        // Default to a length of 60, start empty output
        // Length is expensive to set, so only set it once, then just update data
        m_ledBuffer = new AddressableLEDBuffer(60);
        m_led.setLength(m_ledBuffer.getLength());

        // Set the data
        m_led.setData(m_ledBuffer);
        m_led.start();
    }

    // Sets the whole led strip to a single color.
    public void setColor(int redIndex, int greenIndex, int blueIndex) {
        for (var i = 0; i < m_ledBuffer.getLength(); i++) {
            // Sets the specified LED to the RGB values for red
            m_ledBuffer.setRGB(i, redIndex, greenIndex, blueIndex);
         }
         
         m_led.setData(m_ledBuffer);
    }
    
    private void setRainbow(int firstPixelHue, int saturation, int value) {
        int m_rainbowFirstPixelHue = firstPixelHue;
        for (var i = 0; i < m_ledBuffer.getLength(); i++) {
            //calculate the hue
            final var hue = (m_rainbowFirstPixelHue + (i * 180 / m_ledBuffer.getLength())) % 180;
            m_ledBuffer.setHSV(i, hue, saturation, value);
        }
        // Increase by to make the rainbow "move"
        m_rainbowFirstPixelHue += 3;
        // Check bounds
        m_rainbowFirstPixelHue %= 180;
    }

    // Sets the led strip to a rainbow by keeping the same saturation and value and cycling through the hue.
    public void setRainbowPeriodic(int firstPixelHue) {
        // Fill the buffer with a rainbow
        setRainbow(firstPixelHue, 0, 0);
        // Set the LEDs
        m_led.setData(m_ledBuffer);
    }
}