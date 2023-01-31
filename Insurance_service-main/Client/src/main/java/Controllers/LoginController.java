package Controllers;

import basicClasses.Authorization;
import clientSettings.Client;
import helpers.Alerts;
import helpers.WindowChanger;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class LoginController {

    @FXML
    private TextField loginField;

    @FXML
    private Button loginBtn;

    @FXML
    private Button gotoRegWindowBtn;

    @FXML
    private PasswordField passwordField;

    @FXML
    void passKeyPressed(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER))
            login();
    }

    @FXML
    void login() {
        Client.client.sendMessage("login");
        Authorization authorization = new Authorization(loginField.getText(), passwordField.getText());
        Client.client.sendObject(authorization);
        String ans = Client.client.readMessage();
        switch (ans) {
            case "user" -> {
                Client.client.sendMessage("showUserContracts");
                WindowChanger.changeWindow(getClass(), loginBtn, "userWindow.fxml", "Меню клиента");
            }
            case "admin" -> WindowChanger.changeWindow(getClass(), loginBtn, "adminWindow.fxml", "Меню администратора", false, false);
            case "agent" -> WindowChanger.changeWindow(getClass(), loginBtn, "agentWindow.fxml", "Меню страхового агента", false, false);
            default -> Alerts.showErrorAlert("Ошибка авторизации", "Неверный логин (пароль)");
        }
    }

    @FXML
    void registerUser() {
        WindowChanger.changeWindow(getClass(), gotoRegWindowBtn, "userRegistration.fxml", "Регистрация", true, false);
    }
}