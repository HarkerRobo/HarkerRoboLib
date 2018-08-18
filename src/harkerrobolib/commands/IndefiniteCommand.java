package harkerrobolib.commands;

import edu.wpi.first.wpilibj.command.Command;

/**
 * 
 * @author Finn Frankis
 * @version Aug 17, 2018
 */
public class IndefiniteCommand extends Command {

    /**
    * {@inheritDoc}
    */
    @Override
    protected boolean isFinished () {
        return false;
    }
    
}
