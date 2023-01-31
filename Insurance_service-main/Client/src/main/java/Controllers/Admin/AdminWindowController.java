package Controllers.Admin;

import clientSettings.Client;
import helpers.WindowChanger;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.Desktop;

public class AdminWindowController {

    @FXML
    private Button agentControlBtn;

    @FXML
    private Button userControlBtn;

    @FXML
    private Button closeBtn;

    @FXML
    void close() {
        Stage stage = (Stage) closeBtn.getScene().getWindow();
        stage.close();
    }

    @FXML
    void showAgentsReport() {
        Client.client.sendMessage("showAgentsReport");
        ArrayList<String[]> arrayList = (ArrayList<String[]>) Client.client.readObject();
        PieChart pieChart = new PieChart();

        for (String[] strings : arrayList) {
            pieChart.getData().add(new PieChart.Data(strings[0], Integer.parseInt(strings[1])));
        }

        pieChart.setPrefSize(500, 400);

        pieChart.setLegendSide(Side.LEFT);
        pieChart.setStartAngle(30);

        final Label caption = new Label("");
        caption.setTextFill(Color.WHITE);
        caption.setStyle("-fx-font: 16 arial;");

        for (final PieChart.Data data : pieChart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
                caption.setTranslateX(e.getSceneX() - 9);
                caption.setTranslateY(e.getSceneY() - 17);
                caption.setText(String.valueOf(Math.round(data.getPieValue())));
            });
        }
        AnchorPane root = new AnchorPane();
        root.getChildren().addAll(pieChart, caption);
        Stage stage = new Stage();
        stage.setTitle("Отчет по страховым агентам");
        stage.setScene(new Scene(root, 500, 400));
        stage.show();
    }

    @FXML
    void showReport() {
        Client.client.sendMessage("showStatusReport");
        ArrayList<String[]> arrayList = (ArrayList<String[]>) Client.client.readObject();
        PieChart pieChart = new PieChart();

        PieChart.Data slice1 = new PieChart.Data(arrayList.get(0)[0], Integer.parseInt(arrayList.get(0)[1]));
        PieChart.Data slice2 = new PieChart.Data(arrayList.get(1)[0], Integer.parseInt(arrayList.get(1)[1]));
        PieChart.Data slice3 = new PieChart.Data(arrayList.get(2)[0], Integer.parseInt(arrayList.get(2)[1]));

        pieChart.getData().add(slice1);
        pieChart.getData().add(slice2);
        pieChart.getData().add(slice3);

        pieChart.setPrefSize(500, 400);

        pieChart.setLegendSide(Side.LEFT);
        pieChart.setStartAngle(30);

        final Label caption = new Label("");
        caption.setTextFill(Color.WHITE);
        caption.setStyle("-fx-font: 16 arial;");

        for (final PieChart.Data data : pieChart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
                caption.setTranslateX(e.getSceneX() - 9);
                caption.setTranslateY(e.getSceneY() - 17);
                caption.setText(String.valueOf(Math.round(data.getPieValue())));
            });
        }
        AnchorPane root = new AnchorPane();
        root.getChildren().addAll(pieChart, caption);
        Stage stage = new Stage();
        stage.setTitle("Отчет по страховым услугам");
        stage.setScene(new Scene(root, 500, 400));
        stage.show();
    }

    @FXML
    void showFinancialReport() {
        Client.client.sendMessage("showTxtReport");
        ArrayList<String[]> arrayList = (ArrayList<String[]>) Client.client.readObject();
        try {
            int num = 1, tpKol, tmKol, tnKol;
            double tpSum, tmSum, tnSum;
            tpKol = tmKol = tnKol = 0;
            tpSum = tmSum = tnSum = 0.0;
            FileWriter writer = new FileWriter("financialReport.txt");
            writer.write("| №|           Название страховой            |    Одобрено    |    Отклонено   | Не рассмотрено |\n");
            writer.write("|  |                 услуги                  | Кол-во | Сумма | Кол-во | Сумма | Кол-во | Сумма |\n");
            writer.write("=================================================================================================\n");
            for (String[] ins : arrayList) {
                tpKol += Integer.parseInt(ins[1]); tpSum += Double.parseDouble(ins[2]);
                tmKol += Integer.parseInt(ins[3]); tmSum += Double.parseDouble(ins[4]);
                tnKol += Integer.parseInt(ins[5]); tnSum += Double.parseDouble(ins[6]);
                writer.write(String.format("|%2d| %-40s|%5s   |%5s  |%5s   |%5s  |%5s   |%5s  |\n",
                        num++, ins[0], ins[1], ins[2], ins[3], ins[4], ins[5], ins[6]));
            }
            writer.write("-------------------------------------------------------------------------------------------------\n");
            writer.write(String.format("|%25s%20s%5d   |%5.1f  |%5d   |%5.1f  |%5d   |%5.1f  |\n",
                    "Итого", "|", tpKol, tpSum, tmKol, tmSum, tnKol, tnSum));
            writer.write("=================================================================================================\n");
            writer.flush();
            writer.close();
            File file = new File("financialReport.txt");
            Desktop desktop = Desktop.getDesktop();
            if (file.exists())
                desktop.open(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void showAgentsTable() {
        Client.client.sendMessage("showAgentsTable");
        WindowChanger.changeWindow(getClass(), agentControlBtn, "showAgentsTable.fxml", "Управление страховыми агентами", true);
    }

    @FXML
    void showUsersTable() {
        Client.client.sendMessage("showUsersTable");
        WindowChanger.changeWindow(getClass(), userControlBtn, "showUsersTable.fxml", "Управление пользователями", true);
    }
}
