package harkerrobolib.auto

import edu.wpi.first.wpilibj.command.Command
import edu.wpi.first.wpilibj.command.CommandGroup
import harkerrobolib.commands.ThrowException

abstract class AutoMode(loc: StartLocation,
                        val leftCommands : Command =
                                leftCommandDefault,
                        val centerCommands : Command =
                                centerCommandDefault,
                        val rightCommands : Command =
                                rightCommandDefault) :
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

    companion object {
        val leftCommandDefault = ThrowException("Left autonomous mode not defined.")
        val centerCommandDefault = ThrowException("Center autonomous mode not defined.")
        val rightCommandDefault = ThrowException("Right autonomous mode not defined.")
    }
}
