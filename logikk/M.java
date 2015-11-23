package virtualm.logikk;

import virtualm.fileio.FilIO;

import java.util.function.Consumer;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

/**
 * Created by Jo Øivind Gjernes on 23.11.2015.
 * <p>
 * Sup
 */
public class M

{
	private final static int RAM_SIZE = 256;
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
		opcode curr_op = opcode.getCode(RAM[PC]);
		int adr;
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
				++PC;
				adr = oversettAdresse(RAM[PC]);
				R = RAM[adr];
				break;
			case STORE:
				++PC;
				adr = oversettAdresse(RAM[PC]);
				RAM[adr] = R;
				break;
			case MOV:
				++PC;
				R = RAM[PC];
				break;
			case ADD:
				++PC;
				adr = oversettAdresse(RAM[PC]);
				R += RAM[adr];
				break;
			case SUB:
				++PC;
				adr = oversettAdresse(RAM[PC]);
				R -= RAM[adr];
				break;
			case MULT:
				++PC;
				adr = oversettAdresse(RAM[PC]);
				R *= RAM[adr];
				break;
			case DIV:
				++PC;
				adr = oversettAdresse(RAM[PC]);
				R /= RAM[adr];
				break;
			case JUMP:
				++PC;
				PC = RAM[PC];
				return false; // Unngå ++PC;
			case JNEG:
				++PC;
				if (R < 0) {
					PC = RAM[PC];
					return false; // Ikke inkrementere PC!!
				}
				break;
			case JZERO:
				++PC;
				if (PC == 0) {
					PC = RAM[PC];
					return false; // Ikke inkrementere PC!!
				}
				break;
			case STOP:
				stop();
				return true;
			case INVALID:
				throw new IllegalArgumentException("[stepProgram] Ugyldig opcode!\nPC=" + PC);
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
		RAM = new int[RAM_SIZE]; // RESET RAM
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
