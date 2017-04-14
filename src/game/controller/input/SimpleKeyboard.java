package game.controller.input;

import game.controller.Game;
import game.util.Direction;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * En enkel lytter som snakker lytter på et tastatur, og gir meldinger til et game-objekt.
 * 
 * @author Haakon Løtveit (haakon.lotveit@student.uib.no)
 *
 */
public class SimpleKeyboard implements KeyListener {
	Game game;
	
	/**
	 * Lager en ny lytter som kan kommunisere med en game-instans.
	 * @param game instansen som skal kommuniseres med.
	 */
	public SimpleKeyboard(Game game){
		this.game = game;
	}
	
	/**
	 * Når du trykker ned en knapp på tastaturet, når en GUI-ting har fokus som har denne registrert som lytter,
	 * blir denne metoden kjørt.
	 * Slik som det er nå når dere får den, forstår den følgende taster:
	 * <table>
	 * 	   <tr><th>Alternativ 1</th><th>Alternativ 2</th>       <th>Betydning</th></tr>
	 *     <tr><td>W</td>           <td>↑ (Piltast opp)</td>    <td>Gå oppover</td></tr>
	 *     <tr><td>A</td>           <td>← (Piltast venstre)</td><td>Gå til venstre</td></tr>
	 *     <tr><td>S</td>           <td>→ (Piltast høyre)</td>  <td>Gå til høyre</td></tr>
	 *     <tr><td>D</td>           <td>↓ (Piltast ned)</td>    <td>Gå nedover</td></tr>
	 */
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()){
		case KeyEvent.VK_UP:
		case KeyEvent.VK_W:
			game.movePlayer(Direction.NORTH); break;
			
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_A:
			game.movePlayer(Direction.WEST); break;
		
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_D:
			game.movePlayer(Direction.EAST); break;
			
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_S:
			game.movePlayer(Direction.SOUTH); break;
		
		
		default:
			System.out.println("Unknown keypress: " + e);
			break;
		}
	}

	/**
	 * Du kan også velge å gjøre ting når en tast blir sluppet,
	 * men standardimplementasjonen velger å ignorere dette.
	 */
	public void keyReleased(KeyEvent e) {
		return;
	}
	
	/**
	 * Du kan også velge å gjøre ting når en tast blir skrevet,
	 * men standardimplementasjonen velger å ignorere dette.
	 */	
	public void keyTyped(KeyEvent arg0) {
		return;
	}
}
