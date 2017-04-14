package game.io;

public class SpriteSheetNotFoundException extends Exception {

	/**
	 * autogennet
	 */
	private static final long serialVersionUID = 5702924671967133475L;

	public SpriteSheetNotFoundException(){
		super();
	}
	
	public SpriteSheetNotFoundException(String msg){
		super(msg);
	}
	public SpriteSheetNotFoundException(String msg, Throwable cause){
		super(msg, cause);
	}
}
