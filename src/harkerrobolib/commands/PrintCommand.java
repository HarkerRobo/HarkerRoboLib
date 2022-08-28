package harkerrobolib.commands;

/**
 * Prints a given value to the console.
 *
 * @author Finn Frankis
 * @version 10/17/18
 */
public class PrintCommand extends CallMethodCommand {
  /**
   * Constructs a new PrintCommand.
   *
   * @param value the value to be printed
   */
  public PrintCommand(String value) {
    super(
        () -> {
          System.out.println(value);
        });
  }
}
