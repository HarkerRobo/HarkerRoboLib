package harkerrobolib.auto;

import edu.wpi.first.wpilibj.command.CommandGroup;

public abstract class AutoMode extends CommandGroup {
	public AutoMode() {
		addCommands();
	}
	public abstract void addCommands();
}
