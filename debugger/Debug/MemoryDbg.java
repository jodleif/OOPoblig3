package virtualm.debugger.Debug;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import virtualm.logikk.M;
import virtualm.logikk.opcode;

/**
 * Created by Jo Øivind Gjernes on 24.11.2015.
 * <p>
 * Klasse for å "se" på minnet
 */
public class MemoryDbg
{
	private final int index;
	private final SimpleStringProperty indexString;
	private SimpleStringProperty opcodeParam;
	private ReadOnlyObjectWrapper<Integer> memParam;
	private int[] RAM;

	public MemoryDbg(int index, int[] RAM)
	{
		this.index = index;
		this.RAM = RAM;
		indexString = new SimpleStringProperty(index + ": ");
		opcodeParam = new SimpleStringProperty();
		memParam = new ReadOnlyObjectWrapper<>();
		update();
	}

	public final SimpleStringProperty getIndexString()
	{
		return indexString;
	}

	public String getOpcode()
	{
		int op = (RAM[index] & M.UPPERMID8) >> 16;
		opcode op1 = opcode.getCode(op);
		if (op1 == opcode.EMPTY && RAM[index] != 0) {
			opcode op2 = opcode.getCode(RAM[index] & M.FLAGS);
			if (op2 != opcode.INVALID)
				op1 = op2;
		}

		return op1.getString() + " (" + op + ")";
	}

	public SimpleStringProperty getOpcodeParam()
	{
		return opcodeParam;
	}

	public ReadOnlyObjectWrapper<Integer> getMemParam()
	{
		return memParam;
	}

	public Integer getMem()
	{
		return RAM[index] & M.LOWER16;
	}

	public void update()
	{
		opcodeParam.set(getOpcode());
		memParam.set(getMem());
	}
}
