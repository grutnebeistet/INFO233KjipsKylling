package game.entity.tiles;

public class IllegalTileException extends Exception {
	/**
	 * autogennet
	 */
	private static final long serialVersionUID = 8643238762087623556L;

	public IllegalTileException(){
		super();
	}
	
	public IllegalTileException(String msg){
		super(msg);
	}
}
