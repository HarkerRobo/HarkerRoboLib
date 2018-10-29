package harkerrobolib.commands

import edu.wpi.first.wpilibj.command.Command
import edu.wpi.first.wpilibj.command.InstantCommand

/**
 * Cancels a command.
 * @author Manan
 */
class CancelCommand
/**
 * Initializes a new CancelCommand to notify the Scheduler to stop a given command.
 * @param c The command that will be canceled.
 */
(val command: Command) : InstantCommand() {

    /**
     * {@inheritDoc}
     */
    override fun initialize() {
        command.cancel()
    }
}
