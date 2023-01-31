package Controllers.Agent;

import clientSettings.Client;
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

public class ShowUsers {
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
    private Button closeBtn;

    @FXML
    private TextField searchField;

    private final ArrayList<String[]> arrayList = (ArrayList<String[]>) Client.client.readObject();

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

        FilteredList<String[]> filteredList = new FilteredList<>(getUsers(), b -> true);
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
        return users;
    }

    @FXML
    void closeWindow() {
        Stage stage = (Stage) closeBtn.getScene().getWindow();
        stage.close();
    }
}
