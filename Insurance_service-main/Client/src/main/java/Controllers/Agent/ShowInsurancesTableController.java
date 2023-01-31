package Controllers.Agent;

import basicClasses.Insurance;
import clientSettings.Client;
import helpers.Alerts;
import helpers.WindowChanger;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleFloatProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ShowInsurancesTableController implements Initializable {

    @FXML
    private TableView<String[]> tableView;

    @FXML
    private TableColumn<String[], String> insuranceColumn;

    @FXML
    private TableColumn<String[], String> priceColumn;

    @FXML
    private TableColumn<String[], Float> monthColumn;

    @FXML
    private TableColumn<String[], Float> halfYearColumn;

    @FXML
    private TableColumn<String[], Float> yearColumn;

    @FXML
    private TableColumn<String[], String> payoutColumn;

    @FXML
    private Button changeBtn;

    @FXML
    private Button deleteBtn;

    @FXML
    private Button closeBtn;

    @FXML
    private Button addBtn;

    @FXML
    private TextField searchField;

    private ArrayList<String[]> arrayList = (ArrayList<String[]>) Client.client.readObject();

    private String[] chosenInsurance;

    @FXML
    void addInsurance() {
        WindowChanger.changeWindow(getClass(), addBtn, "editInsuranceTable.fxml", "Добавление страховой услуги", true, false, true);
        Client.client.sendMessage("showInsurancesTable");
        arrayList = (ArrayList<String[]>) Client.client.readObject();
        tableView.setItems(getInsurances());
        tableView.getSelectionModel().select(tableView.getItems().size() - 1);
        changeBtn.setDisable(false);
        deleteBtn.setDisable(false);
    }

    @FXML
    void changeInsurance() throws IOException {
        try {
            chosenInsurance = tableView.getSelectionModel().getSelectedItems().get(0);
        } catch (IndexOutOfBoundsException e) {
            return;
        }
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/client/client/editInsuranceTable.fxml"));
        loader.load();
        EditInsuranceController controller = loader.getController();
        controller.setText(chosenInsurance[1], chosenInsurance[2], chosenInsurance[3]);
        controller.setId(Integer.parseInt(chosenInsurance[0]));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.getRoot()));
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        Insurance insurance = controller.getText();
        chosenInsurance[1] = insurance.getName();
        chosenInsurance[2] = String.valueOf(insurance.getPricePerMonth());
        chosenInsurance[3] = String.valueOf(insurance.getPayout());
        tableView.refresh();
    }

    @FXML
    void deleteInsurance() {
        try {
            chosenInsurance = tableView.getSelectionModel().getSelectedItems().get(0);
        } catch (IndexOutOfBoundsException e) {
            return;
        }
        if (Alerts.showConfirmAlert("Удаление записи", "Вы уверены что хотите удалить запись?")) {
            Client.client.sendMessage("deleteInsurance");
            Client.client.sendMessage(chosenInsurance[0]);
            tableView.getItems().remove(chosenInsurance);
        }
        if (tableView.getItems().size() == 0) {
            changeBtn.setDisable(true);
            deleteBtn.setDisable(true);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        insuranceColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()[1]));
        monthColumn.setCellValueFactory(param -> new SimpleFloatProperty(Float.parseFloat(param.getValue()[2])).asObject());
        halfYearColumn.setCellValueFactory(param -> new SimpleFloatProperty(Float.parseFloat(param.getValue()[2]) * 5).asObject());
        yearColumn.setCellValueFactory(param -> new SimpleFloatProperty(Float.parseFloat(param.getValue()[2]) * 9).asObject());
        payoutColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()[3]));
        monthColumn.setCellFactory(param -> new TableCell<>() {
            @Override
            protected void updateItem(Float item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", item));
                }
            }
        });
        halfYearColumn.setCellFactory(param -> new TableCell<>() {
            @Override
            protected void updateItem(Float item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", item));
                }
            }
        });
        yearColumn.setCellFactory(param -> new TableCell<>() {
            @Override
            protected void updateItem(Float item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", item));
                }
            }
        });
        tableView.setItems(getInsurances());
        if (tableView.getItems().size() == 0) {
            changeBtn.setDisable(true);
            deleteBtn.setDisable(true);
        }
        else
            tableView.getSelectionModel().select(0);

        FilteredList<String[]> filteredList = new FilteredList<>(getInsurances(), b -> true);
        searchField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                deleteBtn.setDisable(true);
                changeBtn.setDisable(true);
            }
            else {
                deleteBtn.setDisable(false);
                changeBtn.setDisable(false);
            }
        });
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(insurance -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowercase = newValue.toLowerCase();
                return insurance[1].toLowerCase().contains(lowercase) || insurance[2].toLowerCase().contains(lowercase) ||
                        insurance[3].toLowerCase().contains(lowercase) ||
                        String.valueOf(Float.parseFloat(insurance[2]) * 5).toLowerCase().contains(lowercase) ||
                        String.valueOf(Float.parseFloat(insurance[2]) * 9).toLowerCase().contains(lowercase);
            });
        });
        SortedList<String[]> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedList);
    }

    private ObservableList<String[]> getInsurances() {
        ObservableList<String[]> insurances = FXCollections.observableArrayList();
        insurances.addAll(arrayList);
        return insurances;
    }

    @FXML
    void closeWindow() {
        Stage stage = (Stage) closeBtn.getScene().getWindow();
        stage.close();
    }
}
