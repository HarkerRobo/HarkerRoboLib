package harkerrobolib.auto;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandGroupBase;
import harkerrobolib.commands.ThrowExceptionCommand;

public abstract class AutoMode extends CommandGroupBase {
  public final Command LEFT_AUTON_NOT_DEFINED =
      new ThrowExceptionCommand(
          "Left autonomous mode not defined for " + this.getClass().getSimpleName());
  public final Command CENTER_AUTON_NOT_DEFINED =
      new ThrowExceptionCommand(
          "Center autonomous mode not defined for " + this.getClass().getSimpleName());
  public final Command RIGHT_AUTON_NOT_DEFINED =
      new ThrowExceptionCommand(
          "Right autonomous mode not defined for " + this.getClass().getSimpleName());

  public enum Location {
    LEFT,
    CENTER,
    RIGHT
  }

  public AutoMode(Location startLoc, Location endLoc) {
    if (startLoc == Location.LEFT) addCommands(getLeftCommands(endLoc));
    else if (startLoc == Location.CENTER) addCommands(getCenterCommands(endLoc));
    else if (startLoc == Location.RIGHT) addCommands(getRightCommands(endLoc));
  }

  /**
   * Returns the command to be run if the robot begins on the left.
   *
   * @throws RuntimeException if not overriden and attempted to use
   * @return the left set of commands
   */
  public Command getLeftCommands(Location endLoc) {
    return LEFT_AUTON_NOT_DEFINED;
  }

  /**
   * Returns the command to be run if the robot begins on the center.
   *
   * @throws RuntimeException if not overriden and attempted to use
   * @return the center set of commands
   */
  public Command getCenterCommands(Location endLoc) {
    return CENTER_AUTON_NOT_DEFINED;
  }

  /**
   * Returns the command to be run if the robot begins on the right.
   *
   * @throws RuntimeException if not overriden and attempted to use
   * @return the right set of commands
   */
  public Command getRightCommands(Location endLoc) {
    return RIGHT_AUTON_NOT_DEFINED;
  }
}
