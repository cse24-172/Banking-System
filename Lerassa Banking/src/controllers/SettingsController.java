package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import util.SceneSwitcher;

public class SettingsController {

    @FXML private Button editProfileBtn;
    @FXML private Button changePasswordBtn;
    @FXML private Button notificationsBtn;
    @FXML private Button backBtn;

    @FXML
    public void initialize() {

        editProfileBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Edit Profile");
            alert.setContentText("This feature is not implemented yet.");
            alert.show();
        });

        changePasswordBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Change Password");
            alert.setContentText("This feature is not implemented yet.");
            alert.show();
        });

        notificationsBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Notification Settings");
            alert.setContentText("This feature is not implemented yet.");
            alert.show();
        });

        // BACK BUTTON — correct way for SceneSwitcher(Stage, fxml)
        backBtn.setOnAction(e -> {
            Stage stage = (Stage) backBtn.getScene().getWindow();
            SceneSwitcher.switchTo(stage, "customer_dashboard.fxml");
        });
    }
}
