package game.io;

import game.entity.TileLevel;
import game.entity.tiles.AliasNotRegisteredException;
import game.entity.tiles.TileFactory;
import game.entity.tiles.TileNotRegisteredException;
import game.entity.types.Level;
import game.view.gfx.SpriteLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import au.com.bytecode.opencsv.CSVReader;

public class ResourceLoaderSQL implements ResourceLoader {

	protected Map<String, SpriteLoader> spriteLoaders;
	protected Map<String, File> levelFiles;
	protected Map<Integer, String> levelNames;
	protected TileFactory tileFac = null;
	protected Connection connection;
	//	protected java.sql.Statement stmt;
	protected Statement stmt;
	protected ResultSet resultSet;
	protected String driver = "org.apache.derby.jdbc.EmbeddedDriver";

	public ResourceLoaderSQL() throws Exception {

		String protocol = "jdbc:derby:"; 				 	      
		String databaseNavn = "database";

		String jdbcUrl = protocol + databaseNavn + ";create=true";
		this.connection = DriverManager.getConnection(jdbcUrl);


		DatabaseMetaData dbm = connection.getMetaData();
		ResultSet highscoreTable = dbm.getTables(null, null, "HIGHSCORE", null);
		
		/*
		 * Her blir det litt rot:
		 * Selv om databasen allerede eksisterer, kjøres loadDatabase uansett (= kræsj) (?). 
		 * Highscoretabell prøves alltid å opprettes, som om (den utkommenterte) if-setningen ignoreres
		 * Leverer oppgaven med en Highscore-tabell opprettet for å unngå crash.
		 */

	
	//	if(!((new File(databaseNavn).exists()) && new File(databaseNavn).isDirectory())){ //if !exists
			
		//if(!highscoreTable.next()){                                      // Hvorfor kjører løkken selv når highscoreTable/databasen finnes?
			System.out.println(!highscoreTable.next() + " highscore finnes ikke   " + highscoreTable.next() + " finnes");
			//Begge disse sysoutene evalueres til 'false' ???

			Class.forName(driver).newInstance();
			System.out.println("[INFO] Loading CSV files...");
			this.loadDatabase("sql/CreateAdventureTable.sql", "sql/insertAdventure.sql", "res/adventure.csv", "adventure");	
			this.loadDatabase("sql/CreateSpritesheetsTable.sql", "sql/Insert-spritesheets.sql", "res/spritesheets.csv", "spritesheets");
			this.loadDatabase("sql/CreateStandardTilesTable.sql", "sql/insertStandardTiles.sql", "res/standard-tiles.csv", "standard-tiles");
			this.loadDatabase("sql/CreateAliasTable.sql", "sql/insertAlias.sql", "res/alias.csv", "alias");
					//	if(!highscoreTable.next()){
//			try (java.sql.PreparedStatement ps = connection.prepareStatement(readFile(new File("sql/CreateHighscores.sql")))) {
//				ps.execute();
//				}
			//}
		}

	//}

	private void loadDatabase(String createTable, String insertTable, String sourceCSV, String nameOfCSV) throws Exception {
		stmt = connection.createStatement();
		try {
			if (nameOfCSV.equals("spritesheets")) {
				stmt.executeUpdate("DROP TABLE spritesheetsTable");
			} else if (nameOfCSV.equals("adventure")) {
				stmt.executeUpdate("DROP TABLE adventureTable");
			} else if (nameOfCSV.equals("standard-tiles")) {
				stmt.executeUpdate("DROP TABLE standardtilesTable");
			} else if (nameOfCSV.equals("alias")) {
				stmt.executeUpdate("DROP TABLE aliasTable");
			}
		} catch(SQLSyntaxErrorException e) {
			System.out.println("[INFO] No appropriate table exists, so one will now be generated.");
		}

		// Create given table
		try (PreparedStatement statement = connection.prepareStatement(readFile(new File(createTable)))) {
			statement.execute();
		}

		// Loads given table with CSV data
		try (PreparedStatement statement = connection.prepareStatement(readFile(new File(insertTable)))) {
			try (CSVReader reader = new CSVReader(new FileReader(new File(sourceCSV)), ',', '"', 1)) {
				for (String[] row : reader.readAll()) {
					if (nameOfCSV.equals("alias")) {

						if (tileFac == null) {
							tileFac = new TileFactory(this.getSpriteLoader("tiles"));
						}

						statement.setString(1, row[0]);
						statement.setString(2, row[1]);
						statement.execute();

						resultSet = stmt.executeQuery("SELECT alias, navn FROM aliasTable");

						while (resultSet.next()) {
							tileFac.registerAlias(resultSet.getString("alias").charAt(0), resultSet.getString("navn"));
						}

					} else if (nameOfCSV.equals("spritesheets")) {
						spriteLoaders = new HashMap<>();

						statement.setString(1, row[0]);
						statement.setString(2, row[1]);
						statement.setInt(3, Integer.valueOf(row[2]));
						statement.execute();

						resultSet = stmt.executeQuery("SELECT name, spritesheetloc, tilesize FROM spritesheetsTable");

						while (resultSet.next()) {
							spriteLoaders.put(resultSet.getString("name"), new SpriteLoader(new File(resultSet.getString("spritesheetloc")), resultSet.getInt("tilesize")));
						}

					} else if (nameOfCSV.equals("adventure")) {
						levelFiles = new HashMap<>();
						levelNames = new HashMap<>();

						statement.setInt(1, Integer.valueOf(row[0]));
						statement.setString(2, row[1]);
						statement.setString(3, row[2]);
						statement.execute();

						resultSet = stmt.executeQuery("SELECT level, name, file FROM adventureTable");
						while (resultSet.next()) {
							levelFiles.put(resultSet.getString("name"), new File("res/levels/" + resultSet.getString("file")));
							levelNames.put(resultSet.getInt("level"), resultSet.getString("name"));
						}

					} else if (nameOfCSV.equals("standard-tiles")) {

						if (tileFac == null) {
							tileFac = new TileFactory(this.getSpriteLoader("tiles"));
						}

						statement.setString(1, row[0]);
						statement.setString(2, row[1]);
						statement.setString(3, row[2]);
						statement.setString(4, row[3]);
						statement.setString(5, row[4]);
						statement.execute();


						resultSet = stmt.executeQuery("SELECT navn, walkable, lethal, spritecol, spriterow FROM standardtilesTable");

						while (resultSet.next()) {
							tileFac.registerTile(new String[] { resultSet.getString("navn"), resultSet.getString("walkable"), resultSet.getString("lethal"), resultSet.getString("spritecol"), resultSet.getString("spriterow") });
						}
					}
				}
			}
		}
	}

