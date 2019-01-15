package harkerrobolib.commands;

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
