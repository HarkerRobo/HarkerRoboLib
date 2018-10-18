package harkerrobolib.auto;

import edu.wpi.first.wpilibj.command.CommandGroup;

public abstract class AutoMode extends CommandGroup {
	public enum StartLocation {
		LEFT, CENTER, RIGHT
	}

	public AutoMode(StartLocation loc) {
		if (loc == StartLocation.LEFT) {addSequential(getLeftCommands());}
		else if (loc == StartLocation.CENTER) {addSequential (getCenterCommands());}
		else if (loc == StartLocation.RIGHT) {addSequential(getRightCommands());} 
	}
	
	public abstract CommandGroup getLeftCommands();
	public abstract CommandGroup getCenterCommands();
	public abstract CommandGroup getRightCommands();
}
