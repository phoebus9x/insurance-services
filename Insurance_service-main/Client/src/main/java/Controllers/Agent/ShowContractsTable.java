package Controllers.Agent;

import clientSettings.Client;
import helpers.Alerts;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Objects;

public class ShowContractsTable {

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
    private TableColumn<String[], String> clientColumn;

    @FXML
    private Button approveBtn;

    @FXML
    private Button rejectBtn;

    @FXML
    private Button closeBtn;

    @FXML
    private TextField searchField;

    private ArrayList<String[]> arrayList = (ArrayList<String[]>) Client.client.readObject();

    private String[] chosenContract;

    @FXML
    void approveContract() {
        try {
            chosenContract = tableView.getSelectionModel().getSelectedItems().get(0);
            if (chosenContract[9] != null)
                return;
        } catch (IndexOutOfBoundsException e) {
            return;
        }
        if (Alerts.showConfirmAlert("Утверждение заявки клиента", "Одобрить заявку клиента?")) {
            Client.client.sendMessage("changeStatus");
            Client.client.sendMessage(chosenContract[0]);
            Client.client.sendMessage("2");
            chosenContract[8] = "Одобрена";
            chosenContract[9] = Client.client.readMessage();
            tableView.refresh();
            tableView.getSelectionModel().select(0);
        }
    }

    @FXML
    void rejectContract() {
        try {
            chosenContract = tableView.getSelectionModel().getSelectedItems().get(0);
            if (chosenContract[9] != null)
                return;
        } catch (IndexOutOfBoundsException e) {
            return;
        }
        if (Alerts.showConfirmAlert("Отклонение заявки клиента", "Отклонить заявку клиента?")) {
            Client.client.sendMessage("changeStatus");
            Client.client.sendMessage(chosenContract[0]);
            Client.client.sendMessage("3");
            chosenContract[8] = "Отклонена";
            chosenContract[9] = Client.client.readMessage();
            tableView.refresh();
            tableView.getSelectionModel().select(0);
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
        clientColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()[10]));
        tableView.setItems(getContracts());
        if (tableView.getItems().size() == 0) {
            approveBtn.setDisable(true);
            rejectBtn.setDisable(true);
        }
        else
            tableView.getSelectionModel().select(0);

        FilteredList<String[]> filteredList = new FilteredList<>(getContracts(), b -> true);
        searchField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                approveBtn.setDisable(true);
                rejectBtn.setDisable(true);
            }
            else {
                approveBtn.setDisable(false);
                rejectBtn.setDisable(false);
            }
        });
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(contract -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowercase = newValue.toLowerCase();
                return contract[7].toLowerCase().contains(lowercase)  || contract[3].toLowerCase().contains(lowercase) ||
                        contract[4].toLowerCase().contains(lowercase) || contract[5].toLowerCase().contains(lowercase) ||
                        contract[6].toLowerCase().contains(lowercase) || contract[8].toLowerCase().contains(lowercase) ||
                        contract[10].toLowerCase().contains(lowercase);
            });
        });
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
