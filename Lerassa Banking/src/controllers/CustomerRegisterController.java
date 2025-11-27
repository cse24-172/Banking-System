package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import util.SceneSwitcher;
import database.Database;
import session.Session;

import java.sql.*;

public class CustomerRegisterController {

    @FXML private TextField firstNameField;
    @FXML private TextField surnameField;
    @FXML private ChoiceBox<String> accountTypeBox;
    @FXML private TextField depositField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Button registerBtn;
    @FXML private Button backBtn;
    @FXML private Label statusLabel;

    @FXML
    public void initialize() {

        accountTypeBox.getItems().addAll("Savings", "Investment", "Cheque");

        backBtn.setOnAction(e -> SceneSwitcher.switchTo(e, "login.fxml"));
        registerBtn.setOnAction(e -> createAccount());
    }

    private void createAccount() {

        String first = firstNameField.getText().trim();
        String last = surnameField.getText().trim();
        String type = accountTypeBox.getValue();
        String depo = depositField.getText().trim();
        String pass = passwordField.getText();
        String confirm = confirmPasswordField.getText();

        // VALIDATION
        if (first.isEmpty() || last.isEmpty() || type == null || depo.isEmpty() || pass.isEmpty()) {
            showError("Please fill all fields.");
            return;
        }

        if (!pass.equals(confirm)) {
            showError("Passwords do not match.");
            return;
        }

        double deposit;
        try {
            deposit = Double.parseDouble(depo);
        } catch (Exception e) {
            showError("Initial deposit must be numeric.");
            return;
        }

        try (Connection conn = Database.connect()) {

            // -----------------------------
            // 1️⃣ INSERT CUSTOMER
            // -----------------------------
            PreparedStatement ps1 = conn.prepareStatement(
                    "INSERT INTO customers (fullName, surname, title) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps1.setString(1, first);
            ps1.setString(2, last);
            ps1.setString(3, "Mr/Ms");

            ps1.executeUpdate();

            ResultSet rs = ps1.getGeneratedKeys();
            rs.next();
            int customerId = rs.getInt(1);

            // -----------------------------
            // 2️⃣ CREATE ACCOUNT NUMBER
            // -----------------------------
            String accNum = "AC" + System.currentTimeMillis();

            // -----------------------------
            // 3️⃣ INSERT FIRST ACCOUNT
            // -----------------------------
            PreparedStatement ps2 = conn.prepareStatement(
                    "INSERT INTO accounts (customer_id, account_number, account_type, balance) VALUES (?, ?, ?, ?)"
            );

            ps2.setInt(1, customerId);
            ps2.setString(2, accNum);
            ps2.setString(3, type);
            ps2.setDouble(4, deposit);
            ps2.executeUpdate();

            // -----------------------------
            // 4️⃣ AUTO-LOGIN SESSION
            // -----------------------------
            Session.customerId = customerId;
            Session.username = first;

            // -----------------------------
            // 5️⃣ SUCCESS ALERT
            // -----------------------------
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Account created successfully!");
            alert.showAndWait();

            // -----------------------------
            // 6️⃣ REDIRECT TO DASHBOARD
            // -----------------------------
            Stage stage = (Stage) registerBtn.getScene().getWindow();
            SceneSwitcher.switchTo(stage, "customer_dashboard.fxml");

        } catch (Exception e) {
            e.printStackTrace();
            showError("Database error.");
        }
    }

    // -----------------------------
    // ERROR HANDLER
    // -----------------------------
    private void showError(String msg) {
        statusLabel.setText(msg);
        statusLabel.setStyle("-fx-text-fill: red;");
    }
}
