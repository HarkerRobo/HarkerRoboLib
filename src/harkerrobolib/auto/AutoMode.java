package harkerrobolib.auto;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import harkerrobolib.commands.ThrowExceptionCommand;

public abstract class AutoMode extends CommandGroup {
	public final Command leftAutonNotDefined = 
			new ThrowExceptionCommand ("Left autonomous mode not defined for " + this.getClass().getSimpleName());
	public final Command centerAutonNotDefined =
			new ThrowExceptionCommand ("Center autonomous mode not defined for " + this.getClass().getSimpleName());
	public final Command rightAutonNotDefined = 
			new ThrowExceptionCommand ("Right autonomous mode not defined for " + this.getClass().getSimpleName());
	
	public enum Location {
		LEFT, CENTER, RIGHT
	}

	public AutoMode(Location startLoc, Location endLoc) {
		if (startLoc == Location.LEFT) {addSequential(getLeftCommands(endLoc));}
		else if (startLoc ==Location.CENTER) {addSequential (getCenterCommands(endLoc));}
		else if (startLoc == Location.RIGHT) {addSequential(getRightCommands(endLoc));} 
	}

	/**
	 * Returns the command to be run if the robot begins on the left. 
	 * 
	 * @throws RuntimeException if not overriden and attempted to use
	 * @return the left set of commands
	 */
	public Command getLeftCommands(Location endLoc) {
		return leftAutonNotDefined;
	}
	
	/**
	 * Returns the command to be run if the robot begins on the center.
	 * 
	 * @throws RuntimeException if not overriden and attempted to use
	 * @return the center set of commands
	 */
	public Command getCenterCommands(Location endLoc) {
		return centerAutonNotDefined;
	}	
	
	/**
	 * Returns the command to be run if the robot begins on the right.
	 * 
	 * @throws RuntimeException if not overriden and attempted to use
	 * @return the right set of commands
	 */
	public Command getRightCommands(Location endLoc) {
		return rightAutonNotDefined;
	}
}
