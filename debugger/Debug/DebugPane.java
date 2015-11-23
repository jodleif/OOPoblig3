package virtualm.debugger.Debug;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import virtualm.debugger.DebuggerGUI;
import virtualm.logikk.M;

/**
 * Created by Jo Øivind Gjernes on 23.11.2015.
 */
public class DebugPane extends Pane
{
	private M virtualm;
	private VBox debugInfo;
	private static final String registerString = "Register: ";
	private static final String programTellerString = "Programteller: ";
	private ListView<String> listView;
	private Text register;
	private Text programTeller;
	private double height;

	public DebugPane(M virtualm, double height)
	{
		this.virtualm = virtualm;
		this.height = height;
		initPane();
		oppdater();
	}

	private void initPane()
	{
		register = new Text();
		programTeller = new Text();
		listView = new ListView<>();
		listView.setPrefHeight(DebuggerGUI.HEIGHT);
		listView.setMaxHeight(height - programTeller.getY() * 2);
		listView.setPrefWidth(80);
		listView.setMaxWidth(100);
		debugInfo = new VBox(register, programTeller, listView);
		this.getChildren().add(debugInfo);

	}

	public void oppdater()
	{
		if (virtualm == null) {
			register.setText(registerString);
			programTeller.setText(programTellerString);
		} else {
			register.setText(registerString + String.valueOf(virtualm.getR()));
			programTeller.setText(programTellerString + String.valueOf(virtualm.getPC()));
			listView.setItems(hentRAM());
		}
	}

	private ObservableList<String> hentRAM()
	{
		ObservableList<String> ramView = FXCollections.observableArrayList();
		int[] ram = virtualm.getRAM();
		if (ram != null) {
			for (int i = 0; i < ram.length; ++i) {
				String s = i + ": " + ram[i];
				ramView.add(s);
			}
		}
		return ramView;
	}

}
