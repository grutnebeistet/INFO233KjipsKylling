package game.entity.tiles;

public class TileNotRegisteredException extends Exception {
	/**
	 * autogennet
	 */
	private static final long serialVersionUID = 6445167795165690134L;

	public TileNotRegisteredException() {
		super();
	}
	
	public TileNotRegisteredException(String msg){
		super(msg);
	}
	
	public TileNotRegisteredException(String msg, Throwable cause){
		super(msg, cause);
	}
}
