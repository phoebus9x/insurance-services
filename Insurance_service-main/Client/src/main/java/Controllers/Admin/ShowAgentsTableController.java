package Controllers.Admin;

import basicClasses.Agent;
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
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class ShowAgentsTableController implements Initializable {
    @FXML
    private TableView<String[]> tableView;

    @FXML
    private TableColumn<String[], String> loginColumn;

    @FXML
    private TableColumn<String[], String> emplDateColumn;

    @FXML
    private TableColumn<String[], String> surnameColumn;

    @FXML
    private TableColumn<String[], String> nameColumn;

    @FXML
    private TableColumn<String[], String> birthdayColumn;

    @FXML
    private TableColumn<String[], String> phoneColumn;

    @FXML
    private TableColumn<String[], String> addressColumn;

    @FXML
    private Button addBtn;

    @FXML
    private Button changeBtn;

    @FXML
    private Button deleteBtn;

    @FXML
    private Button closeBtn;

    @FXML
    private TextField searchField;

    private ArrayList<String[]> arrayList = (ArrayList<String[]>) Client.client.readObject();

    private String[] chosenAgent;

    @FXML
    void addAgent() {
        WindowChanger.changeWindow(getClass(), addBtn, "editAgentTable.fxml", "Добавление страхового агента", true, false, true);
        Client.client.sendMessage("showAgentsTable");
        arrayList = (ArrayList<String[]>) Client.client.readObject();
        tableView.setItems(getAgents());
        tableView.getSelectionModel().select(tableView.getItems().size() - 1);
        changeBtn.setDisable(false);
        deleteBtn.setDisable(false);
    }

    @FXML
    void changeAgent() throws IOException {
        chosenAgent = tableView.getSelectionModel().getSelectedItems().get(0);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/client/client/editAgentTable.fxml"));
        loader.load();
        EditAgentController controller = loader.getController();
        controller.setText(chosenAgent[6], chosenAgent[1], chosenAgent[2], chosenAgent[3], chosenAgent[4], chosenAgent[5], chosenAgent[8], chosenAgent[7]);
        controller.setId(Integer.parseInt(chosenAgent[0]));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.getRoot()));
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        Agent agent = controller.getText();
        chosenAgent[1] = agent.getSurname();
        chosenAgent[2] = agent.getName();
        chosenAgent[3] = agent.getBirthday().toString();
        chosenAgent[4] = agent.getPhone();
        chosenAgent[5] = agent.getAddress();
        chosenAgent[8] = agent.getEmploymentDate().toString();
        tableView.refresh();
    }

    @FXML
    void closeWindow() {
        Stage stage = (Stage) closeBtn.getScene().getWindow();
        stage.close();
    }

    @FXML
    void deleteAgent() {
        chosenAgent = tableView.getSelectionModel().getSelectedItems().get(0);
        if (Alerts.showConfirmAlert("Удаление записи", "Вы уверены что хотите удалить запись?")) {
            Client.client.sendMessage("deleteAgent");
            Client.client.sendMessage(chosenAgent[0]);
            if (Objects.equals(Client.client.readMessage(), "error")) {
                Alerts.showErrorAlert("Ошибка удаления", "Невозможно удалить страхового агента, так как он уже заключил страховой договор");
            }
            else {
                tableView.getItems().remove(chosenAgent);
                if (tableView.getItems().size() == 0) {
                    changeBtn.setDisable(true);
                    deleteBtn.setDisable(true);
                }
            }
        }
        if (tableView.getItems().size() == 0) {
            changeBtn.setDisable(true);
            deleteBtn.setDisable(true);
        }
    }

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()[6]));
        surnameColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()[1]));
        nameColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()[2]));
        birthdayColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()[3]));
        phoneColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()[4]));
        addressColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()[5]));
        emplDateColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()[8]));
        tableView.setItems(getAgents());
        if (tableView.getItems().size() == 0) {
            changeBtn.setDisable(true);
            deleteBtn.setDisable(true);
        }
        else
            tableView.getSelectionModel().select(0);

        FilteredList<String[]> filteredList = new FilteredList<>(getAgents(), b -> true);
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
            filteredList.setPredicate(agent -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowercase = newValue.toLowerCase();
                return agent[1].toLowerCase().contains(lowercase) || agent[2].toLowerCase().contains(lowercase) ||
                        agent[3].toLowerCase().contains(lowercase) || agent[4].toLowerCase().contains(lowercase) ||
                        agent[5].toLowerCase().contains(lowercase) || agent[6].toLowerCase().contains(lowercase) ||
                        agent[8].toLowerCase().contains(lowercase);
            });
        });
        SortedList<String[]> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedList);
    }

    private ObservableList<String[]> getAgents() {
        ObservableList<String[]> agents = FXCollections.observableArrayList();
        agents.addAll(arrayList);
        return agents;
    }
}
