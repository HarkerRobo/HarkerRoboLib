package harkerrobolib.commands

import java.util.function.Supplier

import edu.wpi.first.wpilibj.command.InstantCommand

/**
 * Prints a given value to the console.
 * @param value the value to be printed
 * @author Finn Frankis
 * @version 10/17/18
 */
class PrintCommand (value: String) : CallMethodCommand({ println(value) })
