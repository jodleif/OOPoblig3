package virtualm.debugger.Debug;

import javafx.scene.layout.Pane;
import virtualm.logikk.M;

/**
 * Created by Jo Øivind Gjernes on 23.11.2015.
 */
public class DebugPane extends Pane
{
	private M virtualm;

	public DebugPane(M virtualm)
	{
		this.virtualm = virtualm;
	}
}
