package virtualm.fileio;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Created by Jo Øivind Gjernes on 23.11.2015.
 * <p>
 * Klasse for å håndtere lagring og lasting av filer!
 */
public class FilIO
{

	/**
	 * Last inn en tekstfil som en lang String
	 *
	 * @param filnavn filnavnet
	 * @return en string som inneholder hele fila
	 */
	public static String lesFil(String filnavn)
	{
		StringBuilder res = new StringBuilder();
		try {
			Path path = Paths.get(filnavn);
			Files.lines(path).forEach(line -> {
				res.append(line);
				res.append("\n");
			});
		} catch (IOException e) {
			System.err.println("[lesFil] Feil under lesing av fil\n" + e.getMessage());
		}
		return res.toString();
	}

	/**
	 * Les "råe" bytes fra fil. Ikke brukt sålangt
	 *
	 * @param filnavn filnavnet å lese
	 * @return tabell med alle bytes i fila
	 */
	public static byte[] lesByteFil(String filnavn)
	{
		Path path = Paths.get(filnavn);
		try {
			return Files.readAllBytes(path);
		} catch (IOException e) {
			System.err.println("[lesByteFil] Feil under lesing av fil\n" + e.getMessage());
		}
		return null;
	}

	/**
	 * Lese en ascii fil som er kodet med tall/maskinkoder
	 *
	 * @param filnavn filen du vil åpne
	 * @return tabell med tall - må oversettes til "register språk"
	 */
	public static int[] lesTekstFilMedBytes(String filnavn)
	{
		String str = lesFil(filnavn);
		return tallStrengTilTabell(str);
	}

	/**
	 * Strengt tatt ikke "FilIO" spesifikk - men kjekt å trekke ut
	 * funskjonalitet så mye som mulig.
	 *
	 * @param tallStreng streng fylt med tallverdier for operasjoner
	 * @return tabell med alle tallverdiene "skilt" ut fra strengen
	 */
	public static int[] tallStrengTilTabell(String tallStreng)
	{
		int[] arr = new int[tallStreng.length()];
		Scanner scan = new Scanner(tallStreng);

		int teller = 0;
		while (scan.hasNext()) {
			if (scan.hasNextInt(10)) {
				int intInput = scan.nextInt(10);
				arr[teller++] = intInput;
			} else {
				scan.next();
			}
		}

		int[] result = new int[teller];
		System.arraycopy(arr, 0, result, 0, teller);
		return result;
	}
}
