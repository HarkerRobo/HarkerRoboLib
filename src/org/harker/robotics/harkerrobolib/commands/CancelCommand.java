package org.harker.robotics.harkerrobolib.commands;

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

    // Called just before this Command runs the first time
    protected void initialize() {
      
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        c.cancel();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
