package helpers;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class Alerts {
    public static void showErrorAlert(String title, String errorText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        setAlert(alert, title, errorText);
        alert.showAndWait();
    }

    public static void showInfoAlert(String title, String errorText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        setAlert(alert, title, errorText);
        alert.showAndWait();
    }

    public static boolean showConfirmAlert(String title, String errorText) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        setAlert(alert, title, errorText);
        alert.getButtonTypes().clear();
        ButtonType yes = new ButtonType("Да");
        ButtonType cancel = new ButtonType("Отмена");
        alert.getButtonTypes().add(yes);
        alert.getButtonTypes().add(cancel);
        return alert.showAndWait().get() == yes;
    }

    private static void setAlert(Alert alert, String title, String errorText) {
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(errorText);
    }
}
