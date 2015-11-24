package virtualm.logikk;

/**
 * Created by Jo Øivind Gjernes on 23.11.2015.
 *
 * Liste over gyldige "opcodes" for oppslag - begge veier
 */
public enum opcode
{
	IREAD (10, "IREAD"), // Ingen param
	IWRITE (11, "IWRITE"), // Ingen param
	CREAD (12, "CREAD"), // Ingen param
	CWRITE (13, "CWRITE"), // Ingen param
	LOAD (20, "LOAD"),
	STORE (21, "STORE"),
	MOV (22, "MOV"),
	ADD (30, "ADD"),
	SUB (31, "SUB"),
	MULT (32, "MULT"),
	DIV (33, "DIV"),
	JUMP (40, "JUMP"),
	JNEG (41, "JNEG"),
	JZERO (42, "JZERO"),
	STOP (50, "STOP"), // Ingen param
	INVALID(-1, "INVALID"),
	//VAR(M.VARFLAG, "VAR"),
	EMPTY(0, " ");


	private int val;
	private String valS;

	opcode(int val, String valS)
	{
		this.val = val;
		this.valS = valS;
	}

	public String getString(){
		return valS;
	}

	public int getVal(){
		return val;
	}

	public static opcode getCode(int code)
	{
		for(opcode op : opcode.values()){
			if(op.getVal()==code)
				return op;
		}
		return opcode.INVALID;
	}

	public static opcode getCode(String code)
	{
		for(opcode op : opcode.values()){
			if (op.getString().equals(code.toUpperCase()))
				return op;
		}
		if (code.equals("RSET")) return opcode.MOV; // Følge spesifikasjon
		return opcode.INVALID;
	}

	@Override
	public String toString()
	{
		return valS;
	}

}
