package virtualm.debugger;


import javafx.event.ActionEvent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import virtualm.fileio.FilIO;

import java.io.File;
import java.io.IOException;

/**
 * Created by Jo Øivind Gjernes on 23.11.2015.
 * <p>
 * Meny - valgte å lage en egen "klasse" fordi:
 * 1. Prøve ut hvordan det funker å extende et vanlig menyobjekt
 * 2. Unngå at "DebuggerGUI" fila blir enda mer rotete
 */
class Meny extends MenuBar
{
	private Menu menuFil;
	private Menu menuInstillinger;
	private Menu menuHelp;


	private FileChooser filÅpner;

	private MenuItem lastFil;
	private MenuItem fjernTekst;
	private MenuItem avslutt;

	private MenuItem minneRep;
	private DebuggerGUI parent;

	public Meny(DebuggerGUI parent) throws IOException
	{
		this.parent = parent;
		menuFil = new Menu("Fil");
		menuInstillinger = new Menu("Innstillinger");
		menuHelp = new Menu("Hjelp");

		setupFileChooser();
		this.getMenus().addAll(menuFil, menuInstillinger, menuHelp);
		initMenyer();
	}

	private void initMenyer()
	{
		lastFil = new MenuItem("Last fil");
		fjernTekst = new MenuItem("Fjern tekst");
		avslutt = new MenuItem("Avslutt!");
		minneRep = new MenuItem("Velg minnereperesentasjon");

		avslutt.setOnAction(Meny::quit);
		lastFil.setOnAction(this::velgFil);
		fjernTekst.setOnAction(this::fjernTekst);
		minneRep.setOnAction(parent::velgMinneRep);

		menuFil.getItems().addAll(lastFil, fjernTekst, avslutt);
		menuInstillinger.getItems().add(minneRep);
	}

	private void setupFileChooser() throws IOException
	{
		filÅpner = new FileChooser();
		filÅpner.setTitle("Åpne en fil!");
		filÅpner.setInitialDirectory(new File("."));
		filÅpner.getExtensionFilters().addAll(
			new FileChooser.ExtensionFilter("Text fil", "*.txt"),
			new FileChooser.ExtensionFilter("M-fil", "*.m")
		);
	}

	private void velgFil(ActionEvent e)
	{
		try {
			String path = filÅpner.showOpenDialog(null).getAbsolutePath();
			String tekst = FilIO.lesFil(path);
			parent.settCodeViewTekst(tekst);
		} catch (Exception exep) {
			// NullPointerException hvis man trykker AVBRYT når man åpner fil!
			if (!(exep instanceof NullPointerException)) {
				DebuggerGUI.printAlert("Feil under lasting av fil!\n" + exep.getMessage());
			}
		}
	}

	private static void quit(ActionEvent actionEvent)
	{
		System.exit(0);
	}

	private void fjernTekst(ActionEvent e)
	{
		parent.settCodeViewTekst("");
	}
}

