package Main;


import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.AreaChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.WritableImage;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import samThreshold.Flag;

public class ExportData {

	public void exportData(ArrayList<AreaChart<Number, Number>> graphList, String name, ArrayList<Flag> flagList,
			File excelFile, Stage stage) {

		FileChooser fileChooser = new FileChooser();

		fileChooser.setTitle(" Save/Export Summary");
		fileChooser.setInitialFileName(
				"Flag Summary - " + excelFile.getName().substring(0, excelFile.getName().lastIndexOf(".")) + ".txt");

		File file = fileChooser.showSaveDialog(stage);

	

		if (file != null) {
			try {
				PrintWriter writer = new PrintWriter(file, "UTF-8");

				writer.println();
				writer.println(
						"Flag Summary: " + excelFile.getName().substring(0, excelFile.getName().lastIndexOf(".")));

				for (Flag flag : flagList) {

					writer.println("\nFlag: " + flag.getMessage());
					writer.println("Phase: " + flag.getPhase());
					writer.println("Start: " + flag.getStartTime());
					writer.println("End: " + flag.getEndTime());
					writer.println("Threshold: " + flag.getTarget());
					writer.println("Actual: " + flag.getActual());

				}

				writer.close();

				Alert success = new Alert(AlertType.INFORMATION);
				// this line allows the alert box to accept a gridpane for
				// displaying the flags
				success.setTitle("Information Dialog");
				success.setHeaderText("Flags Exported Successfully");
				success.showAndWait();

			} catch (IOException ex) {
				System.out.println(ex.getMessage());
			}
		}

		for (AreaChart<Number, Number> a : graphList) {

			WritableImage image = a.snapshot(new SnapshotParameters(), null);

			System.out.println(image.getWidth());

			File file1 = new File(file.getParentFile() + "\\" + a.getTitle() + ".png");
			file1.getParentFile().mkdirs();

			try {
				ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file1);
			} catch (IOException e) {
				// TODO: handle exception here
			}
		}
	}
}
