package harkerrobolib.auto

import edu.wpi.first.wpilibj.command.Command
import edu.wpi.first.wpilibj.command.CommandGroup

class HSCommandGroup : CommandGroup() {
    fun sequential(c: Command): HSCommandGroup {
        super.addSequential(c)
        return this
    }

    fun parallel(c: Command): HSCommandGroup {
        super.addParallel(c)
        return this
    }
}
