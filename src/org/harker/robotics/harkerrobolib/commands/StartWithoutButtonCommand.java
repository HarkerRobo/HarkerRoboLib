/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.harker.robotics.harkerrobolib.commands;

import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.command.Command;

/**
 * A command to run another command when a given button is not pressed. Note that 
 * the StartWithoutButtonCommand will end after its given command has finished executing.
 * @author Manan
 * @see StartWithButtonCommand
 */
public class StartWithoutButtonCommand extends Command {
    
    private final Command command;
    private final Button button;
    
    public StartWithoutButtonCommand(Button button, Command command) {
        this.button = button;
        this.command = command;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if(!button.get()) command.start();
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
