package harkerrobolib.wrappers

import edu.wpi.first.wpilibj.buttons.Button
import edu.wpi.first.wpilibj.command.Command
import harkerrobolib.commands.*


/**
 * Wrapper class for Button that adds cancelWhenReleased and alters whilePressed to be more intuitive
 *
 * @author Manan
 */
abstract class ButtonWrapper : Button() {

    /**
     * Cancels the command when the button is released
     * @param c the command to cancel
     */
    fun cancelWhenReleased(c: Command) {
        whenReleased(CancelCommand(c))
    }

    /**
     * Starts command when button is pressed, and cancels it when button is released
     * @param c the command to run while pressed
     */
    fun whilePressed(c: Command) {
        whenPressed(c)
        cancelWhenReleased(c)
    }
}
