package game.entity.monster;

import java.util.Random;

import game.entity.types.Level;
import game.util.Direction;
import game.view.gfx.SpriteLoader;

/*
 * @author Adrian Roberts (adrian.roberts@student.uib.no)
 * Følger en gitt patrulje. En patrulje er en streng med en av ﬁre tegn i. N for nord, W for
 * vest, E for øst og S for sør.9 Når den har fulgt alle stegene i patruljen begynner den forfra igjen. En
 * patrulje på “NNEESSWW” skal gå rundt i ring, for eksempel.
 */


public class PatruljeMonster extends AbstractMonster {
	private String patrulje;
	private int i;
	public PatruljeMonster(Level level, SpriteLoader loader, int column, int row) {
		super(column, row, level, loader, new Random().nextInt(6));
		patrulje = "SSEEEEENNWW";
		i = 0;
	}

	@Override
	public void tick() {
		int nteTick = 44;
		if((tickcounter++ % nteTick) != 0){
			return;
		}
		/*
		 * TODO
		 * ISLETHal
		 */
		System.out.println("TICK " + tickcounter/nteTick);
		Direction[] directions = Direction.values();
		//for (int i = 0; i < patrulje.length(); i++) {
	
		//System.out.println(retning);
		System.out.println("I: " + i);
		if(i < patrulje.length()){
			char retning = patrulje.charAt(i);
			switch (retning) {
			case 'N':
				
				this.move(directions[0], level);
				i =  (i+1)%patrulje.length();	
				break;
			case 'W':
				this.move(directions[1], level);
				i =  (i+1)%patrulje.length();
				break;
			case 'S':
				this.move(directions[3], level);
				i =  (i+1)%patrulje.length();
				break;
			case 'E':
				this.move(directions[2], level);
				i =  (i+1)%patrulje.length();
				break;
			default:
				break;
			}

		}


	}

	@Override
	public byte getPriority() {
		// TODO Auto-generated method stub
		return 0;
	}

}
