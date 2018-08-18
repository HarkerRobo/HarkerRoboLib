package harkerrobolib.commands;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Cancels a command.
 * @author Manan
 */
public class CancelCommand extends Command {
    
    private Command c;
    
    /**
     * Initializes a new CancelCommand to notify the Scheduler to stop a given command.
     * @param c The command that will be canceled.
     */
    public CancelCommand(Command c) {
        this.c = c;
    }

    /**
     * {@inheritDoc}
     */
    protected void initialize() {
      
    }

    /**
     * {@inheritDoc}
     */
    protected void execute() {
        c.cancel();
    }

    /**
     * {@inheritDoc}
     */
    protected boolean isFinished() {
        return true;
    }
    
    /**
     * {@inheritDoc}
     */    protected void end() {
    }

     /**
      * {@inheritDoc}
      */
    protected void interrupted() {
    }
}
