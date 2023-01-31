package helpers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class WindowChanger {
    public static void changeWindow(Class className, Button button, String fname, String title, boolean isModal, boolean isResizable, boolean isWait) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(className.getResource("/client/client/" + fname));
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent root = fxmlLoader.getRoot();
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setResizable(isResizable);
        stage.setScene(new Scene(root));
        if (isModal)
            stage.initModality(Modality.APPLICATION_MODAL);
        else
            button.getScene().getWindow().hide();
        if (isWait)
            stage.showAndWait();
        else
            stage.show();
    }

    public static void changeWindow(Class className, Button button, String fname, String title, boolean isModal, boolean isResizable) {
        changeWindow(className, button, fname, title, isModal, isResizable, false);
    }

    public static void changeWindow(Class className, Button button, String fname, String title, boolean isModal) {
        changeWindow(className, button, fname, title, isModal, true, false);
    }

    public static void changeWindow(Class className, Button button, String fname, String title) {
        changeWindow(className, button, fname, title, false, true, false);
    }
}
