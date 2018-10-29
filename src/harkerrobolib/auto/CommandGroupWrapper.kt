package harkerrobolib.auto

import edu.wpi.first.wpilibj.command.Command
import edu.wpi.first.wpilibj.command.CommandGroup

class CommandGroupWrapper : CommandGroup() {
    fun sequential(c: Command): CommandGroupWrapper {
        super.addSequential(c)
        return this
    }

    fun parallel(c: Command): CommandGroupWrapper {
        super.addParallel(c)
        return this
    }
}
