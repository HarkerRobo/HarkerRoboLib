package harkerrobolib.auto;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class CommandGroupWrapper extends CommandGroup {
	public CommandGroupWrapper sequential(Command c) {
		super.addSequential(c);
		return this;
	}
	
	public CommandGroupWrapper parallel (Command c) {
		super.addParallel(c);
		return this;
	}
}
