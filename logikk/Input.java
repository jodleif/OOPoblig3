package virtualm.logikk;

import java.util.function.IntSupplier;

/**
 * Created by Jo Øivind Gjernes on 23.11.2015.
 * <p>
 * Klasse som håndterer input for M
 * <p>
 * Default konstruksjon henter den input fra console
 * hvis du tilegner den en "supplier" funksjon vil den hente verdier fra denne funksjonen!
 */
public class Input
{
	private boolean stdin = true;

	private IntSupplier intSuppl;

	public Input()
	{
	}

	public int read()
	{
		if (stdin) {
			return readIntConsole();
		} else {
			if (intSuppl != null) {
				return intSuppl.getAsInt();
			} else {
				System.err.println("No supplier defined in input class!!! (provided input 0)");
				return 0;
			}
		}
	}

	public int readc()
	{
		if (stdin) {
			int i;
			try {
				System.out.println("Enter a value between 0-255");
				i = System.in.read();
			} catch (Exception e) {
				System.err.println("ERROR: " + e.getMessage());
				i = 0;
			}
			return i;
		} else {
			return read();
		}
	}

	public void setIntSuppl(IntSupplier intSuppl)
	{
		stdin = false;
		this.intSuppl = intSuppl;
	}

	private int readIntConsole()
	{
		int i = 0;
		try {
			int last = System.in.read() - '0';
			while (last != 10) {
				i += last;
				last = System.in.read();
				if (last == 10) continue; // 10 == '\n'
				last -= '0';
				if (i > 127 || i < -128) {
					return i - last;
				}
			}
		} catch (Exception e) {
			System.err.println("[readIntConsole] ERROR INPUT ONLY NUMBERS between -128,127.\nReturning 0");
			return 0;
		}
		return i;
	}

}
