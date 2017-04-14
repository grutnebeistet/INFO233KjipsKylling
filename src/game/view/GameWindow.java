package game.view;

import game.controller.input.SimpleKeyboard;
import game.entity.types.Level;
import game.entity.types.Player;
import game.view.gfx.Lerret;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Vinduet som vises når dere spiller spillet. Det er her lerretet bor.
 * Hvis dere vil ha ting som skal vises til høyre 
 * (til dømes tid som er igjen, eller om dere vil implementere items eller noe,
 * er dette stedet å gjøre det på.)
 *
 * To instanser anses å være like hviss de er samme instans, så equals og hashCode er ikke overkjørt.
 * 
 * @author Haakon Løtveit (haakon.lotveit@student.uib.no)
 *
 */
public class GameWindow extends JFrame {
	/**
	 * autogennet
	 */
	private static final long serialVersionUID = 4499966461488101017L;
	private static final String TITLE = "Kjip's Challenge";
	private SimpleKeyboard keyboard;
	private Player player;
	private Lerret lerret;
	
	public GameWindow(SimpleKeyboard keyboard, Player player){
		super(TITLE);
		this.keyboard = keyboard;
		this.player = player;
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		
	}
	
	public void loadLevel(Level level){
		if(null == lerret){
			this.lerret = new Lerret(level, player, keyboard);
			this.add(lerret);
		}
		this.lerret.setLevel(level);
		this.setSize(0,0);
		this.pack();
		this.setVisible(true);
	}
		
	public boolean popupDeath(){
		Object[] options = {"Restart", "Exit"};
		int choice = JOptionPane.showOptionDialog(this, "You're dead.\nNow what do you do?", "Death and such",
				JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
				null, options, options[0]);
		
		return 0==choice;
	}

	/*
	 * TODO: Selv om disse popupene virker, burde en ha bedre tekst.
	 */
	public void popupHighscores(String msg){
		popupGeneric("Highscores", msg);
	}
	
	public void popupVictory() {
		popupGeneric("Ferdig!", "Brett klart");
	}
	
	public void popupGameComplete() {
		popupGeneric("Spill rundet", "Ferdig!");
	}

	public void popupGeneric(String title, String message){
		JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
	}
}
