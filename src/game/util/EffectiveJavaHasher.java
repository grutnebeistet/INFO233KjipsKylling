package game.util;

/**
 * Implementerer litt enkel hashing som beskrevet i Joshua Blochs Effective Java, 2. utgave.
 * (ISBN-13: 978-0321356680)
 * @author Haakon Løtveit
 *
 * Bruk disse til å hashe klassene dine med, så slipper du en del bry.
 */
public class EffectiveJavaHasher {
	/**
	 * Hasher en boolsk verdi
	 * @param b den boolske verdien
	 * @return en hash av boolske verdier.
	 */
	public static int hashBoolean(boolean b){
		return b? 1 : 0;
	}
	
	/**
	 * Hasher et heltall (også short, byte og char)
	 * @param i tallet som skal hashes.
	 * @return tallet representert som en int.
	 */
	public static int hashInteger(int i){
		return (int) i;
	}
	
	/**
	 * hasher et stort heltall.
	 * @param l en long som skal hashes.
	 * @return en hash av longen
	 */
	public static int hashLong(long l){
		/* slapp av, hva dette gjør kommer nok ikke på eksamen. ;) */
		return (int) (l ^ (l >>> 32));
	}
	
	/**
	 * hasher et flyttall (32bit float, IEEE754)
	 * @param f flyttallet som skal hashes.
	 * @return en hash av flyttallet.
	 */
	public static int hashFloat(float f){
		return Float.floatToIntBits(f);
	}
	
	/**
	 * hasher et flyttall (64bit float, IEEE754)
	 * @param d flyttallet som skal hashes.
	 * @return en hash av flyttallet.
	 */
	public static int hashDouble(double d) { /* Insert Beavis and Butthead-fnising når vi ser "double d" */
		return hashLong(Double.doubleToLongBits(d));
	}

}
