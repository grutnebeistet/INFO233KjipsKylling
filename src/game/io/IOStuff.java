package game.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Små hjelpemetoder for filer og slikt.
 * @author Haakon Løtveit (haakon.lotveit@student.uib.no)
 *
 */
public class IOStuff {

	/**
	 * Leser en fil til en streng.
	 * Bruker useDelimiter for å sette delimiter til EOF (End Of File), og leser deretter til neste delimiter.
	 * "\\Z" blir til "\Z", som betyr slutten på filen. (Z er siste bokstaven i det engelske alfabetet).
	 * \Z er et spesielt tegn, på samme måte som \t og \n er det i strenger.
	 * \Z er definert for regulære uttrykk som går litt utenfor pensum, men kogvitere i blant dere skal kjenne igjen konspeptet fra DASP fagene i alle fall.
	 * @param file filen som skal slurpes
	 * @return en streng som inneholder hele filen.
	 * @throws FileNotFoundException dersom filen ikke ble funnet.
	 */
	public static String slurpFile(File file) throws FileNotFoundException{
		try(Scanner slurper = new Scanner(file)){
			return slurper.useDelimiter("\\Z").next();
		}
	}
	
	
}
