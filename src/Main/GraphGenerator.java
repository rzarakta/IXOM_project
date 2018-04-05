package Main;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import Read_Data.DataPoint;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import samThreshold.Flag;
import timeconverter.TimeConverter;

public class GraphGenerator {

	private TimeConverter timeConverter = new TimeConverter();
	private ExportData exportData = new ExportData();
	private ArrayList<AreaChart<Number, Number>> graphList = new ArrayList<AreaChart<Number, Number>>();

	public void generateGraphs(ArrayList<DataPoint> data, String name, ArrayList<Flag> flagList,
			ArrayList<String> phaseNames, Stage stage, File excelFile) {

		Stage window = new Stage();
		Group root = new Group();

		final TabPane tabPane = new TabPane();

		tabPane.setPrefSize(1000, 600);
		tabPane.setSide(Side.TOP);
		tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

		BorderPane borderPane = new BorderPane();

		final Tab TurbTab = new Tab();
		setTurbTab(TurbTab, name, data, flagList, phaseNames);

		final Tab CondTab = new Tab();
		setCondTab(CondTab, name, data, flagList, phaseNames);

		final Tab TempTab = new Tab();
		setTempTab(TempTab, name, data, flagList, phaseNames);

		// andrews addition
		final Tab FlagTab = new Tab();
		setFlagTab(FlagTab, name, flagList, stage, excelFile);

		tabPane.getTabs().addAll(FlagTab, CondTab, TempTab, TurbTab);
		borderPane.setCenter(tabPane);
		root.getChildren().add(borderPane);

		Scene scene = new Scene(root, Color.WHITE);
		window.setTitle("Line Chart Sample");
		scene.getStylesheets().add("viper.css");

		window.setScene(scene);
		window.show();

	}

