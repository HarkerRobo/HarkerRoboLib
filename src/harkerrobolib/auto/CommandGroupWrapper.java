package harkerrobolib.auto;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * 
 * @author Finn Frankis
 * @version 10/18/18
 */
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
