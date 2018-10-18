package harkerrobolib.commands;

import java.util.function.Predicate;
import java.util.function.Supplier;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 * 
 * @author Finn Frankis
 * @version 10/18/18
 */
public class CallMethodCommand extends InstantCommand {
	Runnable method;
	public CallMethodCommand (Runnable method) {
		this.method = method;
	}
	
	public void initialize() {
		method.run();
	}
	
}
