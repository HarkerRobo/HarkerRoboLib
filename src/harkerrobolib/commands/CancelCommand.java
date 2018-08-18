package harkerrobolib.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 * Cancels a command.
 * @author Manan
 */
public class CancelCommand extends InstantCommand {
    
    private Command command;
    
    /**
     * Initializes a new CancelCommand to notify the Scheduler to stop a given command.
     * @param c The command that will be canceled.
     */
    public CancelCommand(Command c) {
        command = c;
    }

    /**
     * {@inheritDoc}
     */
    protected void initialize() {
      command.cancel();
    }
}
