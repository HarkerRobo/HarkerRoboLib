package harkerrobolib.commands;

import edu.wpi.first.wpilibj2.command.Command;

/**
 * Represents a command to be run indefinitely.
 *
 * @author Finn Frankis
 * @version Aug 17, 2018
 */
public class IndefiniteCommand extends Command {

  /** {@inheritDoc} */
  @Override
  public boolean isFinished() {
    return false;
  }
}
