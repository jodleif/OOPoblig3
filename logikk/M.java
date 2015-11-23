package virtualm.logikk;

/**
 * Created by Jo Øivind Gjernes on 23.11.2015.
 * <p>
 * Sup
 */
public class M

{
	private final static int RAM_SIZE = 256;
	private int[] RAM; // Eller holder det med en byte-array?
	private int R; // Register
	private int PC; // Programteller

	// Flere instansvariabler?
	public M()
	{
		RAM = new int[RAM_SIZE];
		R = 0;
		PC = 0;
 /* Initialisering av instansvariabler */
	}

	public void loadProgram(String fileName)
	{
 /*
 * Nullstill instansvariabler
 * Åpne fil
 * Les inn program fra fil, linje for linje
 * Lukk fil
 */
	}

	public void stepProgram()
	{
 /*
 * Les neste instruksjon fra RAM
 * Utfør instruksjonen (og oppdater programtelleren)
 */
	}

	public void executeProgram()
	{
 /*
 * Så lenge programmet ikke er avsluttet
 * Utfør neste instruksjon (stepProgram)
 * Vis avsluttende melding
 */
	}

	public static void main(String[] args)
	{
 /*
 * Hent filnavn fra kommandolinje (args[0])
 * Lag et M-objekt
 * Last program fra fil (loadProgram)
 * Utfør programmet (executeProgram)
 */
	}
 /* Eventuelle hjelpemetoder */

	public int[] getRAM()
	{
		return RAM;
	}

	public int getR()
	{
		return R;
	}

	public int getPC()
	{
		return PC;
	}
}
