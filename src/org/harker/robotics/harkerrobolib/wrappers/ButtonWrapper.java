package org.harker.robotics.harkerrobolib.wrappers;

import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.command.Command;
import org.harker.robotics.harkerrobolib.commands.CancelCommand;


/**
 * Wrapper class for Button that adds cancelWhenReleased and alters whilePressed to be more intuitive
 *
 * @author Manan
 */
abstract class ButtonWrapper extends Button {

    /**
     * Cancels the command when the button is released
     * @param c the command to cancel
     */
    public void cancelWhenReleased(Command c) {
	whenReleased(new CancelCommand(c));
    }

    /**
     * Starts command when button is pressed, and cancels it when button is released
     * @param c the command to run while pressed
     */
    public void whilePressed(Command c) {
	whenPressed(c);
	cancelWhenReleased(c);
    }
}
