package game.entity;

public class PlayerNotInstantiatedException extends RuntimeException {
	
	/**
	 * autogennet
	 */
	private static final long serialVersionUID = -6953477107311754055L;
	public PlayerNotInstantiatedException(){
		super();
	}
	public PlayerNotInstantiatedException(String msg){
		super(msg);
	}
	public PlayerNotInstantiatedException(String msg, Throwable cause){
		super(msg, cause);
	}
}
