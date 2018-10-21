package harkerrobolib.auto;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * 
 * @author Finn Frankis
 * @version 10/18/18
 */
public class ParallelCommandGroup extends CommandGroup {
	public ParallelCommandGroup (Command... commands) {
		for (Command c : commands) {addParallel(c);}
	}
}