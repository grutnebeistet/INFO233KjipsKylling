package game.controller;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import game.controller.input.SimpleKeyboard;
import game.entity.SimplePlayer;
import game.entity.types.Level;
import game.entity.types.Monster;
import game.entity.types.Player;
import game.io.BuildLevelException;
import game.io.LevelNotFoundException;
import game.io.ResourceLoader;
import game.io.SpriteSheetNotFoundException;
import game.util.Direction;
import game.view.GameWindow;

/**
 * Denne klassen er en kontroller (of sorts) for spillet.
 * Den laster inn data vha. en ResourceLoader, og starter ting.
 * To instanser anses å være like hviss de er samme objekt.
 * 
 * @author Haakon Løtveit (haakon.lotveit@student.uib.no)
 *
 */
public class Game {
	protected Player player;
	protected SimpleKeyboard keyboard;
	protected ResourceLoader loader;
	protected GameWindow window;
	protected Level level;
	protected int currentLevel = 0;
	protected Statement stmt;
	protected ResultSet resultSet;
	protected Hashtable<String, Monster> table;
	/**
	 * Skaper en ny kontroller.
	 * Game er ansvarlig for å starte ting, og å holde orden på brett og slikt.
	 * Game har derimot intet ansvar for hvordan ting blir malt til skjermen.
	 * En mulighet for å laste inn flere monstre kan være å utvide ResourceLoader til å holde orden på monstre, og la game registrere dem hos brettene.
	 * @param loader en ResourceLoader som Game kan bruke til å laste inn ting til senere bruk.
	 * @throws SpriteSheetNotFoundException Dersom spillerens spritesheet ikke kan lastes inn.
	 */
	public Game(ResourceLoader loader) throws SpriteSheetNotFoundException {
		this.loader = loader;
		this.keyboard = new SimpleKeyboard(this);
		SimplePlayer.init(loader.getSpriteLoader("player"));
		this.player = SimplePlayer.getInstance();		
		window = new GameWindow(keyboard, player);
	
	}

	/**
	 * Lar deg flytte brukeren. Til dømes kan en tastaturlytter kalle denne metoden. (SimpleKeyboard gjør dette)
	 * Playerobjektet er selv ansvarlig for å sjekke om den kan gå dit eller ikke.
	 * @param where retningen spilleren skal bevege seg i.
	 */
	public void movePlayer(Direction where){
		player.move(where, level);
	}

