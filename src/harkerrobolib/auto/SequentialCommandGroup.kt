package harkerrobolib.auto

import edu.wpi.first.wpilibj.command.Command
import edu.wpi.first.wpilibj.command.CommandGroup

/**
 *
 * @author Finn Frankis
 * @version 10/18/18
 */
class SequentialCommandGroup(vararg commands: Command) : CommandGroup() {
    init {
        for (c in commands) {
            addSequential(c)
        }
    }
}
