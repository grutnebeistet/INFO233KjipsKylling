package game.entity.types;

import game.entity.TileLevel;

/**
 * Dette representerer et todimensjonalt brett.
 * Det er en implementasjon {@link TileLevel} som dere kan bruke.
 * Å fikse på {@link TileLevel} er kun nødvendig for å fikse inn fler monstre.
 * 
 * 
 * @author Haakon Løtveit (haakon.lotveit@student.uib.no)
 *
 */
public interface Level extends Paintable, Tickable {
	/**
	 * Kan noe gå på denne posisjonen?
	 * @param column kolonnen i kartet
	 * @param row raden
	 * @return true hvis du kan gå der. false ellers.
	 */
	public boolean walkable(int column, int row);
	
	/**
	 * Returnerer en tile på et gitt punkt.
	 * @param column kolonnen
	 * @param row rekken
	 * @return en Tile av et slag som er på det gitte punktet.
	 */
	public Tile tileAt(int column, int row);
	
	/**
	 * Hvor mange rader er det?
	 * @return et tall som representerer høyden på brettet. Hvis du vil ha høyden i piksler må du gange med {@link Level#tilesize()}
	 */
	public int tileRows();
	/**
	 * Hvor mange kolonner er det?
	 * @return et tall som representerer bredden på brettet. Hvis du vil ha bredden i piksler må du gange med {@link Level#tilesize()}
	 */
	public int tileColumns();
	/**
	 * Hvor mange piksler en tile er.
	 * En tile er like bred som den er høy, så dette tallet gjelder for både bredde og høyde.
	 * @return bredde/høyde på en tile i piksler.
	 */
	public int tilesize();
	/**
	 * Hvor skal spilleren begynne?
	 * @return kolonnen spilleren begynner på. 0 er den lengst til venstre.
	 */
	public int getStartingColumn();
	/**
	 * Hvor skal spilleren begynne?
	 * @return raden spilleren begynner på. 0 er den øverste.
	 */
	public int getStartingRow();
	/**
	 * Hvor er mål?
	 * @return kolonnen spilleren skal nå. 0 er den lengst til venstre.
	 */
	public int getGoalColumn();
	/**
	 * Hvor er mål?
	 * @return raden spilleren skal nå. 0 er den øverste.
	 */
	public int getGoalRow();
	/**
	 * Legg til et monster
	 * @param monster monsteret som skal legges til.
	 */
	public void registerMonster(Monster monster);
	/**
	 * Fjern alle monstre
	 */
	public void removeMonsters();
	/**
	 * Kan en spiller stå på gitt sted uten å dø?
	 * @param column kolonnen, 0 er lengst til venstre
	 * @param row raden, 0 er øverste
	 * @return true dersom Tilen på stedet er deadly per {@link Tile#isLethal()} eller det er et monster der. Ellers false;
	 */
	public boolean isPlaceDeadly(int column, int row);
	/**
	 * Nullstiller brettet, og setter spilleren på startpunktet.
	 * @param player Spilleren som skal på brettet.
	 */
	public void reset(Player player);
}
