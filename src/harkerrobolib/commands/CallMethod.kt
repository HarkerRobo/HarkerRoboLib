package harkerrobolib.commands

import java.util.function.Predicate
import java.util.function.Supplier

import edu.wpi.first.wpilibj.command.Command
import edu.wpi.first.wpilibj.command.InstantCommand

/**
 * Executes a given runnable once.
 * @author Finn Frankis
 * @version 10/18/18
 */
open class CallMethod(val method: () -> Unit) : InstantCommand() {

    public override fun initialize() {
        method.invoke()
    }

}
