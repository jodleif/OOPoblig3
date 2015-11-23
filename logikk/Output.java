package virtualm.logikk;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Consumer;

/**
 * Created by Jo Øivind Gjernes on 23.11.2015.
 */
public class Output
{
	private boolean stdout;
	private Deque<String> outputBuffer;
	boolean waitForNewline = false;

	private Consumer<String> cons;

	public Output(boolean stdout)
	{
		outputBuffer = new ArrayDeque<>();
		this.stdout = stdout;
	}

	public void print(String toPrint)
	{
		if (stdout) {
			if (isWaitForNewline()) {
				if (toPrint.equals("\n")) {
					printAll();
				} else {
					outputBuffer.add(toPrint);
				}
			} else {
				System.out.println(toPrint);
			}
		} else {
			if (toPrint != null) {
				if (cons != null) {
					cons.accept(toPrint);
				} else {
					outputBuffer.add(toPrint);
				}
			}
		}
	}

	private void printAll()
	{
		System.out.println(getBuffer());
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
		this.cons = e;
	}

	public boolean isStdout()
	{
		return stdout;
	}

	public void setStdout(boolean stdout)
	{
		this.stdout = stdout;
	}

	public Deque<String> getOutputBuffer()
	{
		return outputBuffer;
	}

	public void setOutputBuffer(ArrayDeque<String> outputBuffer)
	{
		this.outputBuffer = outputBuffer;
	}

	public boolean isWaitForNewline()
	{
		return waitForNewline;
	}

	public void setWaitForNewline(boolean waitForNewline)
	{
		this.waitForNewline = waitForNewline;
	}
}
