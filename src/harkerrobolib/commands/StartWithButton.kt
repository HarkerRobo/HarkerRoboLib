package harkerrobolib.commands

import edu.wpi.first.wpilibj.buttons.Button
import edu.wpi.first.wpilibj.command.Command

/**
 * A command to run another command when a given button is pressed. Note that
 * the StartWithButton will end after its given command has finished executing.
 * @author Manan
 * @see StartWithoutButton
 */
class StartWithButton
/**
 * Constructs a new StartWithButton.
 * @param button the button for which the given command will start.
 * @param command the command which will begin when the given button is pressed.
 */
(val button: Button, val command: Command) : Command() {
    // Called repeatedly when this Command is scheduled to run
    override fun execute() {
        if (button.get()) command.start()
    }

    // Make this return true when this Command no longer needs to run execute()
    override fun isFinished(): Boolean {
        return !command.isRunning
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    override fun interrupted() {
        command.cancel()
    }
}
