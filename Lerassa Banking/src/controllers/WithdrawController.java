package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import session.Session;
import util.SceneSwitcher;
import database.Database;

import java.sql.*;
import java.util.Random;

public class WithdrawController {

    @FXML private ChoiceBox<String> accountBox;
    @FXML private TextField amountField;
    @FXML private ChoiceBox<String> countryCodeBox;
    @FXML private TextField phoneField;
    @FXML private TextField pinField;

    @FXML private Button withdrawBtn;
    @FXML private Button backBtn;

    @FXML private Label statusLabel;

    @FXML
    public void initialize() {
        loadCountryCodes();
        loadAccounts();
        generatePin();

        withdrawBtn.setOnAction(e -> withdrawMoney());
        backBtn.setOnAction(e -> goBack());
    }

    // --------------------------------------------------------
    // LOAD COUNTRY CODES
    // --------------------------------------------------------
    private void loadCountryCodes() {
        countryCodeBox.getItems().addAll("+267", "+27", "+260", "+264");
        countryCodeBox.setValue("+267");
    }

    // --------------------------------------------------------
    // GENERATE RANDOM 4-DIGIT PIN
    // --------------------------------------------------------
    private void generatePin() {
        Random r = new Random();
        int pin = 1000 + r.nextInt(9000);
        pinField.setText(String.valueOf(pin));
    }

    // --------------------------------------------------------
    // LOAD USER ACCOUNTS
    // --------------------------------------------------------
    private void loadAccounts() {
        try (Connection conn = Database.connect()) {

            PreparedStatement ps = conn.prepareStatement(
                "SELECT account_number, account_type FROM accounts WHERE customer_id = ?"
            );
            ps.setInt(1, Session.customerId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String acc = rs.getString("account_number");
                String type = rs.getString("account_type");
                accountBox.getItems().add(acc + " (" + type + ")");
            }

            if (!accountBox.getItems().isEmpty()) {
                accountBox.setValue(accountBox.getItems().get(0));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("ERROR LOADING ACCOUNTS");
        }
    }

    // --------------------------------------------------------
    // WITHDRAW LOGIC
    // --------------------------------------------------------
    private void withdrawMoney() {

        if (accountBox.getValue() == null) {
            showError("Select an account first.");
            return;
        }

        String amountText = amountField.getText().trim();
        if (amountText.isEmpty()) {
            showError("Enter withdrawal amount.");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                showError("Amount must be greater than zero.");
                return;
            }
        } catch (NumberFormatException e) {
            showError("Invalid amount.");
            return;
        }

        // Parse account number and account type safely
        String selected = accountBox.getValue();
        String accountNumber = selected.split(" \\(")[0];  // safe split
        String accountType = selected.substring(
                selected.indexOf("(") + 1,
                selected.indexOf(")")
        );

        // Block savings account withdrawals
        if (accountType.equalsIgnoreCase("Savings")) {
            showError("Savings accounts cannot withdraw.");
            return;
        }

        try (Connection conn = Database.connect()) {

            // EXTRA SAFETY: confirm type from DB
            PreparedStatement psType = conn.prepareStatement(
                "SELECT account_type FROM accounts WHERE account_number = ?"
            );
            psType.setString(1, accountNumber);
            ResultSet typeRs = psType.executeQuery();

            if (typeRs.next()) {
                if (typeRs.getString("account_type").equalsIgnoreCase("Savings")) {
                    showError("Savings accounts cannot withdraw.");
                    return;
                }
            }

            // Get current balance
            PreparedStatement ps1 = conn.prepareStatement(
                "SELECT balance FROM accounts WHERE account_number = ?"
            );
            ps1.setString(1, accountNumber);

            ResultSet rs = ps1.executeQuery();
            if (!rs.next()) {
                showError("Account not found.");
                return;
            }

            double currentBalance = rs.getDouble("balance");

            if (currentBalance < amount) {
                showError("Insufficient funds.");
                return;
            }

            double newBalance = currentBalance - amount;

            // Update balance
            PreparedStatement ps2 = conn.prepareStatement(
                "UPDATE accounts SET balance = ? WHERE account_number = ?"
            );
            ps2.setDouble(1, newBalance);
            ps2.setString(2, accountNumber);
            ps2.executeUpdate();

            // SUCCESS MESSAGE
            showSuccess(
                "Withdrawal Successful!\n\n" +
                "Collect cash at any ATM.\n" +
                "Use PIN: " + pinField.getText()
            );

            amountField.clear();
            generatePin();

        } catch (Exception e) {
            e.printStackTrace();
            showError("Database error.");
        }
    }

    // --------------------------------------------------------
    // BACK BUTTON
    // --------------------------------------------------------
    private void goBack() {
        Stage stage = (Stage) backBtn.getScene().getWindow();
        SceneSwitcher.switchTo(stage, "customer_dashboard.fxml");
    }

    // --------------------------------------------------------
    // ALERT HELPERS
    // --------------------------------------------------------
    private void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
        statusLabel.setText(msg);
        statusLabel.setStyle("-fx-text-fill:red;");
    }

    private void showSuccess(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
        statusLabel.setText("Withdrawal Successful!");
        statusLabel.setStyle("-fx-text-fill:green;");
    }
}
