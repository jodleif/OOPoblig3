package virtualm.logikk.Parser;

import virtualm.logikk.M;
import virtualm.logikk.opcode;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by Jo Øivind Gjernes on 23.11.2015.
 * <p>
 * Statiske metoder for å tolke "assembler" koden til M
 * Dette har vokst til å bli en assembler som er noenlunde kompatibel med M sin spesifikasjon!
 * <p>
 * Har i siste liten lagt til en to-pass "kompilering" av kildekoden
 * slik at jeg har mulighet for en form for variabler som minneadresser
 * ++++
 * Minneadressering starter slik:
 * Bunn av minnet
 * | PRORGRAMMET LIGGER HER | <- Like etter programmet kommer "adresse 0"
 * ----
 * Intill videre betyr dette at man har mistet muligheten til å referere direkte til programmet i seg selv
 */
public class Parser
{

	public static LinkedHashMap<String, Integer> variabler;
	public static LinkedHashMap<String, Integer> labels;
	public static String[] kodefil;

	public Parser()
	{

	}

	/**
	 * Første pass av "kompilering"
	 * Modifiserer koden kun på en måte: "Labels" fjernes.
	 *
	 * @return antall linjer
	 */
	private static int førstePass()
	{
		int effektiveLinjer = 0;
		for (int j = 0; j < kodefil.length; ++j) {
			String behandlet = førstePassLinje(kodefil[j], effektiveLinjer);
			if (behandlet != null) {
				++effektiveLinjer;
				kodefil[j] = (behandlet);
			}
		}

		return effektiveLinjer;
	}

	private static String førstePassLinje(String linje, int linjeNummer)
	{
		String[] token = linje.split(" ");

		int tokenNummer = 0;
		boolean fjernFørsteElem = false;

		if (token.length == 0 || token[0].length() == 0) return null;

		if (token[0].charAt(0) == '.') {// Løkkepunkt!
			tokenNummer++;
			labels.put(token[0], linjeNummer); // Linjenummer er ikke inkrementert for denne linjen, men maskinen starter på 0
			fjernFørsteElem = true;
		}

		if (token.length < tokenNummer + 1)
			throw new IllegalArgumentException("Ingen instruksjon etter .label? Linje: " + (linjeNummer + 1));
		opcode op = opcode.getCode(token[tokenNummer]);

		if (op == opcode.INVALID) return null;

		if (token.length > tokenNummer + 1) {
			tokenNummer++;
			if ((token[tokenNummer].charAt(0) != ';') && (op != opcode.RSET)) {
				if (!variabler.containsKey(token[1])) {
					variabler.put(token[1], variabler.size()); // Antall variabler == variabler.size()
				}
			}
		}
		if (fjernFørsteElem) {
			return fjernFørsteElement(token);
		}
		return linje;
	}

	private static String fjernFørsteElement(String[] tokens)
	{
		String result = "";
		for (int i = 1; i < tokens.length; ++i) {
			if (result.length() > 0) result += " ";
			result += tokens[i];
		}
		return result;
	}

	private static Integer parseLinje(String linje, int programLengde, int linjeNummer)
	{

		String[] linjeDelt = linje.split(" ");
		// Kan være kommentarer
		if (linjeDelt.length == 0) return null;

		opcode op = opcode.getCode(linjeDelt[0]);

		if (op == opcode.INVALID)
			throw new IllegalArgumentException("Linje: " + linjeNummer + "\nUGYLDIG OPCODE: " + linjeDelt[0]);

		// Legge til kode
		Integer i = (op.getVal() << 16);
		if (linjeDelt.length > 1) {
			if (linjeDelt[1].charAt(0) == ';') return i; // Hvis kommentar
			i += behandleAdresse(op, linjeDelt[1], programLengde, linjeNummer);
		}
		return i;
	}

	/**
	 * Behandler adresse avhengig av om det er minneadresser eller verdier!
	 *
	 * @param op            operasjon på "adressen"(eller konstant verdi)
	 * @param tokenEtter    "adressen" eller variablen som skal behandles
	 * @param programLengde total lengde på programmet (slik det blir i minnet på den virtuelle maskinen)
	 * @param linjeNummer   hvilken linje som tolkes
	 * @return
	 * @throws IllegalArgumentException
	 */
	private static Integer behandleAdresse(opcode op, String tokenEtter, int programLengde, int linjeNummer) throws IllegalArgumentException
	{
		Integer i = 0;
		// Ikke etterfulgt av noe
		if (op.getVal() >= 10 && op.getVal() <= 13) return i;
		if (op.getVal() == 50) return i;
		if (op.getVal() == opcode.RSET.getVal()) return konstant(tokenEtter, linjeNummer);
		if (op.getVal() >= 40 && op.getVal() <= 42) return label(tokenEtter);
		return adresseEllerVariabel(tokenEtter, programLengde);
	}

	private static Integer adresseEllerVariabel(String adresse, int programLengde) throws IllegalArgumentException
	{
		if (!variabler.containsKey(adresse))
			throw new IllegalArgumentException("Fant ikke variabel! Parsing feil!!!\nVariabel: " + adresse);
		int adr = variabler.get(adresse) + programLengde;
		return adr;
	}

	private static Integer label(String adresse)
	{
		if (!labels.containsKey(adresse)) {
			throw new IllegalArgumentException("Fant ikke label!\nSjekk: " + adresse);
		}
		int adr = labels.get(adresse);
		return adr;
	}


	private static Integer konstant(String konstant, int linjeNummer)
	{
		try {
			int opc = Integer.parseInt(konstant);
			byte opcode = (byte) opc;
			if (opc != opcode) System.err.println("WARNING: Variable overflow");
			return opcode & M.LOWER8;
		} catch (Exception e) {
			throw new IllegalArgumentException("Linje: " + linjeNummer + "\nUgyldig adresse!\n" + e.getMessage());
		}
	}

	public static int[] kodeTilRam(String kode) throws IllegalArgumentException
	{
		kodefil = kode.split("\n");

		ArrayList<Integer> liste = new ArrayList<>();
		variabler = new LinkedHashMap<>();
		labels = new LinkedHashMap<>();

		int størrelseIMinnet = førstePass();

		if (kodefil.length == 0) throw new IllegalArgumentException("Tom kode!");
		int linjeNummer = 1;
		for (String linje : kodefil) {
			Integer i = 0;
			if (linje.length() != 0) {
				i = parseLinje(linje, størrelseIMinnet, linjeNummer);
			}
			if (i != null && i != 0) {
				liste.add(i);
				++linjeNummer;
			}
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
