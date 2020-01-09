package harkerrobolib.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;

/**
 * Executes a given runnable once.
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
