package org.harker.robotics.harkerrobolib.commands;

import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.command.Command;

/**
 * A command to run another command when a given button is pressed. Note that 
 * the StartWithButtonCommand will end after its given command has finished executing.
 * @author Manan
 * @see StartWithoutButtonCommand
 */
public class StartWithButtonCommand extends Command {
    
    private final Command command;
    private final Button button;
    
    public StartWithButtonCommand(Button button, Command command) {
        this.button = button;
        this.command = command;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if(button.get()) command.start();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return !command.isRunning();
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        command.cancel();
    }
}
