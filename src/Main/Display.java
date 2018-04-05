package Main;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import samThreshold.Flag;
import samThreshold.FlagGeneration;
import Read_Data.CSV2Array;
import Read_Data.ExceltoCSV;
import Read_Data.PhaseNamesFromCSV;
import Read_Data.DataPoint;
import decayAnalysis.Analyser;
import decayAnalysis.Coordinate;
import decayAnalysis.Quintuple;
import decayAnalysis.RegressionAndParameters;

public class Display extends Application {

	// !!!PATS ADDITION!!!//
	private ExceltoCSV convertor = new ExceltoCSV();
	private CSV2Array populator = new CSV2Array();
	private GraphGenerator graphGenerator;
	private ArrayList<DataPoint> data;
	private ArrayList<Flag> flagList;
	private File excelFile, dirtyCSVFile;

	// **The textfield which displays file path**//
	private TextField textField = new TextField();

	private String selection;
	private Scene scene;

	@Override
	public void start(Stage stage) throws Exception {

		stage.setTitle("IXOM CIP Diagnostic Tool");
		stage.setResizable(false);

		// Wrapping everything in this StackPane called 'root' was necessary to
		// centre the grid later
		StackPane root = new StackPane();
		root.setId("pane");
		
		if(System.getProperty("os.name").equals("Linux")){
			scene = new Scene(root, 520, 520);
	}
	else{
    scene = new Scene(root, 430, 430);
	}
		
		scene.getStylesheets().add("cobra.css");

		// Sam's Addition - Instantiation of drop-down menu for selecting CIP
		// process
		ComboBox<String> combobox = setUpComboBox();
		combobox.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				selection = combobox.getValue();

			}
		});

		// This is the run button and contains the click event for when you
		// click it
		Button btnRun = new Button();
		btnRun.setId("btnRun");
		btnRun.setText("Run Analysis Tool");
		btnRun.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				String filename = textField.getText();
				

				// this is for if someone clicks run and they havent selected a
				// file
				if (filename.equals("")) {
					Alert alertNoFlag = new Alert(AlertType.INFORMATION);
					alertNoFlag.setTitle("Alert!");
					alertNoFlag.setHeaderText("No file detected");
					alertNoFlag.setContentText("Select a file before clicking run");

					alertNoFlag.showAndWait();
				}
				
				//this is for if someone clicks run and they havent selected a
				//file
				else if(combobox.getValue().equals("Select Process")){
					Alert alertNoFlag = new Alert(AlertType.INFORMATION);
					alertNoFlag.setTitle("Alert!");
					alertNoFlag.setHeaderText("No process detected");
					alertNoFlag.setContentText("Select a process before clicking run");

					alertNoFlag.showAndWait();
				}
				

				else {
					// !!!! PATS ADDITION !!!!////
					// Calls csv convertor//
					excelFile = new File(filename);
					dirtyCSVFile = new File(excelFile.getParent(), "output.csv");
					try {
						convertor.xls(excelFile, dirtyCSVFile, 0);
					} catch (EncryptedDocumentException e1) {

						e1.printStackTrace();
					} catch (InvalidFormatException e1) {

						e1.printStackTrace();
					}

					try {
						// henry's addition
						File RawCSV = new File(excelFile.getParent(), "RawOutput.csv");
						convertor.xls(excelFile, RawCSV, ExceltoCSV.getFinalSheetNumber(excelFile) - 1);
						ArrayList<String> phaseNames = PhaseNamesFromCSV.returnPhaseNames(RawCSV);

						// !!!PATS ADDITION!!!//
						data = populator.populateData(dirtyCSVFile, data);

						// set up Map
						DataPoint.setMap(data, phaseNames);

						// !!!SAM'S ADDITION!!!//
						/*
						 * instantiates Flag Array, Flag Generation object - it
						 * is passed data array and contains metricTaker object
						 * for performing flag tests thrown flags are added to
						 * array and then passed to the application
						 */
						flagList = new ArrayList<Flag>();
						FlagGeneration f = new FlagGeneration(data, selection, phaseNames);

						// Print results of curve fitting, remove later
						

						f.thresholds(flagList);
						analyseData(data);

						String shortname = (excelFile.getName().substring(0, excelFile.getName().lastIndexOf('.')) + " - " + selection);
						graphGenerator = new GraphGenerator();
						graphGenerator.generateGraphs(data, shortname, flagList, phaseNames, stage, excelFile);

					} catch (Exception e) {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("ERROR");
						alert.setHeaderText("(There was an error in the retrieval process)");
						alert.setContentText(
								"Please restart the application and try again. Make sure you have selected a valid data file to analyse");
						alert.showAndWait();
						e.printStackTrace();
					}

				}
			}
		});

		// The following is a button and when clicked it launches a file chooser
		Button btnFileChooser = new Button();
		btnFileChooser.setText("Choose file");
		btnFileChooser.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Open Resource File");

				// this String contains the file path of the file that you
				// choose with the fileChooser
				String name = fileChooser.showOpenDialog(stage).toString();

				textField.setText(name);

			}

		});

		// This is the grid, which is contained within a

		GridPane grid = new GridPane();
		grid.setId("grid");
		// calls method grid settings
		setUpGrid(grid);

		grid.add(new Label("Select a data file to analyse: "), 0, 0);
		grid.add(textField, 0, 1);

		grid.add(btnFileChooser, 1, 1);

		grid.add(combobox, 0, 2);
		grid.add(btnRun, 0, 4);
		grid.setVgap(20);
		root.getChildren().add(grid);
		stage.setScene(scene);
		setUserAgentStylesheet(STYLESHEET_CASPIAN);
		stage.show();
	}

	public void setUpGrid(GridPane grid) {
		grid.setAlignment(Pos.CENTER);
		grid.setVgap(10);
		grid.setHgap(10);
		grid.setPadding(new Insets(5, 5, 5, 5));
	}

	// ewf
	// !!!PATS ADDITION!!!//
	public String printAll(ArrayList<DataPoint> data) {

		String output;
		output = "|   Date   |   Time   | turb | cond | soil | temp | zone | \n";

		for (DataPoint d : data) {
			output = output + d.print();
		}

		return output;

	}

	// SAMS CODE // ANDREW SAYS: I had to abandon this particular method when
	// using the gridpane, but I'm keeping it here in case we need it again
	public String printFlags(ArrayList<Flag> flagList) {

		String output;

		output = "| Start Time | End Time | Phase | Message | Target | Actual  | \n";

		for (Flag flag : flagList) {

			output = output + flag.print();
		}

		return output;
	}

	public static void main(String args[]) {
		launch(args);
	}

	// method for testing Curve Fitting etc, prints out results to console
	public void analyseData(ArrayList<DataPoint> data) {
		Analyser a = new Analyser();

		ArrayList<ArrayList<DataPoint>> LOC = a.splitByZones(data);

		
		for (ArrayList<DataPoint> dp : LOC) {
			DataPoint point = dp.get(0);
			String zoneString = DataPoint.getMap().get(point).toString();

			if (zoneString.contains("RINSE") && !zoneString.contains("PRERINSE")) {
				ArrayList<Coordinate> coords = a.findSteepestCond(dp);

				RegressionAndParameters reg = new RegressionAndParameters(coords);
				Quintuple quintuple = reg.leastSquaresFitting(coords);
				String Message = "Conductivity: y = " + round3dp(quintuple.getA()) + "exp("
						+ round3dp(quintuple.getB()) + "x), R^2: " + round3dp(quintuple.getrSquared());
				Flag analyticflag = new Flag(coords.get(0).getTime(), coords.get(coords.size() - 1).getTime(),
						point.getZone(), zoneString, Message, "NA", 0);
				analyticflag.setType("Conductivity");
				flagList.add(analyticflag);
				
			}
		}
	}

	public ComboBox<String> setUpComboBox() {
		ComboBox<String> combobox = new ComboBox<String>();
		combobox.setId("combobox");
		combobox.getItems().addAll("Concentrate Lines", "Drier & Fluid beds", "Evap Preheat (MPC)",
				"Evap Preheat (WMP/SMP)", "Evap (MPC)", "Evap (WMP/SMP)", "Ingredients Oils", "Vitamin/Minerals System"

		);
		combobox.setValue("Select Process");
		return combobox;
	}

	public void setGridLabelConstant(GridPane grid) {
		grid.add(new Label("| Start Time |"), 0, 0);
		grid.add(new Label("| End Time |"), 1, 0);
		grid.add(new Label("| Phase |"), 2, 0);
		grid.add(new Label("| Message |"), 3, 0);
		grid.add(new Label("| Target |"), 4, 0);
		grid.add(new Label("| Actual |"), 5, 0);
	}

	public void setGridLabelVary(GridPane grid, Flag flag, int counter) {
		grid.add(new Label(flag.getStartTime()), 0, counter);
		grid.add(new Label(flag.getEndTime()), 1, counter);
		grid.add(new Label(flag.getPhase()), 2, counter);
		grid.add(new Label(flag.getMessage()), 3, counter);
		grid.add(new Label(String.valueOf(flag.getTarget())), 4, counter);
		grid.add(new Label(String.valueOf(flag.getActual())), 5, counter);
	}
	
	public double round3dp(double unrounded){
		unrounded = Math.round(unrounded*1000)*0.001;
		
		return unrounded;
	}

}
