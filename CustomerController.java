package src.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import src.Main;
import src.model.Customer;

public class CustomerController {

    @FXML private TextField firstNameField;
    @FXML private TextField surnameField;
    @FXML private TextField addressField;
    @FXML private TextField idNumberField;
    @FXML private CheckBox employedCheckBox;
    @FXML private GridPane employmentGrid;
    @FXML private TextField employerField;
    @FXML private TextField employerAddressField;
    @FXML private Label messageLabel;

    @FXML
    private void handleEmploymentToggle() {
        employmentGrid.setVisible(employedCheckBox.isSelected());
    }

    @FXML
    private void handleRegister() {
        // Validate input
        if (firstNameField.getText().trim().isEmpty() ||
            surnameField.getText().trim().isEmpty() ||
            addressField.getText().trim().isEmpty() ||
            idNumberField.getText().trim().isEmpty()) {
            
            messageLabel.setText("Please fill in all required fields");
            return;
        }

        try {
            Customer customer;
            String customerId = generateCustomerId();
            
            if (employedCheckBox.isSelected() && 
                !employerField.getText().trim().isEmpty() &&
                !employerAddressField.getText().trim().isEmpty()) {
                
                customer = new Customer(
                    customerId,
                    firstNameField.getText().trim(),
                    surnameField.getText().trim(),
                    addressField.getText().trim(),
                    idNumberField.getText().trim(),
                    employerField.getText().trim(),
                    employerAddressField.getText().trim()
                );
            } else {
                customer = new Customer(
                    customerId,
                    firstNameField.getText().trim(),
                    surnameField.getText().trim(),
                    addressField.getText().trim(),
                    idNumberField.getText().trim()
                );
            }

            // Add customer to bank
            Main.getBank().addCustomer(customer);
            
            showAlert("Success", 
                "Registration Successful!\n" +
                "Your Customer ID is: " + customerId + "\n" +
                "Please use this ID to login.");
            
            handleBackToLogin();
            
        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Registration failed: " + e.getMessage());
        }
    }

    @FXML
    private void handleBackToLogin() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/src/view/login.fxml"));
            Stage stage = (Stage) firstNameField.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Banking System - CSE202");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String generateCustomerId() {
        return "C" + String.format("%03d", Main.getBank().getAllCustomers().size() + 1);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}