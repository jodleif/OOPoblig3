package virtualm.debugger;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import virtualm.debugger.Debug.DebugPane;
import virtualm.logikk.M;
import virtualm.logikk.Parser;

/**
 * Created by Jo Øivind Gjernes on 23.11.2015.
 */
public class DebuggerGUI extends Application
{
	public static final double WIDTH = 1024d;
	public static final double HEIGHT = 768d;
	private static final String TITLE = "Virtual M debugger";

	private M virtualM;

	private Scene scene;
	private Group root = new Group();
	private BorderPane bp = new BorderPane();

	private TextArea codeView;
	private Text statusLinje;
	private HBox knappPane;
	private Button lastTilRam, run, step, exit;
	private DebugPane debugPane;


	@Override
	public void start(Stage primaryStage) throws Exception
	{
		primaryStage.setTitle(TITLE);
		scene = new Scene(root, WIDTH,HEIGHT);

		virtualM = new M();
		virtualM.setConsumer(s -> {
			statusLinje.setText(statusLinje.getText() + s);
		});

		setupLayout();
		setupButtons();
		setupDebugPane();
		root.getChildren().add(bp);

		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void StartApp(String[] args)
	{
		launch(args);
	}

	private void setupLayout()
	{
		codeView = new TextArea();
		codeView.setMinHeight(HEIGHT - 100);
		codeView.setMinWidth(WIDTH-200);
		bp.setCenter(codeView);
	}


	private void setupButtons()
	{
		lastTilRam = new Button("Last kode til RAM");
		lastTilRam.setOnAction(event -> {
			int[] programTilRam;
			try {
				programTilRam = Parser.kodeTilRam(codeView.getText());
				if (programTilRam != null) {
					virtualM.loadIntoRam(programTilRam);
					debugPane.oppdater();
				}
			} catch (Exception e) {
				printAlert("ERROR PARSING\n" + e.getMessage());
			}
		});
		run = new Button("Kjør");
		step = new Button("Steg");
		step.setOnAction(event -> {
			try {
				virtualM.stepProgram();
				debugPane.oppdater();
			} catch (Exception e) {
				printAlert("STEP ERROR\n" + e.getMessage());
			}
		});
		exit = new Button("Avslutt");
		statusLinje = new Text("ProgramOutput: ");
		knappPane = new HBox(lastTilRam, run, step, exit, statusLinje);
		bp.setBottom(knappPane);
	}

	private void setupDebugPane()
	{
		debugPane = new DebugPane(virtualM, HEIGHT - 100);
		bp.setRight(debugPane);
	}

	private void printAlert(String s)
	{

		Alert alert = new Alert(Alert.AlertType.ERROR, s);
		alert.show();
	}
}
