package virtualm.fileio;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Jo Øivind Gjernes on 23.11.2015.
 * <p>
 * Klasse for å håndtere lagring og lasting av filer!
 */
public class FilIO
{
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
}
