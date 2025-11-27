package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import util.SceneSwitcher;
import database.Database;
import session.Session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DepositController {

    @FXML private ChoiceBox<String> accountBox;
    @FXML private TextField amountField;
    @FXML private Button depositBtn;
    @FXML private Button backBtn;
    @FXML private Label statusLabel;

    @FXML
    public void initialize() {
        loadAccounts();

        depositBtn.setOnAction(e -> handleDeposit());
        backBtn.setOnAction(e -> goBack());
    }

    /**
     * Load user's accounts into ChoiceBox
     */
    private void loadAccounts() {
        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT id, account_type FROM accounts WHERE customer_id = ?")) {

            ps.setInt(1, Session.customerId);
            ResultSet rs = ps.executeQuery();

            accountBox.getItems().clear();

            while (rs.next()) {
                String entry = rs.getInt("id") + " - " + rs.getString("account_type");
                accountBox.getItems().add(entry);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles depositing money
     */
    private void handleDeposit() {

        // Validate account
        if (accountBox.getValue() == null) {
            showError("Please select an account to deposit into.");
            return;
        }

        // Validate amount
        String input = amountField.getText().trim();
        if (input.isEmpty()) {
            showError("Please enter an amount.");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(input);
        } catch (Exception ex) {
            showError("Amount must be a valid number.");
            return;
        }

        if (amount <= 0) {
            showError("Deposit amount must be greater than 0.");
            return;
        }

        int accountId = Integer.parseInt(accountBox.getValue().split(" - ")[0]);

        try (Connection conn = Database.connect()) {

            // Get current balance
            PreparedStatement ps1 = conn.prepareStatement(
                    "SELECT balance FROM accounts WHERE id = ?");
            ps1.setInt(1, accountId);
            ResultSet rs = ps1.executeQuery();

            if (!rs.next()) {
                showError("Account not found.");
                return;
            }

            double newBalance = rs.getDouble("balance") + amount;

            // Update account balance
            PreparedStatement ps2 = conn.prepareStatement(
                    "UPDATE accounts SET balance = ? WHERE id = ?");
            ps2.setDouble(1, newBalance);
            ps2.setInt(2, accountId);
            ps2.executeUpdate();

            // Log transaction
            PreparedStatement log = conn.prepareStatement(
                    "INSERT INTO transactions(account_id, type, amount, date) " +
                            "VALUES (?, ?, ?, datetime('now'))");
            log.setInt(1, accountId);
            log.setString(2, "Deposit");
            log.setDouble(3, amount);
            log.executeUpdate();

            showSuccess("Deposit successful!\nNew Balance: P" + String.format("%.2f", newBalance));

            // Clear fields
            amountField.clear();
            statusLabel.setText("");

        } catch (Exception ex) {
            ex.printStackTrace();
            showError("Database error occurred.");
        }
    }

    /**
     * Navigation back to dashboard
     */
    private void goBack() {
        Stage stage = (Stage) backBtn.getScene().getWindow();
        SceneSwitcher.switchTo(stage, "customer_dashboard.fxml");
    }

    /**
     * SHOW ERROR ALERT
     */
    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Deposit Error");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();

        statusLabel.setText(msg);
        statusLabel.setStyle("-fx-text-fill: red;");
    }

    /**
     * SHOW SUCCESS ALERT
     */
    private void showSuccess(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Deposit Successful");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();

        statusLabel.setText("Deposit successful!");
        statusLabel.setStyle("-fx-text-fill: green;");
    }
}
