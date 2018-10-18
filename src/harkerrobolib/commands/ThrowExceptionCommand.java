package harkerrobolib.commands;

import java.io.File;
import java.security.InvalidParameterException;

import edu.wpi.first.wpilibj.command.Command;

public class ThrowExceptionCommand extends CallMethodCommand {

	public ThrowExceptionCommand(RuntimeException e) {
		super(() -> throwException(e));
	}
	
	public static void throwException (RuntimeException e){
		throw e;
	}

}
