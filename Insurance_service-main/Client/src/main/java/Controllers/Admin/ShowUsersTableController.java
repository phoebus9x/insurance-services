package Controllers.Admin;

import basicClasses.User;
import clientSettings.Client;
import helpers.Alerts;
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
import java.util.ArrayList;
import java.util.Objects;

public class ShowUsersTableController {
    @FXML
    private TableView<String[]> tableView;

    @FXML
    private TableColumn<String[], String> loginColumn;

    @FXML
    private TableColumn<String[], String> passportColumn;

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
    private Button changeBtn;

    @FXML
    private Button deleteBtn;

    @FXML
    private Button closeBtn;

    @FXML
    public TextField searchField;

    private final ArrayList<String[]> arrayList = (ArrayList<String[]>) Client.client.readObject();

    private String[] chosenUser;

    @FXML
    void changeUser() throws IOException {
        chosenUser = tableView.getSelectionModel().getSelectedItems().get(0);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/client/client/editUserTable.fxml"));
        loader.load();
        EditUserController controller = loader.getController();
        controller.setText(chosenUser[6], chosenUser[1], chosenUser[2], chosenUser[3], chosenUser[4], chosenUser[5], chosenUser[8], chosenUser[7]);
        controller.setId(Integer.parseInt(chosenUser[0]));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.getRoot()));
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        User user = controller.getText();
        chosenUser[1] = user.getSurname();
        chosenUser[2] = user.getName();
        chosenUser[3] = user.getBirthday().toString();
        chosenUser[4] = user.getPhone();
        chosenUser[5] = user.getAddress();
        chosenUser[8] = user.getPassport();
        tableView.refresh();
    }

    @FXML
    void deleteUser() {
        chosenUser = tableView.getSelectionModel().getSelectedItems().get(0);
        if (Alerts.showConfirmAlert("Удаление записи", "Вы уверены что хотите удалить запись?")) {
            Client.client.sendMessage("deleteUser");
            Client.client.sendMessage(chosenUser[0]);
            if (Objects.equals(Client.client.readMessage(), "error")) {
                Alerts.showErrorAlert("Ошибка удаления", "Невозможно удалить клиента, так как он уже заключил страховой договор");
            }
            else {
                tableView.getItems().remove(chosenUser);
                if (tableView.getItems().size() == 0) {
                    changeBtn.setDisable(true);
                    deleteBtn.setDisable(true);
                }
            }
        }
    }

    @FXML
    void initialize() {
        loginColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()[6]));
        surnameColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()[1]));
        nameColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()[2]));
        birthdayColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()[3]));
        phoneColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()[4]));
        addressColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()[5]));
        passportColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()[8]));
        tableView.setItems(getUsers());
        if (tableView.getItems().size() == 0) {
            changeBtn.setDisable(true);
            deleteBtn.setDisable(true);
        }
        else
            tableView.getSelectionModel().select(0);

        FilteredList<String[]> filteredList = new FilteredList<>(getUsers(), b -> true);
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
            filteredList.setPredicate(user -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowercase = newValue.toLowerCase();
                return user[1].toLowerCase().contains(lowercase) || user[2].toLowerCase().contains(lowercase) ||
                        user[3].toLowerCase().contains(lowercase) || user[4].toLowerCase().contains(lowercase) ||
                        user[5].toLowerCase().contains(lowercase) || user[6].toLowerCase().contains(lowercase) ||
                        user[8].toLowerCase().contains(lowercase);
            });
        });
        SortedList<String[]> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedList);
    }

    private ObservableList<String[]> getUsers() {
        ObservableList<String[]> users = FXCollections.observableArrayList();
        users.addAll(arrayList);
        if (users.size() == 0) {
            changeBtn.setDisable(true);
            deleteBtn.setDisable(true);
        }
        return users;
    }

    @FXML
    void closeWindow() {
        Stage stage = (Stage) closeBtn.getScene().getWindow();
        stage.close();
    }
}
