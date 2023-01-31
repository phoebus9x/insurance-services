package Controllers.User;

import basicClasses.Contract;
import clientSettings.Client;
import helpers.Alerts;
import helpers.WindowChanger;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class UserWindowController {

    @FXML
    private TableView<String[]> tableView;

    @FXML
    private TableColumn<String[], String> nameColumn;

    @FXML
    private TableColumn<String[], String> startDateColumn;

    @FXML
    private TableColumn<String[], String> endDateColumn;

    @FXML
    private TableColumn<String[], String> priceColumn;

    @FXML
    private TableColumn<String[], String> payoutColumn;

    @FXML
    private TableColumn<String[], String> statusColumn;

    @FXML
    private TableColumn<String[], String> agentColumn;

    @FXML
    private Button addContractBtn;

    @FXML
    private Button deleteBtn;

    @FXML
    private Button changeBtn;

    @FXML
    private Button closeBtn;

    @FXML
    private TextField searchField;

    @FXML
    private CheckBox filterCheckbox;

    private ArrayList<String[]> arrayList = (ArrayList<String[]>) Client.client.readObject();

    private String[] chosenContract;

    @FXML
    void addContract() {
        Client.client.sendMessage("showInsurancesTable");
        WindowChanger.changeWindow(getClass(), addContractBtn, "editContractTable.fxml", "Подача заявки на оформление страховой услуги",
                true, false, true);
        Client.client.sendMessage("showUserContracts");
        arrayList = (ArrayList<String[]>) Client.client.readObject();
        tableView.setItems(getContracts());
    }

    @FXML
    void change() {
        try {
            chosenContract = tableView.getSelectionModel().getSelectedItems().get(0);
            if (chosenContract[9] != null)
                return;
        } catch (IndexOutOfBoundsException e) {
            return;
        }
        Client.client.sendMessage("showInsurancesTable");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/client/client/editContractTable.fxml"));
        try {
            loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        EditContractController controller = loader.getController();
        controller.setText(Integer.parseInt(chosenContract[0]), chosenContract[3], chosenContract[4]);
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.getRoot()));
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        Contract contract = controller.getDates();
        chosenContract[3] = String.valueOf(contract.getStartDate());
        chosenContract[4] = String.valueOf(contract.getEndDate());
        tableView.refresh();
    }

    @FXML
    void delete() {
        try {
            chosenContract = tableView.getSelectionModel().getSelectedItems().get(0);
            if (chosenContract[9] != null)
                return;
        } catch (IndexOutOfBoundsException e) {
            return;
        }
        if (Alerts.showConfirmAlert("Удаление записи", "Вы уверены что хотите удалить запись?")) {
            Client.client.sendMessage("deleteContract");
            Client.client.sendMessage(chosenContract[0]);
            tableView.getItems().remove(chosenContract);
        }
        if (tableView.getItems().size() == 0) {
            changeBtn.setDisable(true);
            deleteBtn.setDisable(true);
        }
    }

    @FXML
    void filter() {
        ObservableList<String[]> newList = FXCollections.observableArrayList();
        if (filterCheckbox.isSelected()) {
            for (String[] c : getContracts()) {
                if (!Objects.equals(c[8], "В рассмотрении")) {
                    newList.add(c);
                }
            }
            tableView.setItems(newList);
        } else {
            tableView.setItems(getContracts());
        }
    }

    @FXML
    void initialize() {
        nameColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()[6]));
        startDateColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()[3]));
        endDateColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()[4]));
        priceColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()[5]));
        payoutColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()[7]));
        statusColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()[8]));
        agentColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()[9]));
        tableView.setItems(getContracts());
        if (tableView.getItems().size() == 0) {
            changeBtn.setDisable(true);
            deleteBtn.setDisable(true);
        }
        else
            tableView.getSelectionModel().select(0);

        FilteredList<String[]> filteredList = new FilteredList<>(getContracts(), b -> true);
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
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filteredList.setPredicate(contract -> {
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }
            String lowercase = newValue.toLowerCase();
            return contract[7].toLowerCase().contains(lowercase)  || contract[3].toLowerCase().contains(lowercase) ||
                    contract[4].toLowerCase().contains(lowercase) || contract[5].toLowerCase().contains(lowercase) ||
                    contract[6].toLowerCase().contains(lowercase) || contract[8].toLowerCase().contains(lowercase);
        }));
        SortedList<String[]> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedList);
    }

    private ObservableList<String[]> getContracts() {
        ObservableList<String[]> contracts = FXCollections.observableArrayList();
        contracts.addAll(arrayList);
        return contracts;
    }

    @FXML
    void close() {
        Stage stage = (Stage) closeBtn.getScene().getWindow();
        stage.close();
    }
}
