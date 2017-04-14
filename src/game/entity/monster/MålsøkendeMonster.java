package game.entity.monster;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import game.entity.SimplePlayer;
import game.entity.types.Level;
import game.entity.types.Player;
import game.entity.types.Tile;
import game.util.Direction;
import game.util.Mover;
import game.view.gfx.SpriteLoader;

/**
 * Homing monster 
 * @author Adrian Roberts (adrian.roberts@student.uib.no)
 *
 */
public class MålsøkendeMonster extends AbstractMonster {
	private Direction startDir;
	public MålsøkendeMonster(Level level, SpriteLoader loader, int column, int row) {
		super(column, row, level, loader, new Random().nextInt(6));
		startDir = Direction.NORTH;
	}

	@Override
	public void tick() {
		/* Gå tilfeldig retning hver tick 44. tick
		 * Dere bør gi deres monstre mer underholdende kommandoer. */


		/* Bare gjør noe hver nte tick. */
		int nteTick = 44;
		if((tickcounter++ % nteTick) != 0){
			return;
		}

		if(facingDirection == null){
			setDirection(startDir);

		}

		//System.out.println(deltaPM()[0] + " " +deltaPM()[1]);

		Direction[] directions = Direction.values();
		int deltaXY = 0;
		int [] retning = {99,99,99,99};
		int i =0;
		for(Direction d : directions){
			//for(int i =0; i<Direction.values().length; i++){

			int[] pos = Mover.position(d, getColumn(), getRow());
			Tile place = level.tileAt(pos[Mover.COLUMN], pos[Mover.ROW]);

			if(null != place && place.isWalkable() && ! place.isLethal()){
				int placeX, placeY; 
				placeX = place.getColumn();
				placeY = place.getRow();
				deltaXY = deltaPP(placeX, placeY);
				//System.out.println("DELTAXY " + deltaXY);

				retning[i] = deltaXY;

			}

			i++;

		}
		int min = 88;
		int dir = 0;
		for(int j =0; j<retning.length; j++){
			if(retning[j]>=0 && retning[j]<min){
				min = retning[j];
				dir = j;
			}
		}
		
		this.move(directions[dir], level);
		//System.out.println(Arrays.toString(retning) + " RETNIG");


	}

	private int deltaPP(int placeX, int placeY){
		int playerY = SimplePlayer.getInstance().getRow();
		int playerX = SimplePlayer.getInstance().getColumn();


		//		int monsterX = this.getColumn();
		//		int monsterY = this.getRow();

		int deltaX = playerX-placeX; //Math.abs(playerX-monsterX);
		int deltaY = playerY-placeY;// Math.abs(playerY-monsterY);
		int delta = Math.abs(deltaX)+ Math.abs(deltaY);
		return delta;

	}

	@Override
	public byte getPriority() {
		// TODO Auto-generated method stub
		return 0;
	}

}
