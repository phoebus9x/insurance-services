package Controllers.User;

import basicClasses.*;
import helpers.*;
import clientSettings.Client;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;

public class UserRegistrationController implements Initializable {

    @FXML
    private TextField loginField;

    @FXML
    private Button registrationBtn;

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
    void registerUser() {
        if (isEmpty()) {
            Alerts.showErrorAlert("Ошибка ввода", "Заполните пустые поля!");
            return;
        }
        Client.client.sendMessage("registration");
        User user = new User(surnameField.getText(), nameField.getText(), birthdayField.getValue(),
                phoneField.getText(), addressField.getText(), passportField.getText());
        Authorization authorization = new Authorization(loginField.getText(), passwordField.getText());
        Client.client.sendObject(user);
        Client.client.sendObject(authorization);
        Alerts.showInfoAlert("Регистрация", "Регистрация прошла успешно");
        Stage stage = (Stage) registrationBtn.getScene().getWindow();
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
        birthdayField.setValue(Validation.DEFAULT_DATE);
        birthdayField.setEditable(false);
        birthdayField.setDayCellFactory(param -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(item.isAfter(LocalDate.now()));
            }
        });
    }

    private boolean isEmpty() {
        return Objects.equals(surnameField.getText(), "") || Objects.equals(nameField.getText(), "") ||
                Objects.equals(loginField.getText(), "") || Objects.equals(passwordField.getText(), "") ||
                Objects.equals(phoneField.getText(), "") || Objects.equals(passportField.getText(), "") ||
                Objects.equals(addressField.getText(), "");
    }
}
