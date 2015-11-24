package virtualm.debugger;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import virtualm.debugger.Debug.DebugPane;
import virtualm.logikk.M;
import virtualm.logikk.Parser.Parser;

/**
 * Created by Jo Øivind Gjernes on 23.11.2015.
 *
 * Hovedklasse for debugger GUIet.
 */
public class DebuggerGUI extends Application
{
	public static final double WIDTH = 1200d;
	public static final double HEIGHT = 1000d;
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

	private Meny menyBar;


	@Override
	public void start(Stage primaryStage) throws Exception
	{
		primaryStage.setTitle(TITLE);
		scene = new Scene(root, WIDTH,HEIGHT);

		virtualM = new M();
		virtualM.setConsumer(s -> statusLinje.setText(statusLinje.getText() + s));

		virtualM.setSupplier(DebuggerGUI::hentHeltall, DebuggerGUI::hentBokstav); // Metode referanse!!!
		menyBar = new Meny(this);

		setupLayout();
		setupButtons();
		setupDebugPane();
		setupKnappActions();

		root.getChildren().add(bp);
		bp.setTop(menyBar);

		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	public void StartApp(String[] args)
	{
		launch(args);
	}

	private void setupLayout()
	{
		codeView = new TextArea();
		codeView.setPrefHeight(HEIGHT - 200);
		codeView.setPrefWidth(WIDTH - 500);
		bp.setCenter(codeView);
	}


	private void setupButtons()
	{
		lastTilRam = new Button("Last kode til RAM");
		run = new Button("Kjør");
		step = new Button("Steg");
		exit = new Button("Reset");
		statusLinje = new Text("ProgramOutput: ");
		knappPane = new HBox(lastTilRam, run, step, exit, statusLinje);
		bp.setBottom(knappPane);
	}

	private void setupKnappActions()
	{
		lastTilRam.setOnAction((event) -> lastTekstTilRam());

		run.setOnAction(event -> {
			if (virtualM.emptyRam()) {
				printAlert("Inget program lastet!");
				return;
			}
			try {
				virtualM.executeProgram();
				debugPane.oppdater();
			} catch (Exception e) {
				printAlert("Feil\n" + e.getMessage());
			}
		});
		step.setOnAction(event -> {
			if (virtualM.emptyRam()) {
				printAlert("Inget program lastet!");
				return;
			}
			try {
				virtualM.stepProgram();
				debugPane.oppdater();
			} catch (Exception e) {
				printAlert("STEP ERROR\n" + e.getMessage());
			}
		});

		exit.setOnAction(event -> {
			virtualM.reset();
			debugPane.oppdater();
			statusLinje.setText("Minnet tømt! VM resatt!");
		});
	}

	private void setupDebugPane()
	{
		debugPane = new DebugPane(virtualM, HEIGHT - 200);
		bp.setRight(debugPane);
	}

	private void lastTekstTilRam()
	{
		int[] programTilRam;
		try {
			String s = codeView.getText();
			if (s == null || s.length() == 0) {
				printAlert("Ingen kode skrevet inn!");
				return;
			}
			programTilRam = Parser.kodeTilRam(s);
			if (programTilRam != null) {
				virtualM.loadIntoRam(programTilRam);
				statusLinje.setText("Program lastet ok!");
				debugPane.oppdater();
			}
		} catch (Exception e) {
			printAlert("ERROR PARSING\n" + e.getMessage());
		}
	}

	void settCodeViewTekst(String tekst)
	{
		this.codeView.setText(tekst);
	}

	private static String hentBokstav()
	{
		while (true) {
			Dialog<String> d = new TextInputDialog();
			d.setContentText("Skriv inn et tegn(Hvis du skriver flere tegn sendes kun det første");
			String s = d.showAndWait().get();
			if (s.length() >= 0) return s;
		}
	}

	private static int hentHeltall()
	{
		while (true) {
			Dialog<String> d = new TextInputDialog();
			d.setContentText("Skriv inn et tall mellom 0-255");
			String s = d.showAndWait().get();
			try {
				int i = Integer.parseInt(s);
				if (i < 255) return i;
			} catch (Exception e) {
				printAlert(e.getMessage());
			}
		}
	}

	public static void printAlert(String s)
	{
		Alert alert = new Alert(Alert.AlertType.ERROR, s);
		alert.show();
	}

	public void velgMinneRep()
	{
		debugPane.velgMinneRepDialog();
	}
}
