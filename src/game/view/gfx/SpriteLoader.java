package game.view.gfx;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Laster inn et spritesheet, som er et stort bilde med mange sprites i seg.
 * 
 * @author Haakon Løtveit
 *
 */
public class SpriteLoader {
	
	protected final BufferedImage SHEET;
	protected final int TILESIZE;
	
	/**
	 * Dette lager en ny SpriteLoader, slik at du kan bufre sprites effektivt.
	 * Effektivitet er ikke superviktig i denne obligen, og perf blir bra nok uansett, men vi følger best practices,
	 * slik at dere ser hvordan det gjøres. Teknikken er også brukt i internettverdenen for å laste småbilder raskere, for samme ytelsesårsakene som for spill.
	 * 
	 * Opprinnelig utgave av dette spillet (som var mer komplisert enn dette) skulle kjøres på en 16mhz 286 med 1mb RAM. Oddsene er at maskinene deres er raske nok til at dette går fint uansett.
	 * 
	 * @param spriteSheet en bitmap av et slag, helst .png, andre formater er ikke testet.
	 * @param tilesize hvor stor en sprite er. I vårt spill er tiles kvadratiske, så bare en verdi, ikke en for x og en for y.
	 * @throws IOException dersom filen ikke blir lastet inn korrekt. (For eksempel om filen ikke finnes, eller filformatet ikke støttes.)
	 * 
	 */
	public SpriteLoader(File spriteSheet, int tilesize) throws IOException{
		this.SHEET = ImageIO.read(spriteSheet);
		this.TILESIZE = tilesize;

		/*
		 * Sjekker at bildefilen din er av en størrelse som gir mening.
		 * Det kan tross alt være at du laster inn feil fil eller noe.
		 */
		if(SHEET.getHeight() % TILESIZE != 0){
			System.err.printf("[WARNING] Bildefilen er %d piksler for høy til å gå rent opp i %d. Sjekk at alt er rett",
							  SHEET.getHeight() % TILESIZE,
							  TILESIZE);
		}
		if(SHEET.getWidth() % TILESIZE != 0){
			System.err.printf("[WARNING] Bildefilen er %d piksler for bred til å gå rent opp i %d. Sjekk at alt er rett",
					  SHEET.getWidth() % TILESIZE,
					  TILESIZE);
		}
	}
	
	/**
	 * Henter ut et bilde fra sheeten basert på posisjonen i arket.
	 * Merk at ingen sjekk blir gjort for dere for å forsikre dere om at bildet blir lastet riktig, 
	 * at koordinatene er korrekte, eller lignende.
	 * 
	 * @param xPos hvilken kolonne det er snakk om, fra 0-n.
	 * @param yPos hvilken rad det er snakk om, fra 0-n.
	 * @return bildet, gitt at det finnes. 
	 */
	public BufferedImage getImage(int xPos, int yPos) {
		return SHEET.getSubimage(xPos * TILESIZE, yPos * TILESIZE, TILESIZE, TILESIZE);
	}
	
	/**
	 * Lar dere sakse ut et bilde fra arket fra hvilke som helst koordinater, med den størrelsen dere selv vil ha.
	 * For eksempel kan dere be om et bilde som er fra (0,0) til (64,64) ved å be om 
	 * getImage(0,0 new Dimension(64,64));
	 * Det er anbefalt at dere holder dere til den enklere getImage-metoden til normal bruk.
	 * Heller ikke her er det noen sjekk om at dere gir korrekte opplysninger.
	 * 
	 * @param xPos Hvilken horizontal piksel (den lengst til venstre er 0) du vil begynne på
	 * @param yPos Hvilken vertikal piksel (den lengst oppe er 0) du vil begynne på
	 * @param size Størrelsen på bildet.
	 * @return bildet kopiert ut fra arket, gitt at parametrene gir mening.
	 */
	public BufferedImage getImage(int xPos, int yPos, Dimension size){
		return SHEET.getSubimage(xPos, yPos, size.width, size.height);
	}
	
	/**
	 * Antall kolonner i arket.
	 * @return kolonner i arket. Merk kolonner, ikke piksler.
	 */
	public int numColumns(){
		return SHEET.getWidth() / TILESIZE;
	}
	
	/**
	 * Antall rader i arket.
	 * @return rader i arket. Merk rader, ikke piksler.
	 */
	public int numRows(){
		return SHEET.getHeight() / TILESIZE;
	}
	
	/**
	 * Returnerer størrelsen på en tile.
	 * @return størrelsen på en tile/sprite. Siden de er kvadratiske er det bare et tall, og ikke en for x og en for y.
	 */
	public int tilesize(){
		return TILESIZE;
	}
	
	/**
	 * Returnerer BufferedImage objektet som brukes som ark.
	 * @return sprite-arket som brukes. Merk at det *ikke* er en kopi.
	 */
	public BufferedImage getSheet(){
		return SHEET;
	}
}
