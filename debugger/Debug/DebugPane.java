package virtualm.debugger.Debug;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import virtualm.debugger.DebuggerGUI;
import virtualm.logikk.M;

/**
 * Created by Jo Øivind Gjernes on 23.11.2015.
 *
 * Klasse for debug-informasjon fra VirtualM
 */
public class DebugPane extends Pane
{
	private M virtualm;
	private VBox debugInfo;
	private static final String registerString = "Register: ";
	private static final String programTellerString = "Programteller: ";
	private TableView<MemoryDbg> tableView;
	private TableColumn<MemoryDbg, String> indexCol;
	private TableColumn<MemoryDbg, String> opcodeCol;
	private TableColumn<MemoryDbg, Integer> memCol;
	private Text register;
	private Text programTeller;
	private double height;
	private ObservableList<MemoryDbg> minneMapping;

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
		tableView = new TableView<>();
		tableView.setPrefHeight(DebuggerGUI.HEIGHT);
		tableView.setMaxHeight(height - programTeller.getY() * 2);
		tableView.setPrefWidth(200);
		tableView.setMaxWidth(200);
		indexCol = new TableColumn<>("adr");
		opcodeCol = new TableColumn<>("opcode");
		memCol = new TableColumn<>("minne");


		kopleOppMotMinne();
		byggOppColumns();
		debugInfo = new VBox(register, programTeller, tableView);
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
			minneMapping.forEach(e -> e.update());
		}
	}

	private void byggOppColumns()
	{
		indexCol.setCellValueFactory(p -> p.getValue().getIndexString());
		opcodeCol.setCellValueFactory(p -> p.getValue().getOpcodeParam());
		memCol.setCellValueFactory(p -> p.getValue().getMemParam());

		tableView.getColumns().add(indexCol);
		tableView.getColumns().add(opcodeCol);
		tableView.getColumns().add(memCol);

		tableView.getColumns().forEach(e -> e.setSortable(false));
		tableView.getColumns().forEach(e -> e.setEditable(false));
	}

	private void kopleOppMotMinne()
	{
		minneMapping = FXCollections.observableArrayList();
		int[] ramReferanse = virtualm.getRAM();
		for (int i = 0; i < M.RAM_SIZE; ++i) {
			minneMapping.add(new MemoryDbg(i, ramReferanse));
		}
		tableView.setItems(minneMapping);
	}


}
