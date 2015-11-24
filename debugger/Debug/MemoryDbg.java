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
	private SimpleStringProperty memParamFull; // Hele minnet på adressen
	private ReadOnlyObjectWrapper<Integer> memParam;
	private int[] RAM;
	private MinneRepresentasjon minneRep;

	public MemoryDbg(int index, int[] RAM)
	{
		this.index = index;
		this.RAM = RAM;
		indexString = new SimpleStringProperty(index + ": ");
		opcodeParam = new SimpleStringProperty();
		memParamFull = new SimpleStringProperty();
		memParam = new ReadOnlyObjectWrapper<>();
		minneRep = MinneRepresentasjon.BINÆRT;
		update();
	}

	public final SimpleStringProperty getIndexString()
	{
		return indexString;
	}

	private String getOpcode()
	{
		int op = (RAM[index] & M.UPPERMID8) >> 16;
		opcode op1 = opcode.getCode(op);

		return ((op1 == null) ? "" : op1.toString()) + " (" + op + ")";
	}

	public SimpleStringProperty getOpcodeParam()
	{
		return opcodeParam;
	}

	public ReadOnlyObjectWrapper<Integer> getMemParam()
	{
		return memParam;
	}

	public SimpleStringProperty getMemParamFull()
	{
		return memParamFull;
	}

	private Integer getMem()
	{
		return RAM[index] & M.LOWER16;
	}

	public void update()
	{
		opcodeParam.set(getOpcode());
		memParam.set(getMem());
		switch (minneRep) {

			case BINÆRT:
				memParamFull.set(Integer.toBinaryString(RAM[index]));
				while (memParamFull.getValue().length() <= 24) {
					memParamFull.setValue("0" + memParamFull.getValue());
				}
				break;
			case HEKSADESIMALT:
				memParamFull.set(Integer.toHexString(RAM[index]));
				break;
			case DESIMAL:
				memParamFull.set(Integer.toString(RAM[index]));
				break;
		}

	}

	public void setMinneRep(MinneRepresentasjon rep)
	{
		this.minneRep = rep;
	}


}
