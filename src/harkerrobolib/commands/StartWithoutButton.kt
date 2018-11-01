/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package harkerrobolib.commands

import edu.wpi.first.wpilibj.buttons.Button
import edu.wpi.first.wpilibj.command.Command

/**
 * A command to run another command when a given button is not pressed. Note that
 * the StartWithoutButton will end after its given command has finished executing.
 * @author Manan
 * @see StartWithButton
 */
class StartWithoutButton
/**
 * Constructs a new StartWithButton.
 * @param button the button which, when not pressed, will begin the command.
 * @param command the command which will begin when the given button is not pressed.
 */
(val button: Button, val command: Command) : Command() {
    // Called repeatedly when this Command is scheduled to run
    override fun execute() {
        if (!button.get()) command.start()
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
