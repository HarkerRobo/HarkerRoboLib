package harkerrobolib.auto;

import edu.wpi.first.wpilibj.command.CommandGroup;

public abstract class AutoMode extends CommandGroup {
	public AutoMode() {
		addCommands();
	}
	
	public enum StartLocation {
		LEFT, CENTER, RIGHT
	}
	
	public abstract void addCommands();
}
