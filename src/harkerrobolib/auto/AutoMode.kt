package harkerrobolib.auto

import edu.wpi.first.wpilibj.command.Command
import edu.wpi.first.wpilibj.command.CommandGroup
import harkerrobolib.commands.ThrowException

abstract class AutoMode(loc: StartLocation,
                        val leftCommands : Command =
                                ThrowException("Left autonomous mode not defined."),
                        val centerCommands : Command =
                                ThrowException("Center autonomous mode not defined."),
                        val rightCommands : Command =
                                ThrowException("Right autonomous mode not defined.")) :
        CommandGroup() {

    enum class StartLocation {
        LEFT, CENTER, RIGHT
    }

    init {
        when(loc) {
            StartLocation.LEFT -> addSequential(leftCommands)
            StartLocation.CENTER -> addSequential(centerCommands)
            StartLocation.RIGHT -> addSequential(rightCommands)
        }
    }
}
