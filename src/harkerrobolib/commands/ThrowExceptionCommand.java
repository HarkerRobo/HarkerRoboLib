package harkerrobolib.commands;

import java.io.File;
import java.security.InvalidParameterException;

import edu.wpi.first.wpilibj.command.Command;

public class ThrowExceptionCommand extends CallMethodCommand {

	public ThrowExceptionCommand(RuntimeException e) {
		super(() -> throwException(e));
	}
	
	public ThrowExceptionCommand (String s) {
		this (new RuntimeException(s));
	}
	public static void throwException (RuntimeException e){
		throw e;
	}

}
