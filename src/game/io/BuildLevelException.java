package game.io;

public class BuildLevelException extends Exception {

	/**
	 * autogennet
	 */
	private static final long serialVersionUID = 4237489055253451969L;

	public BuildLevelException(){
		super();
	}
	
	public BuildLevelException(String msg){
		super(msg);
	}
	
	public BuildLevelException(String msg, Throwable cause){
		super(msg, cause);
	}
}
