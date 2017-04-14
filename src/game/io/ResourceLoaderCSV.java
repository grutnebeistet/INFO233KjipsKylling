package game.io;

import game.entity.TileLevel;
import game.entity.tiles.AliasNotRegisteredException;
import game.entity.tiles.TileFactory;
import game.entity.tiles.TileNotRegisteredException;
import game.view.gfx.SpriteLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;

/**
 * En ResourceLoader-klasse som laster ressurser fra hardkodede CSV-filer.
 * En stor del av obligen er å skrive ResourceLoaderSQL, som leser fra en SQL-database.
 * 
 * To ResourceLoader-instanser anses å være like hvis og bare hvis de er samme instansen, og derfor er ikke hashCode og equals overkjørt.
 * @author Haakon Løtveit (haakon.lotveit@student.uib.no)
 *
 */
public class ResourceLoaderCSV implements ResourceLoader{
	protected Map<String, SpriteLoader> spriteLoaders;
	protected Map<String, File> levelFiles;
	protected Map<Integer, String> levelNames;
	protected TileFactory factory;
	
	/**
	 * Lager en ny ResourceLoader, basert på hardkodede stier.
	 * Jobben deres i obligen blir å skrive ResourceLoaderSQL-klassen, der dere også implementerer ResourceLoader.
	 * Da kan dere enkelt bytte ut ting i main.Main
	 * @see {@link http://en.wikipedia.org/wiki/Loose_coupling} for hvorfor dette er en god ide.
	 * @throws FileNotFoundException Om noen av filene ikke er der.
	 * @throws IOException Om noe går galt under lesing (CSV-parsing eller lignende)
	 * @throws TileNotRegisteredException om noen av aliasene refererer til fliser som ikke er registrerte på forhånd.
	 */
	public ResourceLoaderCSV() throws FileNotFoundException, IOException, TileNotRegisteredException {
		/* Det er diskutabelt i hvilken grad en bør tillate hardkodede stier, men dere skal tross alt migrere vekk fra dette. */
		File adventure     = new File("res/adventure.csv");
		File alias         = new File("res/alias.csv");
		File spritesheets  = new File("res/spritesheets.csv");
		File standardTiles = new File("res/standard-tiles.csv");
		
		/* Setter opp spriteloaderene */
		spriteLoaders = new HashMap<>();
		try(CSVReader rdr = new CSVReader(new FileReader(spritesheets), ',','"',1)){
			for(String[] row : rdr.readAll()){
				File imagefile = new File(row[1]);
				if(!imagefile.exists()){
					throw new FileNotFoundException(String.format("Kan ikke finne fil \"%s\"", imagefile.getAbsolutePath()));
				}
				System.out.printf("[INFO] SpriteLoader(%s) \"%s\" loaded, with file \"%s\"%n", row[2], row[0], imagefile.getAbsolutePath());
				spriteLoaders.put(row[0], new SpriteLoader(imagefile, Integer.parseInt(row[2])));
			}
		}
		
		/* Setter opp brettene */
		levelFiles = new HashMap<>();
		levelNames = new HashMap<>();
		try(CSVReader rdr = new CSVReader(new FileReader(adventure), ',', '"', 1)){
			for(String[] row : rdr.readAll()){
				Integer number = Integer.parseInt(row[0]);
				String levelName = row[1];
				String levelPath = String.format("res/levels/%s", row[2]);
				File levelFile = new File(levelPath);
				
				if(!levelFile.exists()){
					throw new FileNotFoundException(String.format("File \"%s\" does not exist. Can't load levels", levelPath));
				}
				levelFiles.put(levelName, levelFile);
				levelNames.put(number, levelName);
				System.out.printf("[INFO] Registered level %d (%s) %s%n", number, levelName, levelPath);
			}
		}
		
		/* Setter opp tilefactoryen */
		try {
			factory = new TileFactory(this.getSpriteLoader("tiles"));
		} catch (SpriteSheetNotFoundException e) {
			throw new IOException("res/spritesheets.csv does not contain a definition for a spritesheet called \"tiles\".");
		}
		
		factory.registerTiles(standardTiles);
		factory.registerAliases(alias);
		
		/* T-t-t-t-that's all, folks! */
	}
	
	@Override
	public TileLevel getLevel(String name) throws LevelNotFoundException, BuildLevelException {
		if(!levelFiles.containsKey(name)){
			throw new LevelNotFoundException(String.format("Level \"%s\" not found.%n", name));
		}
		
		try {
			/* her returnerer vi ett nytt level for hver gang. Det er lov å mellomlagre, men vi velger å ikke gjøre det her. */
			return TileLevel.load(getTileFactory(), levelFiles.get(name));
		} catch (FileNotFoundException fnfe) {
			throw new LevelNotFoundException(String.format("Levelfile \"%s\" does not exist, cannot load level", levelFiles.get(name).getAbsolutePath()), fnfe);
		} catch(TileNotRegisteredException | AliasNotRegisteredException e){
			throw new BuildLevelException("Kunne ikke bygge brettet.", e);
		}
		
		
	}

	@Override
	public TileLevel getLevel(int number) throws LevelNotFoundException, BuildLevelException {
		if(!levelNames.containsKey(number)){
			throw new LevelNotFoundException(String.format("No level %d is registered%n", number));
		}
		try{
			return this.getLevel(levelNames.get(number));
		}
		catch(LevelNotFoundException lnfe){
			throw new LevelNotFoundException(
					String.format("Level %d has a name (%s), but that name does not point to a level.",
							number,
							levelNames.get(number)),
					lnfe);
		}
	}

	@Override
	public SpriteLoader getSpriteLoader(String name) throws SpriteSheetNotFoundException {
		if(!spriteLoaders.containsKey(name)){
			throw new SpriteSheetNotFoundException(String.format("No spritesheet with the name %s was found", name));
		}
		
		return spriteLoaders.get(name);
	}

	@Override
	public TileFactory getTileFactory() {
		return factory;
	}

	@Override
	public int getNumLevels() {
		return levelFiles.size();
	}

	@Override
	public void regScore(long timeTaken, int levelNo) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getScore(int levelNo) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
