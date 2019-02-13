package harkerrobolib.commands;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Represents a conditional command that uses a lambda to produce conditional values.
 * 
 * @author Chirag Kaushik
 * @since February 12, 2019
 */
public class ConditionalCommand extends edu.wpi.first.wpilibj.command.ConditionalCommand {
    private BooleanSupplier condition;

    public ConditionalCommand(Command trueCommand, BooleanSupplier condition) {
        super(trueCommand);
        this.condition = condition;
    }

    public ConditionalCommand(Command trueCommand, Command falseCommand, BooleanSupplier condition) {
        super(trueCommand, falseCommand);
        this.condition = condition;
    }

    @Override
    public boolean condition() {
        return condition.getAsBoolean();
    }
}