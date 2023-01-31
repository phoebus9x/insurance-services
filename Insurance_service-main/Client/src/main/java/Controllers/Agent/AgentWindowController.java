package Controllers.Agent;

import clientSettings.Client;
import helpers.WindowChanger;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AgentWindowController {
    @FXML
    private Button insuranceControlBtn;

    @FXML
    private Button contractWorkBtn;

    @FXML
    private Button showClientsBtn;

    @FXML
    private Button closeBtn;

    @FXML
    void close() {
        Stage stage = (Stage) closeBtn.getScene().getWindow();
        stage.close();
    }

    @FXML
    void showContracts() {
        Client.client.sendMessage("showAgentContracts");
        WindowChanger.changeWindow(getClass(), contractWorkBtn, "agentContractControl.fxml", "Работа с заявками", true);
    }

    @FXML
    void showInsurancesTable() {
        Client.client.sendMessage("showInsurancesTable");
        WindowChanger.changeWindow(getClass(), insuranceControlBtn, "showInsurancesTable.fxml", "Управление страховыми услугами", true, true);
    }

    @FXML
    public void showClientsTable() {
        Client.client.sendMessage("showUsersTable");
        WindowChanger.changeWindow(getClass(), showClientsBtn, "showUsers.fxml", "Просмотр клиентов", true, true);
    }
}