	public void regScore(long time, int levelNo)throws Exception{

		try {
			java.sql.PreparedStatement ps = connection.prepareStatement(readFile(new File("sql/InsertHigscore.sql")));
			ps.setLong(1, time);
			ps.setString(2, levelNames.get(levelNo));
			ps.executeUpdate();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public String getScore(int levelNo) throws SQLException{
		try {
			resultSet = stmt.executeQuery("SELECT score, level FROM HIGHSCORE");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<String> highscoreList = new LinkedList<>();
		String highscoresS = "";
		while(resultSet.next()){
			int i =1;
			String level = levelNames.get(levelNo);
			if(resultSet.getString(i+1).equals(level)){
				highscoreList.add(resultSet.getString(i));
			}
			++i;
		}
		String current = highscoreList.get(highscoreList.size()-1).toString();
		Collections.sort(highscoreList);
		int i =0;
		while(i < highscoreList.size() && i < 10){
			//highscoresS += s + " tidels sekund \n";
			highscoresS += highscoreList.get(i)+ " tidels sekund \n";
			i++;
		}
		highscoresS += "Your score: " + current + " tidels sekund";
		return highscoresS;
	}
	private static String readFile(File file) throws Exception {
		String contents = "";
		try (Scanner sc = new Scanner(file)) {
			contents = sc.useDelimiter("\\Z").next();
		}
		return contents;
	}

	@Override
	public TileLevel getLevel(String name) throws LevelNotFoundException, BuildLevelException {
		if (!levelFiles.containsKey(name)) {
			throw new LevelNotFoundException(String.format("Level \"%s\" not found.%n", name));
		}

		try {
			return TileLevel.load(getTileFactory(), levelFiles.get(name));
		} catch (FileNotFoundException fnfe) {
			throw new LevelNotFoundException(String.format("Levelfile \"%s\" does not exist, cannot load level", levelFiles.get(name).getAbsolutePath()), fnfe);
		} catch (TileNotRegisteredException | AliasNotRegisteredException e) {
			throw new BuildLevelException("Kunne ikke bygge brettet.", e);
		}

	}

	@Override
	public TileLevel getLevel(int number) throws LevelNotFoundException, BuildLevelException {
		if (!levelNames.containsKey(number)) {
			throw new LevelNotFoundException(String.format("No level %d is registered%n", number));
		}
		try {
			return this.getLevel(levelNames.get(number));
		} catch (LevelNotFoundException lnfe) {
			throw new LevelNotFoundException(String.format("Level %d has a name (%s), but that name does not point to a level.", number, levelNames.get(number)), lnfe);
		}
	}

	@Override
	public SpriteLoader getSpriteLoader(String name) throws SpriteSheetNotFoundException {
		if (!spriteLoaders.containsKey(name)) {

			throw new SpriteSheetNotFoundException(String.format("No spritesheet with the name %s was found", name));
		}

		return spriteLoaders.get(name);
	}

	@Override
	public TileFactory getTileFactory() {
		return tileFac;
	}

	@Override
	public int getNumLevels() {
		return levelFiles.size();
	}
}