	/**
	 * Starter spillet. For hvert brett laster det inn brettet vha. ResourceLoaderen og lar deg spille det brettet til du vinner.
	 * Hvis du dør kan du restarte, etc.
	 * Når du har rundet spillet, avslutter start();
	 * @throws LevelNotFoundException Dersom et av brettene ikke blir funnet.
	 * 	Nåværende start() vil begynne på første brett (1) og deretter inkremementere til siste brett. 
	 *  (Siste brett blir funnet vha. et kall til {@link ResourceLoader#getNumLevels()}. Hvis dere ikke følger konvensjonen med brett 1,2,…,n vil ikke denne metoden virke korrekt.) 
	 * @throws BuildLevelException Dersom lastingen av et brett går galt. Se: {@link ResourceLoader#getLevel(int)} for detaljene
	 */
	public void start() throws LevelNotFoundException, BuildLevelException {
		currentLevel = 1;

		while(currentLevel <= loader.getNumLevels()){
			/* Sett brettet til nytt brett hver gang du klarer ett brett.
			 * Her er det plenty med muligheter til utvidelse av logikken. */

			level = loader.getLevel(currentLevel);
			startLevel();

			long timestamp = System.nanoTime(); 				  /* Aldri bruk System.currentTimeMillis() til denne type ting. Tenk om du sitter på et tog, krysser en tidssone, og så krasjer spillet. */
			int timesPerSecond = 120;                            /* Hvor mange ganger i sekundet entiteter som ikke er spilleren får gjøre noe. */
			long tickFrequency = 1_000_000_000L / timesPerSecond; /* Her har vi tap av presisjon grunnet heltallsdivisjon. For vårt bruk er dette greit. */
			long levelStartedAt = timestamp;



			/* Sjekker at du ikke har vunnet. Da skal spillet laste neste brett. */
			while(!(player.getColumn() == level.getGoalColumn() && 
					player.getRow()    == level.getGoalRow())){

				/* Sjekker om spilleren har gått på noe dødelig */
				if(level.isPlaceDeadly(player.getColumn(), player.getRow())){
					boolean restart = window.popupDeath();
					if(restart){
						level.reset(player);
						window.loadLevel(level);
						continue;
					}
					else{
						return;
					}
				}

				long timeSinceLastOp = System.nanoTime() - timestamp;

				if(timeSinceLastOp >= tickFrequency){
					level.tick();
					timestamp = System.nanoTime();
				}
			}

			long timeTaken = (System.nanoTime() - levelStartedAt) / 100_000_000L;


			//Registrerer tid/score og bane
			try {
				loader.regScore(timeTaken, currentLevel);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String highscores = "uferdig";
			try {
				switch (currentLevel) {
				case 1:

					break;

				default:
					break;
				}
				highscores = loader.getScore(this.currentLevel);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			window.popupVictory();
			window.popupHighscores(highscores);
			currentLevel++;

		}

		window.popupGameComplete();


	}
	public void setMonsters() throws IOException, SQLException{

		String state = queryMonster();
		int levels = queryLevel();

		if(levels==1 && state.equals("LeftRightMonster") || state.equals("UpDownMonster") ){

			Monster monsterOne = table.get("LeftRightMonster");
			Monster monsterTwo = table.get("UpDownMonster");
			level.registerMonster(monsterOne);
			level.registerMonster(monsterTwo);
		}else if(levels==2 && state.equals("HomingMonster") || state.equals("PatrolMonster")){

			Monster monsterOne = table.get("HomingMonster");
			Monster monsterTwo = table.get("PatrolMonster");
			level.registerMonster(monsterOne);
			level.registerMonster(monsterTwo);
		}else if(levels==3 && state.equals("LeftMonster") || state.equals("RightMonster")){

			Monster monsterOne = table.get("LeftMonster");
			Monster monsterTwo = table.get("RightMonster");
			level.registerMonster(monsterOne);
			level.registerMonster(monsterTwo);
		}

	}
	public String queryMonster() throws SQLException{

		resultSet = stmt.executeQuery("SELECT monsterName"
				+ " FROM monsters,levels "
				+ "WHERE monsters.lvl = levels.level AND level="+currentLevel);
		String monster = null;
		while (resultSet.next()) {
			monster = resultSet.getString(1);
			//System.out.println(monster);
		}	
		return monster;	
	}
	public int queryLevel() throws SQLException{

		resultSet = stmt.executeQuery("SELECT levelName"
				+ " FROM monsters,levels "
				+ "WHERE monsters.lvl = levels.level AND level="+currentLevel);
		int level = 0;
		while (resultSet.next()) {
			level = resultSet.getInt(1);
			//System.out.println(monster);
		}	
		return level;	
	}

	private void startLevel(){
		level.reset(player);
		window.loadLevel(level);
		switch (this.currentLevel) {
		case 1:
			window.popupGeneric("Gestapo!", "Find your way out of The Labyrinth!");
			break;
		case 2:
			window.popupGeneric("Gestapo!", "Get ready for some Monster-madness!");
			break;
		case 3:
			window.popupGeneric("Gestapo!", "'Simple grey' they say - simpel?");
			break;
		case 4: 
			window.popupGeneric("Gestapo!", "No swimming!");
			break;
		default:
			break;
		}

	}

	public void shutdown(){
		window.dispose();
		window = null;
	}
}
