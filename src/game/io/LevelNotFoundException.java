package game.io;

public class LevelNotFoundException extends Exception {

	/**
	 * autogennet
	 */
	private static final long serialVersionUID = 4817992426737017808L;

	public LevelNotFoundException(){
		super();
	}
	
	public LevelNotFoundException(String msg){
		super(msg);
	}
	
	public LevelNotFoundException(String msg, Throwable cause){
		super(msg, cause);
	}
}
