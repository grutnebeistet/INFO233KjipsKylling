package game.entity.types;

/**
 * En klasse som implementerer Tickable kan gjøre ting av seg selv.
 * @author Haakon Løtveit (haakon.lotveit@student.uib.no)
 *
 */
public interface Tickable {
	/**
	 * Gjør noe, hva som er noe avhenger av hva klassen er.
	 */
	public void tick();
}
