package virtualm.logikk;

import java.util.ArrayList;

/**
 * Created by Jo Øivind Gjernes on 23.11.2015.
 * <p>
 * Statiske metoder for å tolke tekst
 */
public class Parser
{
	public Parser()
	{

	}

	public static int[] kodeTilRam(String kode) throws IllegalArgumentException
	{
		String[] strings = kode.split("\n");
		ArrayList<Integer> liste = new ArrayList<>();

		if (strings.length == 0) throw new IllegalArgumentException("Tom kode!");
		for (String linje : strings) {
			String[] linjeDelt = linje.split(" ");
			// Kan være kommentarer
			opcode op = opcode.getCode(linjeDelt[0]);
			if (op == opcode.INVALID) throw new IllegalArgumentException("UGYLDIG OPCODE: " + linjeDelt[0]);

			// Legge til kode
			Integer i = (op.getVal() << 16);

			if (linjeDelt.length > 1) {
				//if (linjeDelt[1].charAt(0) <= '0' || linjeDelt[1].charAt(0) >= '9' ) continue;
				try {
					int opcode = Integer.parseInt(linjeDelt[1]);
					i += opcode;
				} catch (Exception e) {
					throw new IllegalArgumentException("Ugyldig adresse!\n" + e.getMessage());
				}
			}
			liste.add(i);
		}
		return arrayListToInt(liste);
	}

	private static int[] arrayListToInt(ArrayList<Integer> liste)
	{
		int[] arr = new int[liste.size()];
		int i = 0;
		for (Integer nr : liste) {
			arr[i++] = nr;
		}
		return arr;
	}
}
