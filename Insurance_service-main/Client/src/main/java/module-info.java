module client.client {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires javafx.graphics;
    requires java.sql;
    requires java.desktop;

    opens client.client to javafx.fxml;
    exports client.client;
    exports Controllers;
    opens Controllers to javafx.fxml;
    exports Controllers.User;
    opens Controllers.User to javafx.fxml;
    exports Controllers.Agent;
    opens Controllers.Agent to javafx.fxml;
    exports Controllers.Admin;
    opens Controllers.Admin to javafx.fxml;
    exports helpers;
    opens helpers to javafx.fxml;
}