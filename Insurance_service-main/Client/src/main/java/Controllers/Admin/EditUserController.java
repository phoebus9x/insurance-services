package Controllers.Admin;

import basicClasses.User;
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

public class EditUserController implements Initializable {

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
    private TextField passportField;

    @FXML
    private TextField passwordField;

    @FXML
    private Button saveBtn;

    private int idUser;

    @FXML
    void save() {
        if (isEmpty()) {
            Alerts.showErrorAlert("Ошибка ввода", "Заполните пустые поля!");
            return;
        }
        Client.client.sendMessage("editUser");
        User user = new User(idUser, surnameField.getText(), nameField.getText(), birthdayField.getValue(),
                phoneField.getText(), addressField.getText(), passportField.getText());
        Client.client.sendObject(user);
        Alerts.showInfoAlert("Изменение данных", "Данные клиента успешно обновлены");
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
    }

    public void setId(int idUser) {
        this.idUser= idUser;
    }

    public void setText(String login, String surname, String name, String birthday, String phone, String address, String passport, String password) {
        loginField.setText(login);
        surnameField.setText(surname);
        nameField.setText(name);
        birthdayField.setValue(LocalDate.parse(birthday));
        phoneField.setText(phone);
        addressField.setText(address);
        passportField.setText(passport);
        passwordField.setText(password);
    }

    private boolean isEmpty() {
        return Objects.equals(surnameField.getText(), "") || Objects.equals(nameField.getText(), "") ||
                Objects.equals(passportField.getText(), "") || Objects.equals(phoneField.getText(), "") ||
                Objects.equals(addressField.getText(), "");
    }

    public User getText() {
        return new User(surnameField.getText(), nameField.getText(), birthdayField.getValue(), phoneField.getText(), addressField.getText(), passportField.getText());
    }
}
