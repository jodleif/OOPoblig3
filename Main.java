package virtualm;

import virtualm.debugger.DebuggerGUI;
import virtualm.logikk.M;

/**
 * Created by Jo Øivind Gjernes on 12.11.2015.
 *
 * Oblig 3 - Objektorientert Programmering
 *
 * BRUK:
 *
 * Kjøre program i console: java virtualm.Main kodefil.txt
 *
 * Starte GUI: java virtualm.Main
 *
 */
public class Main
{

	public static void main(String[] args)
	{
		/*
		 * Hent filnavn fra kommandolinje (args[0])
		 * Lag et M-objekt
		 * Last program fra fil (loadProgram)
		 * Utfør programmet (executeProgram)
		 */
		if (args.length == 0) {
			DebuggerGUI gui = new DebuggerGUI();
			gui.StartApp(args);
		} else {
			M vm = new M();
			vm.loadProgram(args[0]);
			vm.executeProgram();
		}
	}
}
