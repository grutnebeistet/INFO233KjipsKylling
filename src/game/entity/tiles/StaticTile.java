package game.entity.tiles;

import java.awt.Graphics;

import static game.util.EffectiveJavaHasher.*;
import game.entity.types.Level;
import game.entity.types.Tile;
import game.util.Direction;
import game.util.Mover;
import game.view.gfx.SpriteLoader;

/**
 * En enkel tile i et brett.
 * Disse tilene kan være dødelige (lava, syre), og de kan fungere som vegger.
 * Du må holde orden på ganske mye når en skal lage disse skapningene, derfor er det laget en {@link TileBuilder} som bygger slikt for dere.
 * 
 * @author Haakon Løtveit (haakon.lotveit@student.uib.no)
 *
 */
public class StaticTile implements Tile{
	protected String name;
	protected int row, col, tilesize;
	protected SpriteLoader sl;
	protected int spriteX, spriteY;
	protected boolean walkable, lethal;
	
	/**
	 * Denne er relativt grusom å bruke. Bruk {@link TileBuilder} istedenfor.
	 * Mye enklere å holde orden på ting med.
	 * @param row raden på brettet flisen skal være på
	 * @param col kolonnen på brettet flisen skal være på
	 * @param tilesize størrelsen på flisen
	 * @param spriteloader hvor den skal få fatt på spriten sin fra.
	 * @param spriteX hvilken rad i arket spriten sitter på
	 * @param spriteY hvilken kolonne i arket spriten sitter på
	 * @param walkable om du kan gå på den. Merk at en tile ikke kan være walkable og pushable.
	 * @param lethal om du dør om du tar i den. (Tenk lava, syrebad, etc.)
	 */
	public StaticTile(String name, int row, int col, SpriteLoader spriteloader, int spriteX, int spriteY, boolean walkable, boolean lethal) {
		this.name = name;
		this.row = row;
		this.col = col;
		this.sl = spriteloader;
		this.spriteX = spriteX;
		this.spriteY = spriteY;
		this.walkable = walkable;
		this.lethal = lethal;
		
	}
	
	@Override
	public void render(Graphics gfx) {
		gfx.drawImage(sl.getImage(spriteX, spriteY), col*sl.tilesize(), row*sl.tilesize(), null);
	}

	@Override public String getName(){
		return name;
	}
	
	@Override
	public int getRow() {
		return row;
	}

	@Override
	public int getColumn() {
		return col;
	}

	@Override
	public boolean isLethal() {
		return lethal;
	}

	@Override
	public boolean isWalkable() {
		return walkable;
	}

	@Override
	public boolean equals(Object obj) {
		if(null == obj) return false;
		if(obj instanceof StaticTile){
			StaticTile til = (StaticTile) obj;
			return til.lethal   == this.lethal    &&
				   til.walkable == this.lethal    &&
				   til.col 		== this.col 	  &&
				   til.row 		== this.row 	  &&
				   til.sl 		== this.sl 		  &&
				   til.spriteX  == this.spriteX   &&
				   til.spriteY  == this.spriteY   &&
				   til.tilesize == this.tilesize;
			
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		int hash = 13;	/* Vi har importert EffectiveJavaHashing sine statiske metoder, så vi slipper å skrive EffectiveJavaHashing.hashInteger(1); */
		hash = hash * 31 + hashBoolean(lethal);
		hash = hash * 31 + hashBoolean(walkable);
		hash = hash * 31 + hashInteger(col);
		hash = hash * 31 + hashInteger(row);
		hash = hash * 31 + hashInteger(spriteX);
		hash = hash * 31 + hashInteger(spriteY);
		hash = hash * 31 + hashInteger(tilesize);
		hash = hash * 31 + sl.hashCode();
		return hash * 31;
	}
	
	@Override
	public String toString(){
		return String.format("Static tile at (%d,%d), %s, %s",
				getColumn(), getRow(),
				walkable? "walkable" : "not walkable",
				lethal?   "lethal"   : "not lethal");
	}

}
