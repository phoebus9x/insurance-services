package Controllers.User;

import basicClasses.Contract;
import clientSettings.Client;
import helpers.Alerts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.ArrayList;

public class EditContractController {

    @FXML
    private DatePicker startDate;

    @FXML
    private DatePicker endDate;

    @FXML
    private TextField priceField;

    @FXML
    private ComboBox<String> comboBox;

    @FXML
    private Button saveBtn;

    private ArrayList<String[]> insuranceList = (ArrayList<String[]>) Client.client.readObject();

    private int idContract;

    private boolean isUpdate = false;

    @FXML
    void save() {
        if (comboBox.getSelectionModel().getSelectedIndex() == -1) {
            Alerts.showErrorAlert("Ошибка", "Выберите страховую услугу");
            return;
        }
        Contract contract = new Contract();
        contract.setStartDate(startDate.getValue());
        contract.setEndDate(endDate.getValue());
        contract.setIdService(getIdService(comboBox.getSelectionModel().getSelectedIndex()));
        if (isUpdate) {
            Client.client.sendMessage("editContract");
            Alerts.showInfoAlert("Изменение данных", "Данные успешно обновлены");
        }
        else {
            Client.client.sendMessage("addContract");
            Alerts.showInfoAlert("Оформление страховой услуги", "Заявка принята к рассмотрению");
        }
        Client.client.sendObject(contract);
        Stage stage = (Stage) saveBtn.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void initialize() {
        comboBox.setItems(getInsurances());
        comboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) ->
                priceField.setText(getPrice(comboBox.getSelectionModel().getSelectedIndex())));
        endDate.setEditable(false);
        startDate.setEditable(false);
        startDate.setDayCellFactory(param -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(item.isBefore(LocalDate.now()));
            }
        });
        startDate.valueProperty().addListener((ov, oldValue, newValue) -> {
            endDate.setValue(newValue.plusDays(2));
            endDate.setDayCellFactory(param -> new DateCell() {
                @Override
                public void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    setDisable(item.isBefore(newValue.plusDays(2)));
                }
            });
        });
        if (!isUpdate) {
            startDate.setValue(LocalDate.now());
            endDate.setValue(LocalDate.now().plusDays(2));
        }
    }

    private String getPrice(int selectedIndex) {
        return insuranceList.get(selectedIndex)[2];
    }

    private int getIdService(int selectedIndex) {
        return Integer.parseInt(insuranceList.get(selectedIndex)[0]);
    }

    private ObservableList<String> getInsurances() {
        ObservableList<String> insurances = FXCollections.observableArrayList();
        for (String[] row : insuranceList) {
            insurances.add(row[1]);
        }
        return insurances;
    }

    public void setText(int id_contract, String start_date, String end_date) {
        startDate.setValue(LocalDate.parse(start_date));
        endDate.setValue(LocalDate.parse(end_date));
        idContract = id_contract;
        isUpdate = true;
    }

    public Contract getDates() {
        Contract contract = new Contract();
        contract.setStartDate(startDate.getValue());
        contract.setEndDate(endDate.getValue());
        return contract;
    }
}
