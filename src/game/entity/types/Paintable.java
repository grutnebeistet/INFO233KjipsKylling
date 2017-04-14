package game.entity.types;

import java.awt.Graphics;

/**
 * En klasse som implementerer Paintable kan male seg på brettet og bli sett.
 * 
 * 
 * @author Haakon Løtveit (haakon.lotveit@student.uib.no)
 *
 */
public interface Paintable {
	/**
	 * Maler dette objektet på et lerret, vha. en Graphics.
	 * @param gfx Graphics objektet som brukes til å male med.
	 */
	public void render(Graphics gfx);
}
