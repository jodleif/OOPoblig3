package virtualm;

import virtualm.debugger.DebuggerGUI;

/**
 * Created by Jo Øivind Gjernes on 12.11.2015.
 *
 * Oblig 3 - Objektorientert Programmering
 */
public class Main
{

	public static void main(String[] args)
	{
		DebuggerGUI gui = new DebuggerGUI();
		gui.StartApp(args);
	}
}
