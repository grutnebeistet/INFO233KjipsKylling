package game.entity.tiles;

import game.entity.TileLevel;
import game.entity.types.Tile;
import game.view.gfx.SpriteLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Dette er en factory som produserer tiles.
 * Det gjør det lett for til dømes {@link TileLevel} å produsere brett.
 * Slik den er nå har den hjelpemetoder for CSV-filer, men ingenting for SQL.
 * Det kan være verdt å lage hjelpemetoder for {@link ResultSet} typen eller lignende.
 * Men det er opp til dere.
 * 
 * @author Haakon Løtveit (haakon.lotveit@student.uib.no)
 *
 */
public class TileFactory {
	private SpriteLoader sprites;

	private Set<String> names;
	private Map<String, Boolean> walkable;
	private Map<String, Boolean> lethal;
	private Map<String, Integer> column; // kolonne i spritesheet, ingenting med plassering på et brett å gjøre
	private Map<String, Integer> row;    // også per spritesheet. 
	private Map<Character, String>  alias;
	private TileBuilder builder;

	private void init(){
		names    = new HashSet<>();
		walkable = new HashMap<>();
		lethal   = new HashMap<>();
		column   = new HashMap<>();
		row      = new HashMap<>();
		alias    = new HashMap<>();
		builder  = new TileBuilder();
	}

	/**
	 * Lager en ny tilefactory, basert på en spesifikk spriteloader.
	 * @param sprites spriteloaderen som skal brukes.
	 */
	public TileFactory(SpriteLoader sprites){
		this.sprites = sprites;
		init();
	}

	/**
	 * Registrerer et alias, som er en mapping fra et tegn til en streng i fabrikken.
	 * Disse aliasene er det som blir brukt til å lese brettfilene.
	 * @param letter tegnet som skal brukes
	 * @param aliasFor flisen den skal bety
	 * @return true dersom den ble registrert, false dersom den ikke ble det. Den kan la vær å bli registrert dersom det allerede finnes et alias med det navnet.
	 * @throws TileNotRegisteredException dersom tegnet peker til en flis fabrikken ikke vet om.
	 */
	public boolean registerAlias(Character letter, String aliasFor) throws TileNotRegisteredException {
		if(!names.contains(aliasFor)) throw new TileNotRegisteredException("Cannot alias a tile that does not yet exist");		

		if(alias.containsKey(letter)){
			return false;
		}

		System.out.printf("[INFO] Aliasing %s to %s%n", letter, aliasFor);
		alias.put(letter, aliasFor);
		return true;
	}

	/**
	 * En hjelpermetode som registrerer mange tegn, basert på det en får ut av {@link CSVReader#readAll()};
	 * @param csvs en liste av arrays av strenger.
	 * @return true dersom alle flisene ble registrert, false ellers.
	 */
	public boolean registerTile(List<String[]> csvs){
		boolean retval = true;

		for(String[] csv : csvs){
			retval &= this.registerTile(csv);
		}

		return retval;
	}

	/**
	 * En hjelpemetode som registrerer et tegn, basert på det en får ut av {@link CSVReader#readNext()}
	 * @param csv String-arrayet som brukes
	 * @return true dersom aliaset ble registrert, false ellers.
	 */
	public boolean registerTile(String[] csv){
		String  name     = csv[0];
		if(names.contains(name)){
			return false;
		}
		names.add(name);

		this.walkable.put(name, Boolean.parseBoolean(csv[1]));
		this.lethal.put(name, Boolean.parseBoolean(csv[2]));
		this.column.put(name, Integer.parseInt(csv[3]));
		this.row.put(name, Integer.parseInt(csv[4]));

		System.out.printf("[INFO] Registered tile %s%n", name);
		return true;
	}

	/**
	 * Lager en ny (statisk) tile basert på data som er registrert.
	 * @param name Navnet på tilen
	 * @param column x-posisjonen til tilen (ikke piksler, men type rad/kolonner)
	 * @param row y-posisjonen til tilen (ikke piksler, men type rad/kolonner)
	 * @return en ny tile, satt til gitt posisjon.
	 * @throws TileNotRegisteredException dersom ingen tile med det navnet er gitt.
	 * @throws IllegalTileException dersom de registrerte dataene er ulovlige.
	 */
	public Tile make(String name, int column, int row) throws TileNotRegisteredException {
		if(!names.contains(name)){
			String error = String.format("Tile \"%s\" is not registered in database. Have you loaded all data correctly?", name);
			throw new TileNotRegisteredException(error);
		}

		/*
		 *  Dette er forhåpentligvis litt lettere å holde orden på
		 *  enn å måtte lese gjennom en lang ugjennomtrengelig  masse av et kall til new StaticTile
		 */
		return builder
				.col(column)
				.row(row)
				.spriteX(this.row.get(name))
				.spriteY(this.column.get(name))
				.spriteloader(this.sprites)
				.walkable(this.walkable.get(name))
				.lethal(this.lethal.get(name))
				.name(name) /* Dette er første argumentet i konstruktøren, men vi kan gi det sist nå. */
				.create();
	}