	// andrews addition
	@SuppressWarnings("unchecked")
	private void setFlagTab(Tab FlagTab, String name, ArrayList<Flag> flagList, Stage stage, File excelFile) {

		TableView table = new TableView();
		table.setId("flagTable");
		ObservableList<Flag> data = FXCollections.observableArrayList(flagList);

		Label label = new Label("Flags identified for: " + name);
		label.setFont(new Font("Arial", 20));
		
		label.setId("titledickwad");

		TableColumn startCol = new TableColumn("Start Time");
		startCol.setCellValueFactory(new PropertyValueFactory<Flag, String>("startTime"));

		startCol.setSortable(true);
		startCol.prefWidthProperty().bind(table.widthProperty().multiply(0.1));

		TableColumn endCol = new TableColumn("End Time");
		endCol.setCellValueFactory(new PropertyValueFactory<Flag, String>("endTime"));

		endCol.setSortable(true);
		endCol.prefWidthProperty().bind(table.widthProperty().multiply(0.1));

		TableColumn phaseCol = new TableColumn("Phase");
		phaseCol.setCellValueFactory(new PropertyValueFactory<Flag, String>("phase"));

		phaseCol.setSortable(true);
		phaseCol.prefWidthProperty().bind(table.widthProperty().multiply(0.2));

		TableColumn messageCol = new TableColumn("Message");
		messageCol.setCellValueFactory(new PropertyValueFactory<Flag, String>("message"));

		messageCol.setSortable(true);
		// this one is multiplied by a weird number because the total has to be
		// slightly less than 100 otherwise a scrollbar appears
		messageCol.prefWidthProperty().bind(table.widthProperty().multiply(0.3379));

		TableColumn targetCol = new TableColumn("Target");
		targetCol.setCellValueFactory(new PropertyValueFactory<Flag, String>("target"));

		
		targetCol.setSortable(true);
		targetCol.prefWidthProperty().bind(table.widthProperty().multiply(0.13));

		TableColumn actualCol = new TableColumn("Actual");
		actualCol.setCellValueFactory(new PropertyValueFactory<Flag, String>("actual"));

		actualCol.setSortable(true);
		actualCol.prefWidthProperty().bind(table.widthProperty().multiply(0.13));

		table.setItems(data);
		table.getColumns().addAll(startCol, endCol, phaseCol, messageCol, targetCol, actualCol);

		VBox vbox = new VBox();
		vbox.setSpacing(30);
		vbox.setPadding(new Insets(20, 20, 20, 20));
		vbox.setId("cheesefuck");
		vbox.setAlignment(Pos.CENTER_RIGHT);
		Button print = new Button();
		print.setId("printbutton");
		print.setText("Export to File");
		
		print.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

				exportData.exportData(getGraphList(), excelFile.getName(), flagList,
						excelFile, stage);

			}

		});
		
		vbox.getChildren().addAll(label, table, print);
		FlagTab.setText("Flags");
		FlagTab.setContent(vbox);
	}

	private void setCondTab(Tab CondTab, String name, ArrayList<DataPoint> data, ArrayList<Flag> flagList,
			ArrayList<String> phaseNames) {

		final NumberAxis xAxis = new NumberAxis();
		final NumberAxis yAxis = new NumberAxis();
		xAxis.setLabel("Time (Seconds)");
		xAxis.autosize();
		yAxis.setLabel("Conductivity (S/m)");
		// creating the chart

		// defining a series

		int zonecount = 1;
		double offset = timeConverter.HMSToDec(data.get(0).getTime()) * 86400;

		ArrayList<Series<Number, Number>> serieslist = new ArrayList<Series<Number, Number>>();

		// Modular series generator
		// Creates series based on amount of phasenames parsed into method
		for (String phasename : phaseNames) {

			XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
			series.setName(phasename);
			serieslist.add(series);

			// Populates each series with data from appropriate zone

		}
		for (DataPoint d : data) {
			if (d.getZone() == zonecount) {

				serieslist.get(zonecount - 1).getData().add(new XYChart.Data<Number, Number>(
						(timeConverter.HMSToDec(d.getTime()) * 86400) - offset, d.getConductivity()));

			}
			if (d.getZone() != zonecount) {
				zonecount++;
			}
		}

		final AreaChart<Number, Number> lineChart = new AreaChart<Number, Number>(xAxis, yAxis) {

			private List<Shape> shapes = new ArrayList<>();

			@Override
			public void layoutPlotChildren() {
				super.layoutPlotChildren();
				getPlotChildren().removeAll(shapes);
				shapes.clear();

				generateFlagShapes(xAxis, yAxis, flagList, shapes, offset, "Conductivity");

				getPlotChildren().addAll(shapes);
			}

		};

		lineChart.setTitle("Conductivity - " + name);

		lineChart.getData().addAll(serieslist);

		// populating the series with data

		CondTab.setText("Conductivity");
		CondTab.setContent(lineChart);
		graphList.add(lineChart);
	}

	private void setTurbTab(Tab TurbTab, String name, ArrayList<DataPoint> data, ArrayList<Flag> flagList,
			ArrayList<String> phaseNames) {
		final NumberAxis xAxis = new NumberAxis();
		final NumberAxis yAxis = new NumberAxis();
		xAxis.setLabel("Time (Seconds)");
		yAxis.setLabel("Turbidity");
		// creating the chart

		// defining a series
		int zonecount = 1;
		double offset = timeConverter.HMSToDec(data.get(0).getTime()) * 86400;

		ArrayList<Series<Number, Number>> serieslist = new ArrayList<Series<Number, Number>>();

		for (String phasename : phaseNames) {

			XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
			series.setName(phasename);
			serieslist.add(series);

		}
		for (DataPoint d : data) {
			if (d.getZone() == zonecount) {
				serieslist.get(zonecount - 1).getData().add(new XYChart.Data<Number, Number>(
						(timeConverter.HMSToDec(d.getTime()) * 86400) - offset, d.getTurbidity()));

			}
			if (d.getZone() != zonecount) {
				zonecount++;
			}
		}

		final AreaChart<Number, Number> lineChart = new AreaChart<Number, Number>(xAxis, yAxis) {

			private List<Shape> shapes = new ArrayList<>();

			@Override
			public void layoutPlotChildren() {
				super.layoutPlotChildren();
				getPlotChildren().removeAll(shapes);
				shapes.clear();

				generateFlagShapes(xAxis, yAxis, flagList, shapes, offset, "Turbidity");

				getPlotChildren().addAll(shapes);
			}

		};

		lineChart.getData().addAll(serieslist);
		lineChart.setTitle("Turbidity - " + name);
		TurbTab.setText("Turbidity");
		TurbTab.setContent(lineChart);
		graphList.add(lineChart);
	}

	private void setTempTab(Tab TempTab, String name, ArrayList<DataPoint> data, ArrayList<Flag> flagList,
			ArrayList<String> phaseNames) {

		final NumberAxis xAxis = new NumberAxis();
		final NumberAxis yAxis = new NumberAxis();
		yAxis.setAutoRanging(false);
		yAxis.setLowerBound(30);
		yAxis.setUpperBound(100);
		xAxis.setLabel("Time (Seconds)");
		yAxis.setLabel("Temperature (Degrees Celsius)");
		// creating the chart

		// defining a series
		int zonecount = 1;
		double offset = timeConverter.HMSToDec(data.get(0).getTime()) * 86400;

		ArrayList<Series<Number, Number>> serieslist = new ArrayList<Series<Number, Number>>();

		for (String phasename : phaseNames) {

			XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
			series.setName(phasename);
			serieslist.add(series);

		}

		for (DataPoint d : data) {
			if (d.getZone() == zonecount) {
				serieslist.get(zonecount - 1).getData().add(new XYChart.Data<Number, Number>(
						(timeConverter.HMSToDec(d.getTime()) * 86400) - offset, d.getTemp()));

			}
			if (d.getZone() != zonecount) {
				zonecount++;
			}
		}

		final AreaChart<Number, Number> lineChart = new AreaChart<Number, Number>(xAxis, yAxis) {

			private List<Shape> shapes = new ArrayList<>();

			@Override
			public void layoutPlotChildren() {
				super.layoutPlotChildren();
				getPlotChildren().removeAll(shapes);
				shapes.clear();

				generateFlagShapes(xAxis, yAxis, flagList, shapes, offset, "Temperature");

				getPlotChildren().addAll(shapes);
			}

		};

		lineChart.setTitle("Temperature - " + name);

		lineChart.getData().addAll(serieslist);
		TempTab.setText("Temperature");
		TempTab.setContent(lineChart);
		graphList.add(lineChart);
	}

	protected void generateFlagShapes(NumberAxis xAxis, NumberAxis yAxis, ArrayList<Flag> flagList, List<Shape> shapes,
			double offset, String type) {

		for (Flag f : flagList) {

			if (!(f.getType() == null)) {

				if (f.getType().equals(type)) {

					Polygon flagArea = new Polygon();

					double start = (timeConverter.HMSToDec(f.getStartTime()) * 86400) - offset;
					double end = (timeConverter.HMSToDec(f.getEndTime()) * 86400) - offset;

					double x1 = xAxis.getDisplayPosition(start);
					double x2 = xAxis.getDisplayPosition(end);

					flagArea.opacityProperty().set(0.05);
					flagArea.setFill(Color.RED);

					flagArea.getPoints().addAll(new Double[] { x1, 0.0, x1, 500.0, x2, 500.0, x2, 0.0 });
					shapes.add(flagArea);

					flagArea.setOnMouseMoved(new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent event) {
							flagArea.opacityProperty().set(0.28);
						}
					});
					flagArea.setOnMouseExited(new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent event) {
							flagArea.opacityProperty().set(0.05);
						}
					});

					Tooltip tooltip = new Tooltip();
					Tooltip.install(flagArea, tooltip);
					hackTooltipStartTiming(tooltip);

					// tooltip.setTextAlignment(TextAlignment.RIGHT);

					tooltip.setText(
							f.getMessage() + "\n" + "\n" + f.getPhase() + "\n" + "Expected Range: " + f.getTarget()
									+ "\n" + "Actual: " + f.getActual() + "\n" + "Duration: " + f.getDurationLabel());
				}

			}
		}
	}

	public static void hackTooltipStartTiming(Tooltip tooltip) {
		try {
			Field fieldBehavior = tooltip.getClass().getDeclaredField("BEHAVIOR");
			fieldBehavior.setAccessible(true);
			Object objBehavior = fieldBehavior.get(tooltip);

			Field fieldTimer = objBehavior.getClass().getDeclaredField("activationTimer");
			fieldTimer.setAccessible(true);
			Timeline objTimer = (Timeline) fieldTimer.get(objBehavior);

			objTimer.getKeyFrames().clear();
			objTimer.getKeyFrames().add(new KeyFrame(new Duration(100)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<AreaChart<Number, Number>> getGraphList() {
		return graphList;
	}

	public void setGraphList(ArrayList<AreaChart<Number, Number>> graphList) {
		this.graphList = graphList;
	}
}
