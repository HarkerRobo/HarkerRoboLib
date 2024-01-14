package harkerrobolib.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * A command to run another command when a given button is not pressed. Note that the
 * StartWithoutButtonCommand will end after its given command has finished executing.
 *
 * @author Manan
 * @author Jatin Kohli
 * @see StartWithButtonCommand
 */
public class StartWithoutButtonCommand extends Command {

  private final Command command;
  private final Trigger button;

  /**
   * Constructs a new StartWithoutButtonCommand.
   *
   * @param button the button which, when not pressed, will begin the command.
   * @param command the command which will begin when the given button is not pressed.
   */
  public StartWithoutButtonCommand(Trigger button, Command command) {
    this.button = button;
    this.command = command;
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  public void execute() {
    if (!button.getAsBoolean()) command.schedule();
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  public boolean isFinished() {
    return command.isFinished();
  }

  @Override
  public void end(boolean interrupted) {
    command.cancel();
  }
}
