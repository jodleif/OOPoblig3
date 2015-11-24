package virtualm.logikk;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Consumer;

/**
 * Created by Jo Øivind Gjernes on 23.11.2015.
 *
 * Output klasse som håndterer output fra M
 *
 * Default konstruksjon printer man til console
 * Hvis man tilegner en "consumer" funskjon, sendes output dit isteden.
 */
class Output
{
	private boolean stdout = true;
	private Deque<String> outputBuffer;

	private Consumer<String> cons;

	public Output()
	{
		outputBuffer = new ArrayDeque<>();
	}

	public void print(String s)
	{
		if (stdout) {
			System.out.print(s);
		} else {
			if (cons != null) {
				cons.accept(s);
			} else {
				outputBuffer.push(s);
			}
		}
	}

	public String getBuffer()
	{
		String toPrint = "";
		while (!outputBuffer.isEmpty()) {
			toPrint += outputBuffer.pop();
		}
		return toPrint;
	}

	public void setConsumer(Consumer<String> e)
	{
		stdout = false;
		this.cons = e;
	}

	public void setStdout(boolean stdout)
	{
		this.stdout = stdout;
	}

}
