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
	
	public enum StartLocation {
		LEFT, CENTER, RIGHT
	}

	public AutoMode(StartLocation loc) {
		if (loc == StartLocation.LEFT) {addSequential(getLeftCommands());}
		else if (loc == StartLocation.CENTER) {addSequential (getCenterCommands());}
		else if (loc == StartLocation.RIGHT) {addSequential(getRightCommands());} 
	}
	
	/**
	 * Returns the command to be run if the robot begins on the left. 
	 * 
	 * @throws RuntimeException If not overriden and attempted to use,
	 * will throw an exception.
	 * @return the left set of commands
	 */
	public Command getLeftCommands() {
		return leftAutonNotDefined;
	}
	
	/**
	 * Returns the command to be run if the robot begins on the center.
	 * 
	 * @throws RuntimeException If not overriden and attempted to use,
	 * will throw an exception.
	 * @return the center set of commands
	 */
	public Command getCenterCommands() {
		return centerAutonNotDefined;
	}	
	
	/**
	 * Returns the command to be run if the robot begins on the right.
	 * 
	 * @throws RuntimeException If not overriden and attempted to use,
	 * will throw an exception.
	 * @return the right set of commands
	 */
	public Command getRightCommands() {
		return rightAutonNotDefined;
	}
}
