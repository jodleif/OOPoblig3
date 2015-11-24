package virtualm.logikk;

import java.util.function.IntSupplier;
import java.util.function.Supplier;

/**
 * Created by Jo Øivind Gjernes on 23.11.2015.
 * <p>
 * Klasse som håndterer input for M
 * <p>
 * Default konstruksjon henter den input fra console
 * hvis du tilegner den en "supplier" funksjon vil den hente verdier fra denne funksjonen!
 */
class Input
{
	private boolean stdin = true;

	private IntSupplier intSuppl;
	private Supplier<String> strSupplier;

	public Input()
	{
	}

	/**
	 * Les inn et heltall!
	 *
	 * @return heltallet
	 */
	public int read() throws IllegalArgumentException
	{
		if (stdin) {
			return readIntConsole();
		} else {
			if (intSuppl != null) {
				return intSuppl.getAsInt();
			} else {
				throw new IllegalArgumentException("Ingen supplier definert for read\n");
			}
		}
	}

	/**
	 * Les inn bokstav
	 *
	 * @return int verdien til bokstaven
	 */
	public int readc() throws IllegalArgumentException
	{
		if (stdin) {
			try {
				System.out.print("Skriv inn en bokstav + enter:");
				return System.console().readLine().charAt(0);
			} catch (Exception e) {
				System.err.println("ERROR: " + e.getMessage());
				return 0;
			}
		} else {
			if (strSupplier != null) {
				return strSupplier.get().charAt(0);
			} else {
				throw new IllegalArgumentException("Ingen supplier definert for readc\n");
			}
		}
	}

	/**
	 * Sett begge "supplier" funksjoner. Setter begge på en gang for det gir ingen mening å bruke både console og en supplier
	 * @param intSuppl funksjon som returnerer ett heltall
	 * @param strSupplier funksjon som returnerer en stregn, bruker kun første bokstav i strengen!
	 */
	public void setSuppliers(IntSupplier intSuppl, Supplier<String> strSupplier)
	{
		if (intSuppl == null || strSupplier == null)
			throw new IllegalArgumentException("Du må sette både intsuppl og strsupplier!");
		stdin = false;
		this.intSuppl = intSuppl;
		this.strSupplier = strSupplier;
	}

	private int readIntConsole()
	{
		try {
			System.out.print("Skriv inn et heltall!:");
			return Integer.parseInt(System.console().readLine());
		} catch (Exception e) {
			System.err.println("[readIntConsole] ERROR INPUT ONLY NUMBERS between -128,127.\nReturning 0");
			return 0;
		}
	}

}
