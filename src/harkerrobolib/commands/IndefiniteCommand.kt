package harkerrobolib.commands

import edu.wpi.first.wpilibj.command.Command

/**
 * Represents a command to be run indefinitely.
 * @author Finn Frankis
 * @version Aug 17, 2018
 */
open class IndefiniteCommand : Command() {

    /**
     * {@inheritDoc}
     */
    override fun isFinished(): Boolean {
        return false
    }

}
