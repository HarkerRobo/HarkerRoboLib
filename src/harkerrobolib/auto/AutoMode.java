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
	
	public abstract Command getLeftCommands();
	public abstract Command getCenterCommands();
	public abstract Command getRightCommands();
}
