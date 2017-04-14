package game.io;

import java.sql.SQLException;

import game.entity.tiles.TileFactory;
import game.entity.types.Level;
import game.view.gfx.SpriteLoader;

public interface ResourceLoader {
	/**
	 * Returnerer et brett av typen TileLevel. I hvilken grad en burde ha TileLevel og ikke Level kan diskuteres.
	 * @param name navnet på brettet som skal lastes inn. I CSV-filen er det i kolonnen "name". I SQL-databasen må dere definere navnet selv.
	 * @return en TileLevel instans. Det er ikke garantert at to kall til getLevel med de samme parametrene vil gi det samme objektet (du kan få samme objektet, men må ikke), men det er garantert at de vil være equal. Skal aldri returnere null
	 * @throws LevelNotFoundException Dersom brettet ikke er registrert i loaderen.
	 * @throws BuildLevelException Dersom brettet ikke kan bygges (uregistrerte aliaser, fliser, etc.)
	 * Merk at standardutgaven bruker std.out til å logge hva den laster inn for å gi dere en viss oversikt over hva den vet om.
	 */
	public Level getLevel(String name) throws LevelNotFoundException, BuildLevelException;
	
	/**
	 * Returnerer et brett med et gitt nummer.
	 * @param number nummeret til brettet. Tenk level 1, level 2, og så videre. Dette er i kolonnen level i adventure.csv filen. Hvordan dere gjør det i SQL-løsningen er opp til dere.
	 * @return en TileLevel instans. To kall til getLevel med de samme parametrene skal gi like objekter, og kan, men trenger ikke gi det samme objektet. Skal aldri returnere null.
	 * @throws LevelNotFoundException Dersom brettet ikke er registrert i loaderen.
	 * @throws BuildLevelException Dersom brettet ikke kan bygges (uregistrerte aliaser, fliser, etc.)
	 * Merk at standardutgaven bruker std.out til å logge hva den laster inn for å gi dere en viss oversikt over hva den vet om.
	 */
	public Level getLevel(int number) throws LevelNotFoundException, BuildLevelException;
	
	/**
	 * Returnerer en registrert spriteloader til bruk av andre biter av systemet.
	 * @param name navnet til spriteloaderen. En del navn er magiske, dvs. at de har en spesiell betydning:
	 * <table>
	 *     <tr><th>Navn</th><th>Betydning</th></tr>
	 *     <tr><td>tile</td><td>spriteloaderen som vet om (statiske) tiles</td>
	 *     <tr><td>player</td><td>spriteloaderen som vet om spilleren</td></tr>
	 *     <tr><td>monstre</td><td>spriteloaderen som vet om standard monstre</td></tr>
	 * </table>
	 * @return Spriteloaderen. Aldri null. Dersom du kaller denne metoden flere ganger med samme argumenter <i>er du garantert å få samme objekt</i>.
	 *  Dvs. at loader.getSpriteLoader(etNavn) == loader.getSpriteLoader(etNavn) alltid vil holde.
	 *  Derfor må alle implementasjoner passe på å kunne garantere dette, for eksempel ved mellomlagre.
	 * @throws SpriteSheetNotFoundException dersom det ikke finnes en spriteloader med det navnet spesifisert.
	 */
	public SpriteLoader getSpriteLoader (String name) throws SpriteSheetNotFoundException;

	/**
	 * Tilefactoryen her er den som brukes internt hvis det blir ønskelig. (Td. om dere vil laste brett som ikke er registrert i ResourceLoader objektet.
	 * @return en TileFactory instans. Unikhet garanteres ikke. Det er derimot garantert at alle registrerte tiles og aliaser som er registrert i ResourceLoaderen er tilgjengelige i dette objektet.
	 */
	public TileFactory getTileFactory();
	
	/**
	 * Antall brett som er lastet inn i dette ResourceLoader objektet. 
	 * @return et tall t som er 0 eller større. Det kreves ingen garanti for at brettene fra 1-t skal eksistere, men game.controller.Game later som en slik garanti eksisterer.
	 * Derfor må dere enten endre klassen Game slik at den ikke er avhengig av en slik garanti, eller la deres implementasjon garantere dette.
	 */
	public int getNumLevels();

	/**
	 * lager en highscore
	 * @param timeTaken: tidels sekunder brukt til mål
	 * @param levelNo: banenummer, NOT NULL
	 */
	public void regScore(long timeTaken, int levelNo)throws Exception;

	
	/**
	 * Returnerer en streng av highscores, hentet fra databasen
	 * 
	 * @param levelNo
	 *@return highscore (Streng)
	 */
	public String getScore(int levelNo) throws SQLException;
}
