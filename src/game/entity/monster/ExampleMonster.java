package game.entity.monster;

import game.entity.types.Level;
import game.entity.types.Tile;
import game.util.Direction;
import game.util.Mover;
import game.view.gfx.SpriteLoader;

import java.util.ArrayList;
import java.util.Random;

/**
 * Et eksempel på et enkelt monster.
 * Går en tilfeldig rute per tick.
 * 
 * @author Haakon Løtveit (haakon.lotveit@student.uib.no)
 *
 */
public class ExampleMonster extends AbstractMonster {
	private int tickCounter = 0;
	/**
	 * 
	 * @param level brettet monsteret skal være på
	 * @param loader SpriteLoaderen som kan gi monsteret tilgang til spritesene sine.
	 * @param column Kolonnen (X-verdien) som monsteret skal begynne på.
	 * @param row Raden (Y-verdien) som monsteret skal begynne på.
	 */
	public ExampleMonster(Level level, SpriteLoader loader, int column, int row){
		/*
		 * Vi bruker altså den øverste raden i spritesheeten monstre
		 */
		super(column, row, level, loader, new Random().nextInt(6));
	}

	@Override
	public void tick() {
		/* Gå tilfeldig retning hver tick 44. tick
		 * Dere bør gi deres monstre mer underholdende kommandoer. */

		
		/* Bare gjør noe hver nte tick. */
		int nteTick = 44;
		if((tickCounter++ % nteTick) != 0){
			return;
		}
		
		/* Gå i en tilfeldig retning */
		Direction[] directions = Direction.values();
		ArrayList<Direction> placesWeCanGo = new ArrayList<>(4);
		for(Direction d : directions){
			int[] pos = Mover.position(d, getColumn(), getRow());
			Tile place = level.tileAt(pos[Mover.COLUMN], pos[Mover.ROW]);
			if(null != place && place.isWalkable() && ! place.isLethal()){
				placesWeCanGo.add(d);
			}
		}
		Direction walkDir = placesWeCanGo.get(new Random().nextInt(placesWeCanGo.size()));
		this.move(walkDir, level);
	}

	
	@Override
	public byte getPriority(){
		return (byte) 0;
	}
}
