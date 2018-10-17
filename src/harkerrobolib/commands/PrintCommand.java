package harkerrobolib.commands;

import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 * Prints a given value to the console.
 * @author Finn Frankis
 * @version 10/17/18
 */
public class PrintCommand extends InstantCommand {
	private String value;
	
	/**
	 * Constructs a new PrintCommand.
	 * @param value the value to be printed
	 */
	public PrintCommand(String value) {
		this.value = value;
	}
	
	public void initialize () {
		System.out.println(value);
	}
}
