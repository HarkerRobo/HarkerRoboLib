package harkerrobolib.commands

/**
 * Prints a given value to the console.
 * @param value the value to be printed
 * @author Finn Frankis
 * @version 10/17/18
 */
class Print (value: String) : CallMethod({ println(value) })