	/**
	 * Lar deg bruke et forhåndsdefinert alias for å danne tiles.
	 * Laget for å lettere lese inn tiles fra brettfiler.
	 * @param letter tegnet som skal brukes.
	 * @param x raden tilen skal inn i
	 * @param y tilen skal inn i
	 * @return en ny Tile, som om du hadde kalt make med en streng.
	 * @throws TileNotRegisteredException om strengen som aliaset er definert for ikke er funnet
	 * @throws IllegalTileException om de registrerte dataene er ulovlige
	 * @throws AliasNotRegisteredException om du ikke har registrert aliaset.
	 */
	public Tile make(Character letter, int x, int y) throws TileNotRegisteredException, AliasNotRegisteredException {
		if(!alias.containsKey(letter)){
			String error = String.format("Alias \"%c\" is not defined. Have you loaded all data?", letter);
			throw new AliasNotRegisteredException(error);
		}
		return make(alias.get(letter), x, y);
	}

	/**
	 * Hjelpemetode for å laste inn en fil av par (i CSV-format) av tegn og fliser.
	 * @param aliasPairs filen som skal leses inn.
	 * @return true dersom alle ble registrert, false ellers.
	 * @throws TileNotRegisteredException dersom et alias peker til en flis som ikke er registrert.
	 * @throws FileNotFoundException dersom fillen ikke finnes.
	 * @throws IOException dersom parsingen slår feil.
	 */
	public boolean registerAliases(File aliasPairs) throws TileNotRegisteredException, FileNotFoundException, IOException {
		boolean success = true;
		try(CSVReader rdr = new CSVReader(new FileReader(aliasPairs), ',', '"', 1)){
			for(String[] line : rdr.readAll()){
				if(line.length < 2){
					System.err.printf("[ERROR] row has less than two elements. Skipping%n");
					continue;
				}
				if(line[0].length() != 1){
					System.err.printf("[ERROR] %s is not a single character. Skipping.%n", line[0]);
					continue;
				}

				Character c = line[0].charAt(0);
				String name = line[1];

				success &= registerAlias(c, name);

			}
		}

		return success;

	}

	/**
	 * En hjelpemetode for å lese inn fliser (Tiles) fra en CSV-fil.
	 * @param csv filen som skal leses inn.
	 * @return true dersom alle flisene blir registrert, ellers false.
	 */
	public boolean registerTiles(File csv) {
		try(CSVReader reader = new CSVReader(new FileReader(csv), ',', '"', 1)){
			this.registerTile(reader.readAll());
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	/**
	 * Returnerer størrelsen på en flis.
	 * Størrelsen er kvadratisk, og er definert av spriteloaderen.
	 * @return et heltall som representerer størrelsen langs både x-aksen og y-aksen på en flis.
	 */
	public int tilesize() {
		return sprites.tilesize();
	}

	@Override
	public boolean equals(Object obj){
		if(null == obj) return false;
		if(obj instanceof TileFactory){
			TileFactory tf = (TileFactory) obj;
			return this.sprites.equals(tf.sprites) &&
				   this.names.equals(tf.names) &&
				   this.walkable.equals(tf.walkable) &&
				   this.lethal.equals(tf.lethal) &&
				   this.column.equals(tf.column) &&
				   this.row.equals(tf.row) &&
				   this.alias.equals(tf.alias) &&
				   this.builder.equals(tf.builder);
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		int hashCode = 22699;
		hashCode = hashCode * 31 + sprites.hashCode();
		hashCode = hashCode * 31 + names.hashCode();
		hashCode = hashCode * 31 + walkable.hashCode();
		hashCode = hashCode * 31 + lethal.hashCode();
		hashCode = hashCode * 31 + column.hashCode();
		hashCode = hashCode * 31 + row.hashCode();
		hashCode = hashCode * 31 + alias.hashCode();
		hashCode = hashCode * 31 + builder.hashCode();
		return hashCode * 31;
	}
}
