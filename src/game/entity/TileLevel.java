package game.entity;

import static game.util.EffectiveJavaHasher.hashInteger;
import game.entity.monster.ExampleMonster;
import game.entity.monster.MålsøkendeMonster;
import game.entity.monster.VenstreHøyreMonster;
import game.entity.tiles.AliasNotRegisteredException;
import game.entity.tiles.TileFactory;
import game.entity.tiles.TileNotRegisteredException;
import game.entity.types.Level;
import game.entity.types.Monster;
import game.entity.types.Player;
import game.entity.types.Tile;
import game.util.Direction;
import game.view.gfx.SpriteLoader;

import java.awt.Graphics;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * En standard implementasjon av et todimensjonalt brett.
 * Den bruker tiles (fliser) til å male brettet.
 * For å skaffe flisene bruker den en {@link TileFactory}.
 * 
 * @author Haakon Løtveit (haakon.lotveit@student.uib.no)
 *
 */
public class TileLevel implements Level {
	int startCol;
	int startRow;
	int goalCol;
	int goalRow;
	PriorityQueue<Monster> aktivKø = new PriorityQueue<>(), passivKø = new PriorityQueue<>();
	List<Monster> renderList = new LinkedList<>();
	List<Monster> monstre = new ArrayList<>();
	Monster firstMonster, secondMonster;
	
	/*
	 * Fikk dessverre ikke til en velfungerende putmonsters-metode i tide. 
	 */
	
	/**
	 * Leser et brett fra en fil.
	 * @param dispenser fabrikken som skal spytte ut flisene
	 * @param levelFile brettfilen
	 * @return et ferdig TileLevel, klart til bruk.
	 * @throws FileNotFoundException dersom filen ikke blir funnet.
	 * @throws TileNotRegisteredException Dersom en Tile/flis blir referert til som ikke er registrert i dispenser.
	 * @throws AliasNotRegisteredException dersom et alias blir referert til som ikke er registrert i dispenser.
	 */
	public static TileLevel load(TileFactory dispenser, File levelFile) throws FileNotFoundException, TileNotRegisteredException, AliasNotRegisteredException {
		int numCols, numRows, startCol, startRow, goalCol, goalRow;
		String[] wholeMap;
		/* Dere som har tatt inf100 kjenner forhåpentligvis igjen java.util.Scanner.
		 * Scanner er en finfin sak som kan lese fra filer så vel som fra tastaturet.
		 */
		try(Scanner mapreader = new Scanner(levelFile)){
			try(Scanner firstLine = new Scanner(mapreader.nextLine())){

				numCols = firstLine.nextInt();
				numRows = firstLine.nextInt();
				startCol = firstLine.nextInt();
				startRow = firstLine.nextInt();
				goalCol = firstLine.nextInt();
				goalRow = firstLine.nextInt();

				/* Se game.util.IOStuff for mer detaljer*/
				wholeMap = mapreader.useDelimiter("\\Z").next().split("\n"); 

			}
		}

		Tile[][] map = new Tile[numCols][numRows];

		for(int curRow = 0; curRow < numRows; ++curRow){
			String row = wholeMap[curRow];
			String[] columns = row.split("\\s+");

			for(int curCol = 0; curCol < numCols; ++curCol){
				map[curCol][curRow] = dispenser.make(columns[curCol].charAt(0), curCol, curRow);
			}
		}
		return new TileLevel(numCols, numRows, dispenser.tilesize(), startCol, startRow, goalCol, goalRow, map);
	}

	int width;
	int height;
	private int tilesize;
	Tile[][] map; /* map som i geografien på brettet, ikke datastrukturen. */
	Monster theMonster; /* TODO: en del av obligen er å legge til støtte for monstre. Hvordan dere gjør det er opp til dere. Kanskje en datastruktur som ble gjennomgått på forelesning? */

