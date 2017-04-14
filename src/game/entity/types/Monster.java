package game.entity.types;

/**
 * Et monster er en skapning som beveger seg rundt på brettet som ikke er spilleren.
 * 
 * @author Haakon Løtveit (haakon.lotveit@student.uib.no)
 *
 */
public interface Monster extends Movable, Paintable, Tickable{
	/**
	 * En prioritet sier noe om rekkefølgen de skal får bevege seg i.
	 * Alle monstre med høyere prioritet skal få bevege seg før monstre med lavere prioritet.
	 * Rekkefølgen på to like monstre med samme prioritet er udefinert og dermed opp til deg selv.
	 * 0 er ansett som en normal prioritet.
	 * @return en byte (et heltall mellom 127 og -128 inklusiv) som representerer prioriteten.
	 */
	public byte getPriority();

	int startingColumn();

	int startingRow();
}
