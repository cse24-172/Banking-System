package src.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import src.Main;
import src.model.Customer;

public class LoginController {

    @FXML
    private TextField customerIdField;

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    @FXML
    private Label messageLabel;

    @FXML
    private void handleLogin() {
        String customerId = customerIdField.getText().trim();
        
        if (customerId.isEmpty()) {
            showAlert("Error", "Please enter a Customer ID");
            return;
        }

        Customer customer = Main.getBank().findCustomer(customerId);
        if (customer != null) {
            try {
                // Load dashboard
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/view/dashboard.fxml"));
                Parent root = loader.load();
                
                // Pass customer to dashboard controller
                DashboardController controller = loader.getController();
                controller.setCustomer(customer);
                
                Stage stage = (Stage) loginButton.getScene().getWindow();
                stage.setScene(new Scene(root, 1000, 700));
                stage.setTitle("Bank Dashboard - " + customer.getFirstName() + " " + customer.getSurname());
                
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "Failed to load dashboard");
            }
        } else {
            messageLabel.setText("Customer ID not found. Please register.");
        }
    }

    @FXML
    private void handleRegister() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/src/view/customer_registration.fxml"));
            Stage stage = (Stage) registerButton.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Register New Customer");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load registration form");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}