	/**
	 * Standard konstruktør for et brett.
	 * Det er kanskje ikke så veldig relevant å bruke denne, sannsynligvis kommer dere til å holde dere Til TileLevel.loadLevel(TileFactory,File)
	 * @param width antall kolonner.
	 * @param height antall rader
	 * @param tilesize størrelsen på en flis.
	 * @param startCol hvilken kolonne spilleren skal begynne på kartet på.
	 * @param startRow hvilken rad spilleren skal begynne på kartet på.
	 * @param goalCol hvilken kolonne spilleren skal ende opp på.
	 * @param goalRow hvilken rad spilleren skal ende opp på.
	 * @param map en tabell av fliser som er kartet. Det er antatt, men ikke sjekket at tabellen er firkantet.
	 */
	public TileLevel(int width, int height, int tilesize, int startCol, int startRow, int goalCol, int goalRow, Tile[][] map){
		this.startCol = startCol;
		this.startRow = startRow;
		this.goalCol = goalCol;
		this.goalRow = goalRow;
		this.width = width;
		this.height = height;
		this.tilesize = tilesize;
		this.map = map;

		/* 
		 * TODO: Dette må selvsagt vekk og erstattes med en ordentlig måte å populere et kart med monstre på.
		 * Hvordan dere velger å lagre hvor monstre er på et brett er selvsagt helt opp til dere. 
		 */
		putMonster();
	}

	
//	public void putM(){
//		this.secondMonster = getMonster(0);
//		this.firstMonster = getMonster(1);
//		
//	}
//	
//	@Override
//	public void reset(Player player){
//		player.setPosition(getStartingColumn(), getStartingRow());
//		player.setDirection(Direction.SOUTH);
//		putM();
//	}
//	/**
//	 * Metode for å hente bestemt monster
//	 * @param n
//	 * @return monster med bestemt index
//	 */
//	public Monster getMonster(int n){	
//	
//		if(n<0 || monstre.size()<n){
//		return null;
//	}
//		
//	return monstre.get(n);
//		
//	}
//	@Override
//	public void registerMonster(Monster m){ 
//		monstre.add(m);	
//		
//}
	/* DETTE ER IKKE EN GOD PROSEDYRE. DETTE ER EN STYGG HACK FOR Å VISE ET ENKELT MONSTER. */
	private void putMonster(){
		for(int row = 0; row < height; ++row){
			for(int col = 0; col < width; ++col){
				if(col == startCol && row == startRow){
					continue; /* Hopp over der spilleren starter. */
				}
				if(tileAt(col, row).isWalkable() && !tileAt(col, row).isLethal()){
					try {
						theMonster = new MålsøkendeMonster(this, new SpriteLoader(new File("art/monstre.png"), 64), col, row);
						System.out.printf("[HACK] Put a monster, at (%d,%d)%n", col, row);
						return;
					} catch (IOException e) {
						/* Siden dere skal bytte dette ut med noe mer fornuftig, står dette her uten å gjøre noe fornuftig. */
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	private void byttKø(){
		PriorityQueue<Monster> tmp = aktivKø;
		this.aktivKø = passivKø;
		this.passivKø = tmp;
	}
	@Override
	public void reset(Player player){
		player.setPosition(getStartingColumn(), getStartingRow());
		player.setDirection(Direction.SOUTH);
		putMonster();
	}

	@Override
	public void registerMonster(Monster m){
		//				this.theMonster = m;
		aktivKø.add(m);
		renderList.add(m);
	}


	@Override
	public void render(Graphics gfx) {
		for(Tile[] tiles : map){
			for(Tile t : tiles){
				t.render(gfx);
			}
			for(Monster m : renderList){
				m.render(gfx);
			}
		}

		/* TODO: Dette vil jo selvsagt ikke virke når du skal ha flere monstre. */
		theMonster.render(gfx);
	}

	@Override
	public void tick() {
		/* TODO: Du må selvsagt bytte dette ut med en måte å håndtere monstre på. */
		while(!aktivKø.isEmpty()){
			Monster m = aktivKø.poll();
			m.tick();
			passivKø.add(m);
		}
		byttKø();
		//		for(Monster mon : monstre){
		//			mon.tick();
		//		}
		theMonster.tick();
	}

	@Override
	public boolean walkable(int column, int row) {
		if(!(checkLevelBounds(column, row))){
			return false;		
		}
		if(!(map[column][row].isWalkable())){
			return false;
		}

		return true;
	}

	/**
	 * Enkel metode som sjekker om en posisjon er innenfor brettet.
	 * @param column kolonnen vi sjekker
	 * @param row raden vi sjekker
	 * @return
	 */
	protected boolean checkLevelBounds(int column, int row){
		return !(column < 0 || column > width - 1 || row < 0 || row > height - 1);
	}

	@Override
	public Tile tileAt(int column, int row) {
		return checkLevelBounds(column, row)? map[column][row] : null;
	}

	@Override
	public int tileRows() {
		return this.height;
	}

	@Override
	public int tileColumns() {
		return this.width;
	}

	@Override
	public int tilesize() {
		return tilesize;
	}

	@Override
	public int getStartingColumn() {
		return startCol;
	}

	@Override
	public int getStartingRow() {
		return startRow;
	}


	@Override 
	public String toString(){
		return String.format("TileMap (%d×%d), Start: (%d,%d), Goal: (%d,%d)",
				tileColumns(), tileRows(), getStartingColumn(), getStartingRow(), goalCol, goalRow);
	}

	@Override
	public int getGoalColumn() {
		return goalCol;
	}

	@Override
	public int getGoalRow() {
		return goalRow;
	}

	@Override
	public boolean isPlaceDeadly(int column, int row) {
		return  (!checkLevelBounds(column, row) ||
				tileAt(column, row).isLethal() ||
				theMonster.getColumn() == column && theMonster.getRow() == row);
	}

	@Override
	public boolean equals(Object obj){
		if(null == obj) return false;
		if(obj instanceof TileLevel){
			TileLevel tl = (TileLevel) obj;
			return Arrays.deepEquals(tl.map, this.map)    &&
					tl.theMonster.equals(this.theMonster) && /* TODO: Her må dere nok sjekke om hashen er korrekt */
					tl.height   == this.height            &&
					tl.width    == this.width             &&
					tl.startCol == this.startCol          &&
					tl.goalCol  == this.goalCol           &&
					tl.startRow == this.startRow          &&
					tl.goalRow  == this.goalRow;

		}
		return false;
	}

	@Override
	public int hashCode(){
		int hash = 17851; /* Et primtall fra en liste som ble funnet vha. google */
		hash = hash * 31 + theMonster.hashCode(); /* TODO: Dere må fikse denne hashen */
		for(Monster m: renderList){
			hash = hash *31 + m.hashCode(); 
			}
		int mapHash = 19;
		for(Tile[] row : map){
			mapHash = mapHash * 31 + Arrays.hashCode(row);
		}
		hash = hash * 31 + mapHash;
		hash = hash * 31 + hashInteger(height);
		hash = hash * 31 + hashInteger(width);
		hash = hash * 31 + hashInteger(startCol);
		hash = hash * 31 + hashInteger(goalCol);
		hash = hash * 31 + hashInteger(startRow);
		hash = hash * 31 + hashInteger(goalRow);
		return hash * 31;		
	}

	@Override
	public void removeMonsters() {
		aktivKø.removeAll(passivKø);
	}
}

