package harkerrobolib.commands

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
