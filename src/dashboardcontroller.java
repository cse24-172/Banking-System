cat > src/controller/DashboardController.java << 'EOF'
package src.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import src.Main;

public class DashboardController {
    
    @FXML
    private void handleViewAccounts() {
        showAlert("Accounts", "View accounts feature coming soon!");
    }
    
    @FXML
    private void handlePayInterest() {
        Main.getBank().payMonthlyInterest();
        showAlert("Success", "Monthly interest paid to all accounts!");
    }
    
    @FXML
    private void handleLogout() {
        try {
            Parent loginView = FXMLLoader.load(getClass().getResource("/src/view/login.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) javafx.scene.Node).getScene().getWindow();
            stage.setScene(new Scene(loginView, 800, 600));
        } catch (Exception e) {
            showAlert("Error", "Logout failed");
        }
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
EOF