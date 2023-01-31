package Controllers.Agent;

import basicClasses.Insurance;
import clientSettings.Client;
import helpers.Alerts;
import helpers.Validation;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class EditInsuranceController implements Initializable {

    @FXML
    private TextField nameField;

    @FXML
    private TextField pricePerMonthField;

    @FXML
    private TextField payoutField;

    @FXML
    private Button saveBtn;

    private int idService;

    private boolean isUpdate = false;

    @FXML
    void save() {
        if (isEmpty()) {
            Alerts.showErrorAlert("Ошибка ввода", "Заполните пустые поля!");
            return;
        }
        Insurance insurance = new Insurance(nameField.getText(), Float.parseFloat(pricePerMonthField.getText()),
                Float.parseFloat(payoutField.getText()));
        if (isUpdate) {
            Client.client.sendMessage("editInsurance");
            insurance.setIdInsurance(idService);
            Alerts.showInfoAlert("Изменение данных", "Данные о страховой услуге успешно обновлены");
        }
        else {
            Client.client.sendMessage("addInsurance");
            Alerts.showInfoAlert("Добавление страховой услуги", "Страховая услгуга добавлен");
        }
        Client.client.sendObject(insurance);
        Stage stage = (Stage) saveBtn.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(Validation.STRINGWSPACE)) {
                nameField.setText(oldValue);
            }
        });
        pricePerMonthField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(Validation.FLOAT)) {
                pricePerMonthField.setText(oldValue);
            }
        });
        payoutField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(Validation.FLOAT)) {
                payoutField.setText(oldValue);
            }
        });
    }

    private boolean isEmpty() {
        return Objects.equals(nameField.getText(), "") || Objects.equals(pricePerMonthField.getText(), "") ||
                Objects.equals(payoutField.getText(), "");
    }

    public void setText(String name, String pricePerMonth, String payout) {
        nameField.setText(name);
        pricePerMonthField.setText(pricePerMonth);
        payoutField.setText(payout);
    }

    public void setId(int idService) {
        this.idService= idService;
        this.isUpdate = true;
    }

    public Insurance getText() {
        return new Insurance(nameField.getText(), Float.parseFloat(pricePerMonthField.getText()), Float.parseFloat(payoutField.getText()));
    }
}
