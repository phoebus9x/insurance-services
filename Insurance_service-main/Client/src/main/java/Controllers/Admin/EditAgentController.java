package Controllers.Admin;

import basicClasses.*;
import clientSettings.Client;
import helpers.Alerts;
import helpers.Validation;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;

public class EditAgentController implements Initializable {

    @FXML
    private TextField loginField;

    @FXML
    private TextField surnameField;

    @FXML
    private TextField nameField;

    @FXML
    private DatePicker birthdayField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField addressField;

    @FXML
    private DatePicker emplDateField;

    @FXML
    private TextField passwordField;

    @FXML
    private Button saveBtn;

    private int idUser;

    private boolean isUpdate = false;

    @FXML
    void save() {
        if (isEmpty()) {
            Alerts.showErrorAlert("Ошибка ввода", "Заполните пустые поля!");
            return;
        }
        Agent agent = new Agent(surnameField.getText(), nameField.getText(), birthdayField.getValue(),
                phoneField.getText(), addressField.getText(), emplDateField.getValue());
        Authorization authorization = new Authorization();
        if (isUpdate) {
            Client.client.sendMessage("editAgent");
            agent.setIdUser(idUser);
            Alerts.showInfoAlert("Изменение данных", "Данные страхового агента успешно обновлены");
        }
        else {
            authorization.setLogin(loginField.getText());
            authorization.setPassword(passwordField.getText());
            Client.client.sendMessage("addAgent");
            Client.client.sendObject(authorization);
            Alerts.showInfoAlert("Добавление страхового агента", "Страховой агент добавлен");
        }
        Client.client.sendObject(agent);
        Stage stage = (Stage) saveBtn.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        phoneField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue && Objects.equals(phoneField.getText(), "")) {
                phoneField.setText("375");
            }
        });
        phoneField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(Validation.INTEGER)) {
                phoneField.setText(oldValue);
            }
        });
        nameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(Validation.STRING)) {
                nameField.setText(oldValue);
            }
        });
        surnameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(Validation.STRING)) {
                surnameField.setText(oldValue);
            }
        });
        loginField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(Validation.ALPHANUMERIC)) {
                loginField.setText(oldValue);
            }
        });
        birthdayField.setEditable(false);
        birthdayField.setDayCellFactory(param -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(item.isAfter(LocalDate.now()));
            }
        });
        emplDateField.setEditable(false);
        emplDateField.setDayCellFactory(param -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(item.isAfter(LocalDate.now()));
            }
        });
        if (!isUpdate) {
            birthdayField.setValue(Validation.DEFAULT_DATE);
            emplDateField.setValue(LocalDate.now());
        }
    }

    public void setId(int idUser) {
        this.idUser= idUser;
        this.isUpdate = true;
        loginField.setOpacity(0.5);
        loginField.setDisable(true);
        passwordField.setOpacity(0.5);
        passwordField.setDisable(true);
    }

    public void setText(String login, String surname, String name, String birthday, String phone, String address, String emplDate, String password) {
        loginField.setText(login);
        surnameField.setText(surname);
        nameField.setText(name);
        birthdayField.setValue(LocalDate.parse(birthday));
        phoneField.setText(phone);
        addressField.setText(address);
        emplDateField.setValue(LocalDate.parse(emplDate));
        passwordField.setText(password);
    }

    public Agent getText() {
        return new Agent(surnameField.getText(), nameField.getText(), birthdayField.getValue(), phoneField.getText(), addressField.getText(), emplDateField.getValue());
    }

    private boolean isEmpty() {
        return Objects.equals(surnameField.getText(), "") || Objects.equals(nameField.getText(), "") ||
                Objects.equals(phoneField.getText(), "") || Objects.equals(addressField.getText(), "") ||
                Objects.equals(loginField.getText(), "") || Objects.equals(passwordField.getText(), "");
    }
}
