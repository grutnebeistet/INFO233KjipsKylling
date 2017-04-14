package game.entity.monster;

import game.entity.types.Level;
import game.entity.types.Monster;
import game.util.Direction;
import game.util.Mover;
import game.view.gfx.SpriteLoader;

import java.awt.Graphics;

/**
 * Et enkelt monster dere kan arve fra hvis dere vil gjøre det enkelt med grafikk og slikt.
 * 
 * @author Haakon Løtveit (haakon.lotveit@student.uib.no)
 *
 */
public abstract class AbstractMonster implements Monster{
	protected int column, row, sheetRow, sheetColumn;
	protected final int INIT_COL, INIT_ROW;
	protected Direction facingDirection;
	protected SpriteLoader loader;
	protected Level level;
	protected int tickcounter = 0;

	/**
	 * @param column hvor på brettet monsteret er, (X-aksen)
	 * @param row hvor på brettet monsteret er, (Y-aksen)
	 * @param level brettet hvor monsteret er på
	 * @param loader spriteloaderen for dette monsteret
	 * @param sheetRow hvilken rekke på sheeten vi skal bruke.
	 */
	public AbstractMonster(int column, int row, Level level, SpriteLoader loader, int sheetRow){
		this.column = column;
		this.row = row;
		this.sheetRow = sheetRow;
		this.sheetColumn = 0;
		this.level = level;
		this.loader = loader;
		this.INIT_COL = column;
		this.INIT_ROW = row;
	}
	@Override
	public int startingColumn(){
		return INIT_COL;
	}
	@Override
	public int startingRow(){
		return INIT_ROW;
	}

	@Override
	public void move(Direction dir, Level l) {
		setDirection(dir);
		int[] pos = Mover.position(dir, getColumn(), getRow());
		if(l.walkable(pos[Mover.COLUMN], pos[Mover.ROW])){
			column = pos[Mover.COLUMN];
			row    = pos[Mover.ROW];
		}
	}

	@Override
	public int getRow() {
		return row;
	}

	@Override
	public int getColumn() {
		return column;
	}

	@Override
	public void setPosition(int column, int row) {
		this.column = column;
		this.row = row;
	}

	@Override
	public void setDirection(Direction dir) {
		this.facingDirection = dir;
		switch(dir){
		case NORTH:
			this.sheetColumn = 3; break;
		case WEST:
			this.sheetColumn = 2; break;
		case EAST:
			this.sheetColumn = 1; break;
		case SOUTH:
			this.sheetColumn = 0; break;
		}
	}


	@Override
	public void render(Graphics gfx) {

		gfx.drawImage(loader.getImage(sheetColumn, sheetRow), column * loader.tilesize(), row * loader.tilesize(), null);
	}

	@Override
	public abstract void tick();

	@Override
	public abstract byte getPriority();

}
