package virtualm.debugger;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import virtualm.debugger.Debug.DebugPane;
import virtualm.logikk.M;

/**
 * Created by Jo Øivind Gjernes on 23.11.2015.
 */
public class DebuggerGUI extends Application
{
	public static final double WIDTH = 1024d;
	public static final double HEIGHT = 768d;
	private static final String TITLE = "Virtual M debugger";

	private Scene scene;
	private Group root = new Group();
	private BorderPane bp = new BorderPane();

	private TextArea codeView;
	private HBox knappPane;
	private Button run, step, exit;
	private DebugPane debugPane;

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		primaryStage.setTitle(TITLE);
		scene = new Scene(root, WIDTH,HEIGHT);

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
		run = new Button("Kjør");
		step = new Button("Steg");
		exit = new Button("Avslutt");
		knappPane = new HBox(run,step,exit);
		bp.setBottom(knappPane);
	}

	private void setupDebugPane()
	{
		debugPane = new DebugPane(new M(), HEIGHT - 100); // TODO: implementere virtualm
		bp.setRight(debugPane);
	}
}
