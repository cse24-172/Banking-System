package util;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneSwitcher {

    // ----- SWITCH USING EVENT (click, button, etc.) -----
    public static void switchTo(Event event, String fxmlName) {
        try {
            Parent root = FXMLLoader.load(
                SceneSwitcher.class.getResource("/view/" + fxmlName)
            );

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            System.out.println("❌ Could not load FXML: " + fxmlName);
            e.printStackTrace();
        }
    }

    // ----- SWITCH USING STAGE (manual navigation) -----
    public static void switchTo(Stage stage, String fxmlName) {
        try {
            Parent root = FXMLLoader.load(
                SceneSwitcher.class.getResource("/view/" + fxmlName)
            );

            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            System.out.println("❌ Could not load FXML: " + fxmlName);
            e.printStackTrace();
        }
    }
}
