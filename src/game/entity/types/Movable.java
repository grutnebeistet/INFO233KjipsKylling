package game.entity.types;

import game.util.Direction;
/**
 * Noe som er Movable er en gjenstand som kan bevege seg eller bli beveget rundt brettet.
 * 
 * @author Haakon Løtveit (haakon.lotveit@student.uib.no)
 *
 */
public interface Movable {
	/**
	 * Lar objektet forsøke å flytte seg i retningen oppgitt.
	 * Dersom stedet du ender opp på på brettet ikke er walkable (per {@link Tile#isWalkable()}) skal ikke objektet flytte seg.
	 * @param dir retningen du skal flytte deg i
	 * @param l brettet du skal flytte deg på-
	 */
	public void move(Direction dir, Level l);
	/**
	 * Hvor objektet er
	 * @return raden som dette objektet står på
	 */
	public int getRow();
	/**
	 * Hvor objektet er
	 * @return kolonnen som dette objektet står på.
	 */
	public int getColumn();
	/**
	 * Setter 
	 * @param column
	 * @param row
	 */
	public void setPosition(int column, int row);
	/**
	 * Flytter retningen objektet ser i.
	 * @param dir den nye retningen objektet skal se i.
	 */
	public void setDirection(Direction dir);
}
