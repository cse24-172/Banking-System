package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import util.SceneSwitcher;
import database.Database;

import java.sql.*;

public class OpenAccountController {

    @FXML private Button backBtn;

    @FXML private ChoiceBox<String> titleBox;
    @FXML private TextField fullNameField;
    @FXML private TextField surnameField;
    @FXML private TextField idField;
    @FXML private DatePicker dobPicker;
    @FXML private TextField phoneField;
    @FXML private ChoiceBox<String> countryBox;
    @FXML private TextField emailField;
    @FXML private TextField addressField;

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    @FXML private ChoiceBox<String> accountTypeBox;
    @FXML private TextField initialDepositField;
    @FXML private TextField employerField;

    @FXML private Button createBtn;
    @FXML private Label statusLabel;


    @FXML
    public void initialize() {

        titleBox.getItems().addAll("Mr", "Ms", "Mrs", "Miss");

        countryBox.getItems().addAll(
                "Botswana (+267)", "South Africa (+27)",
                "USA (+1)", "UK (+44)"
        );

        accountTypeBox.getItems().addAll("Savings", "Investment", "Cheque");

        employerField.setVisible(false);
        accountTypeBox.setOnAction(e ->
                employerField.setVisible("Cheque".equals(accountTypeBox.getValue()))
        );

        backBtn.setOnAction(e -> goBack());
        createBtn.setOnAction(e -> createCustomerAndAccount());
    }



    private boolean validateInputs() {

        if (titleBox.getValue() == null ||
            fullNameField.getText().isEmpty() ||
            surnameField.getText().isEmpty() ||
            idField.getText().isEmpty() ||
            dobPicker.getValue() == null ||
            phoneField.getText().isEmpty() ||
            countryBox.getValue() == null ||
            emailField.getText().isEmpty() ||
            addressField.getText().isEmpty() ||
            usernameField.getText().isEmpty() ||
            passwordField.getText().isEmpty()) {

            statusLabel.setText("Please fill all fields.");
            showAlert("Missing Information", "Please fill in ALL required fields.");
            return false;
        }

        if (!idField.getText().matches("\\d{11}")) {
            statusLabel.setText("ID must be 11 digits.");
            showAlert("Invalid ID Number", "Your ID number must contain exactly 11 digits.");
            return false;
        }

        String phone = phoneField.getText();

        if (countryBox.getValue().contains("Botswana") && !phone.matches("\\d{8}")) {
            showAlert("Invalid Phone Number", "Botswana numbers must be 8 digits.");
            return false;
        }

        if (countryBox.getValue().contains("South Africa") && !phone.matches("\\d{9}")) {
            showAlert("Invalid Phone Number", "South African numbers must be 9 digits.");
            return false;
        }

        if (countryBox.getValue().contains("USA") && !phone.matches("\\d{10}")) {
            showAlert("Invalid Phone Number", "US numbers must be 10 digits.");
            return false;
        }

        if (countryBox.getValue().contains("UK") && !phone.matches("\\d{10}")) {
            showAlert("Invalid Phone Number", "UK numbers must be 10 digits.");
            return false;
        }

        return true;
    }



    private void createCustomerAndAccount() {

        if (!validateInputs()) return;

        String type = accountTypeBox.getValue();

        if (type == null || initialDepositField.getText().isEmpty()) {
            showAlert("Incomplete Account Info", "Please fill in account type and initial deposit.");
            return;
        }

        double deposit = Double.parseDouble(initialDepositField.getText());

        if (type.equals("Investment") && deposit < 5000) {
            showAlert("Minimum Deposit Error", "Investment accounts require at least 5000.");
            return;
        }

        if (type.equals("Cheque") && employerField.getText().isEmpty()) {
            showAlert("Employer Required", "Cheque accounts must include the employer name.");
            return;
        }

        try {
            Connection conn = Database.connect();

            PreparedStatement ps1 = conn.prepareStatement(
                    "INSERT INTO customers (title, fullName, surname, idNumber, dob, phone, country, email, address, username, password) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS
            );

            ps1.setString(1, titleBox.getValue());
            ps1.setString(2, fullNameField.getText());
            ps1.setString(3, surnameField.getText());
            ps1.setString(4, idField.getText());
            ps1.setString(5, dobPicker.getValue().toString());
            ps1.setString(6, phoneField.getText());
            ps1.setString(7, countryBox.getValue());
            ps1.setString(8, emailField.getText());
            ps1.setString(9, addressField.getText());
            ps1.setString(10, usernameField.getText());
            ps1.setString(11, passwordField.getText());

            ps1.executeUpdate();

            ResultSet rs = ps1.getGeneratedKeys();
            rs.next();
            int customerId = rs.getInt(1);

            PreparedStatement ps2 = conn.prepareStatement(
                    "INSERT INTO accounts (customer_id, account_type, balance) VALUES (?, ?, ?)"
            );

            ps2.setInt(1, customerId);
            ps2.setString(2, type);
            ps2.setDouble(3, deposit);

            ps2.executeUpdate();

            conn.close();

            statusLabel.setStyle("-fx-text-fill: green;");
            statusLabel.setText("Customer and Account created successfully!");
            showAlert("Success", "Customer and account created successfully!");

            clearFields();   // ✅ NEW — CLEAR FORM

        } catch (Exception ex) {

            if (ex.getMessage().contains("UNIQUE constraint failed: customers.username")) {
                showAlert("Username Already Exists",
                        "The username '" + usernameField.getText() + "' is already taken. Please choose another.");
                statusLabel.setText("Username already exists.");
            } else {
                showAlert("Database Error", ex.getMessage());
                statusLabel.setText("Database error.");
            }
        }
    }


    // ✅ NEW — CLEAR ALL FIELDS AFTER SUCCESS
    private void clearFields() {
        titleBox.setValue(null);
        fullNameField.clear();
        surnameField.clear();
        idField.clear();
        dobPicker.setValue(null);
        phoneField.clear();
        countryBox.setValue(null);
        emailField.clear();
        addressField.clear();
        usernameField.clear();
        passwordField.clear();
        accountTypeBox.setValue(null);
        initialDepositField.clear();
        employerField.clear();
        employerField.setVisible(false);
    }



    private void goBack() {
        SceneSwitcher.switchTo(
                (Stage) backBtn.getScene().getWindow(),
                "admin_dashboard.fxml"
        );
    }



    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
