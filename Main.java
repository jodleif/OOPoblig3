package virtualm;

/**
 * Created by Jo Øivind Gjernes on 12.11.2015.
 *
 * Oblig 3 - Objektorientert Programmering
 */
public class Main
{
	public native void assemble(String filename);

	public static void main(String[] args)
	{
		System.out.println("Loading library...");
		System.loadLibrary("virtualm");
		System.out.println("Running assembler...");
		new Main().assemble("test.txt");
	}
}
