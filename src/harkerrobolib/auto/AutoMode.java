package harkerrobolib.auto;

import edu.wpi.first.wpilibj.command.Command;
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
	
	public abstract Command getLeftCommands();
	public abstract Command getCenterCommands();
	public abstract Command getRightCommands();
}
