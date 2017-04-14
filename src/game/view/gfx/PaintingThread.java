package game.view.gfx;

/**
 * En enkel tråd som prøver å male 60 bilder i sekunder.
 * (Dvs. maks 60 FPS)
 * 
 * Siden to PaintingThread objekter er like hvis og bare hvis de er samme objektet overkjøres hverken equal eller hashCode
 *  
 * @author Haakon Løtveit (haakon.lotveit@student.uib.no)
 *
 */
public class PaintingThread extends Thread{
	Lerret l;
	
	/**
	 * Lager en ny malertråd, som maler et lerret.
	 * @param l lerretet som skal males.
	 * @throws NullPointerException dersom du gir en null til konstruktøren.
	 */
	public PaintingThread(Lerret l) throws NullPointerException {
		super();
		if(null == l){
			throw new NullPointerException("Lerret kan ikke være null");
		}
		
		this.l = l;
		this.start();
	}
	
	@Override
	public void run(){
		System.out.println("PaintingThread started");
		
		long timestamp = System.nanoTime();
		long paintFrequency = 1_000_000_000L / 60L; // 60 ganger per sekund
		
		while(!Thread.interrupted()){ /* Hvis du interrupter tråden, avslutter den. */
			long timeSinceLastPaint = System.nanoTime() - timestamp;

			if(timeSinceLastPaint >= paintFrequency){
				l.render();
				timestamp = System.nanoTime();
			}
		}
		
	}
}
