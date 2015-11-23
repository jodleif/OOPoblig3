package virtualm.logikk;

import java.util.function.Consumer;

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
	private Output output;

	// Flere instansvariabler?
	public M()
	{
		output = new Output(false);
		//output.setWaitForNewline(true);
		RAM = new int[RAM_SIZE];
		minneområde = 0;
		R = 0;
		PC = 0;
 /* Initialisering av instansvariabler */
	}

	public void loadProgram(String kode)
	{
		String kodeFraFil = kode; // Dummy lastet inn
		// last fil
		int[] program = Parser.kodeTilRam(kodeFraFil);
		minneområde = program.length; // Starter "RAM" fra minneområde;
 /*
 * Nullstill instansvariabler
 * Åpne fil
 * Les inn program fra fil, linje for linje
 * Lukk fil
 */
	}

	public boolean stepProgram() throws IllegalArgumentException
	{
 /*
 * Les neste instruksjon fra RAM
 * Utfør instruksjonen (og oppdater programtelleren)
 */
		opcode curr_op = opcode.getCode(RAM[PC]);
		int adr = 0;
		switch (curr_op) {

			case IREAD:
				break;
			case IWRITE:
				output.print(String.valueOf(R));
				break;
			case CREAD:
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
				R += RAM[PC];
				break;
			case SUB:
				R -= RAM[PC];
				break;
			case MULT:
				R *= RAM[PC];
				break;
			case DIV:
				R /= RAM[PC];
				break;
			case JUMP:
				++PC;
				PC = RAM[PC];
				return false; // Unngå ++PC;
			case JNEG:
				++PC;
				if (R < 0) {
					PC = RAM[PC];
					return false;
				}
				break;
			case JZERO:
				++PC;
				if (PC == 0) {
					PC = RAM[PC];
					return false;
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
		while (!stepProgram()) {
			stepProgram();
		}
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
		for (int i = 0; i < program.length; ++i) {
			RAM[i] = program[i];
		}
		return true;
	}

	private void stop()
	{
		output.print("Finished!\n");
	}

	public void setConsumer(Consumer<String> c)
	{
		output.setConsumer(c);
	}

	private void reset()
	{
		minneområde = 0;
		PC = 0;
		R = 0;
		RAM = new int[RAM_SIZE]; // RESET RAM
	}
}
