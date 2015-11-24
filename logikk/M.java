package virtualm.logikk;

import virtualm.fileio.FilIO;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

/**
 * Created by Jo Øivind Gjernes on 23.11.2015.
 * <p>
 * Sup
 *
 *
 * Minnet ser slik ut:
 * RAM[ADRESSE] = 00000000_AAAAAAAA_UUUUUUUU_BBBBBBBB
 * 0 - ledig
 * A - Operasjoner "opcoder"
 * U - Ubrukt (kan egentlig bruke variabler opp til 16-bit, men er etpar sjekker inne i programmet som stopper det fra å fungere)
 * B - Adresse, verdi eller tomt
 */
public class M
{
	public final static int RAM_SIZE = 256;

	public final static int FLAGS = 0b01111111_00000000_00000000_00000000; // Eventuelle FLAGG
	public final static int UPPERMID8 = 0b00000000_11111111_00000000_00000000; // Unngå negative tall
	public final static int LOWER16 = 0b00000000_00000000_11111111_11111111;
	//public final static int VARFLAG = 0b00000001_00000000_00000000_00000000; d/ Tester å markere ting som variabler i minnet


	private int[] RAM; // Eller holder det med en byte-array?
	private int minneområde;
	private int R; // Register
	private int PC; // Programteller
	private Output output; // Håndterer output
	private Input input; // Håndterer input

	// Flere instansvariabler?
	public M()
	{
		output = new Output();
		input = new Input();
		RAM = new int[RAM_SIZE];
		minneområde = 0;
		R = 0;
		PC = 0;
 /* Initialisering av instansvariabler */
	}

	public void loadProgram(String filsti)
	{
		String kodeFraFil = FilIO.lesFil(filsti); // Laster inn alle linjer fra fil.
		int[] program = Parser.kodeTilRam(kodeFraFil); // "oversetter" til bytecode
		loadIntoRam(program); // Laster inn programmet i starten av minnet
	}

	public boolean stepProgram() throws IllegalArgumentException
	{
 /*
 * Les neste instruksjon fra RAM
 * Utfør instruksjonen (og oppdater programtelleren)
 */
		opcode curr_op = opcode.getCode((RAM[PC] & UPPERMID8) >> 16);
		int adr = RAM[PC] & LOWER16;
		int oversatt = oversettAdresse(adr);
		switch (curr_op) {
			case IREAD:
				R = input.read();
				break;
			case IWRITE:
				output.print(String.valueOf(R) + "\n");
				break;
			case CREAD:
				R = input.readc();
				break;
			case CWRITE:
				output.print(String.valueOf((char) R));
				break;
			case LOAD:
				R = RAM[oversatt] & LOWER16;
				break;
			case STORE:
				RAM[oversatt] = R & LOWER16;
				break;
			case MOV:
				R = adr;
				break;
			case ADD:
				R += RAM[oversatt];
				break;
			case SUB:
				R -= RAM[oversatt];
				break;
			case MULT:
				R *= RAM[oversatt];
				break;
			case DIV:
				R /= RAM[oversatt];
				break;
			case JUMP:
				PC = adr;
				return false; // Unngå ++PC;
			case JNEG:
				if (R < 0) {
					PC = adr;
					return false; // Ikke inkrementere PC!!
				}
				break;
			case JZERO:
				if (R == 0) {
					PC = adr;
					return false; // Ikke inkrementere PC!!
				}
				break;
			case STOP:
				stop();
				return true;

			default:
				throw new IllegalArgumentException("[stepProgram] Ugyldig opcode!\nPC=" + PC + " OPCODE " + (RAM[PC] & UPPERMID8));
		}
		++PC; // Inkrementer programtelleren
		return false;
	}

	public void executeProgram()
	{
 /*
 * Så lenge programmet ikke er avsluttet
 * Utfør neste instruksjon (stepProgram)
 * Vis avsluttende melding
 */
		while (!stepProgram()) ; // Kjør til programmet er ferdig(returnerer true)
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

	public int oversettAdresse(int adresse) throws IllegalArgumentException
	{
		int adr = adresse + minneområde;
		if (adr > 255) throw new IllegalArgumentException("Utenfor adresseområdet!");
		return adr;
	}

	public boolean loadIntoRam(int[] program)
	{
		if (program.length > RAM_SIZE) return false;
		reset();
		minneområde = program.length;
		System.arraycopy(program, 0, RAM, 0, program.length);
		return true;
	}

	private void stop()
	{
		output.print("Ferdig! ");
		output.print("Ledig minne: " + (255 - getPC()) + "\n");
	}

	public void setConsumer(Consumer<String> c)
	{
		output.setConsumer(c);
	}

	public void setSupplier(IntSupplier s, Supplier<String> cSupplier)
	{
		input.setSuppliers(s, cSupplier);
	}
	public void reset()
	{
		minneområde = 0;
		PC = 0;
		R = 0;
		Arrays.fill(RAM, 0);
	}

	/**
	 * Funksjon som sjekker om minnet er tomt. Antar at minnet er tomt dersom første celle er lik 0
	 *
	 * @return tomt minne(true)?
	 */
	public boolean emptyRam()
	{
		return (RAM[0] == 0);
	}
}
