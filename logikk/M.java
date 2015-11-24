package virtualm.logikk;

import virtualm.fileio.FilIO;
import virtualm.logikk.Parser.Parser;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

/**
 * Created by Jo Øivind Gjernes on 23.11.2015.
 * <p>
 * Selve den virtuelle maskinen. Tolker 32bits heltall der første 16bits er satt av til instruksjonen og
 * siste 16bits er satt av til verdier eller adresser.
 * Foreløpig brukes kun bit 0-7 og bit 16-23
 * <p>
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
	public final static int UPPERMID8 = 0b00000000_11111111_00000000_00000000; // Brukes for å velge verdien i "instruksjons" området
	public final static int LOWER16 = 0b00000000_00000000_11111111_11111111; // Brukes for å velge de laveste 16 bits
	public final static int LOWER8 = 0b00000000_00000000_00000000_11111111; // Brukes for å velge de laveste 8 bits


	private int[] RAM; // Eller holder det med en byte-array?
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
		R = 0;
		PC = 0;
 /* Initialisering av instansvariabler */
	}

	public void loadProgram(String filsti)
	{
		String kodeFraFil = FilIO.lesFil(filsti); // Laster inn alle linjer fra fil.
		int[] program = Parser.assembler(kodeFraFil); // "oversetter" til bytecode
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
		byte r = (byte) R;
		try {
			switch (curr_op) {
				case IREAD:
					R = input.read();
					break;
				case IWRITE:
					output.print(String.valueOf((byte) (R & LOWER8)));
					break;
				case CREAD:
					R = input.readc();
					break;
				case CWRITE:
					output.print(String.valueOf((char) R));
					break;
				case LOAD:
					R = RAM[adr] & LOWER8;
					break;
				case STORE:
					RAM[adr] = R & LOWER8;
					break;
				case RSET:
					R = adr & LOWER8;
					break;
				case ADD:
					r += (byte) RAM[adr];
					R = r & LOWER8;
					break;
				case SUB:
					r -= (byte) RAM[adr];
					R = r & LOWER8;
					break;
				case MULT:
					r *= (byte) RAM[adr];
					R = r & LOWER8;
					break;
				case DIV:
					r /= (byte) RAM[adr];
					R = r & LOWER8;
					break;
				case JUMP:
					PC = adr;
					return false; // Unngå ++PC;
				case JNEG:
					if (r < 0) {
						PC = adr;
						return false; // Ikke inkrementere PC!!
					}
					break;
				case JZERO:
					if (r == 0) {
						PC = adr;
						return false; // Ikke inkrementere PC!!
					}
					break;
				case STOP:
					stop();
					return true;

				default:
					String errorMsg = "";
					if (curr_op.getVal() == 0)
						errorMsg += "[TIPS] Har du husket å avslutte programmet med STOP?\n";
					errorMsg += "[stepProgram] Ugyldig opcode!\nPC=" + PC + " OPCODE " + (curr_op);
					throw new IllegalArgumentException(errorMsg);
			}
		} catch (Exception e) {
			//String feilMelding = ArrayIndexOutOfBoundsException.getMessage();
			if (e instanceof IllegalArgumentException) throw e;
			String feilMelding = "UTENFOR ADRESSEOMRÅDET!\nPC=" + PC + " current op: " + curr_op + " adresse / variabel" + adr;
			throw new IllegalArgumentException(feilMelding);
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

	public boolean loadIntoRam(int[] program)
	{
		if (program.length > RAM_SIZE) return false;
		reset();
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
