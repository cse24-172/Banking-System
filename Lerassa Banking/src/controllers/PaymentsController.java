package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import util.SceneSwitcher;
import database.Database;
import session.Session;

import java.sql.*;
import java.util.Optional;

public class PaymentsController {

    @FXML private Button backBtn;

    // Transfer UI
    @FXML private ChoiceBox<String> fromAccountBox;
    @FXML private ChoiceBox<String> toAccountBox;
    @FXML private TextField transferAmountField;
    @FXML private Button transferBtn;
    @FXML private Label transferStatus;

    // Bill Payment UI
    @FXML private ChoiceBox<String> billAccountBox;
    @FXML private ChoiceBox<String> billTypeBox;
    @FXML private TextField billAccountField;
    @FXML private TextField billAmountField;
    @FXML private Button payBtn;
    @FXML private Label paymentStatus;

    @FXML
    public void initialize() {
        loadAccounts();
        loadBillTypes();
        transferBtn.setOnAction(e -> transfer());
        payBtn.setOnAction(e -> payBill());

        backBtn.setOnAction(e -> {
            Stage stage = (Stage) backBtn.getScene().getWindow();
            SceneSwitcher.switchTo(stage, "customer_dashboard.fxml");
        });
    }

    // ---------------------------------------------------
    // LOAD ACCOUNTS
    // ---------------------------------------------------
    private void loadAccounts() {
        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT id, account_type FROM accounts WHERE customer_id = ?")) {

            ps.setInt(1, Session.customerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String acc = rs.getInt("id") + " - " + rs.getString("account_type");

                fromAccountBox.getItems().add(acc);
                toAccountBox.getItems().add(acc);
                billAccountBox.getItems().add(acc);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadBillTypes() {
        billTypeBox.getItems().addAll(
                "Electricity (BPC)",
                "Water Utilities",
                "Mobile Airtime",
                "Internet Service",
                "TV Subscription (DSTV)"
        );
    }

    // ---------------------------------------------------
    // CONFIRMATION DIALOG
    // ---------------------------------------------------
    private boolean confirm(String msg) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm");
        alert.setContentText(msg);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    // ---------------------------------------------------
    // CLEAR METHOD (YOU REQUESTED THIS)
    // ---------------------------------------------------
    private void clear() {
        transferAmountField.clear();
        billAccountField.clear();
        billAmountField.clear();

        fromAccountBox.setValue(null);
        toAccountBox.setValue(null);
        billAccountBox.setValue(null);
        billTypeBox.setValue(null);

        transferStatus.setText("");
        paymentStatus.setText("");
    }

    // ---------------------------------------------------
    // ACCOUNT BALANCE HELPER
    // ---------------------------------------------------
    private double getBalance(Connection conn, int accountId) throws Exception {
        PreparedStatement ps = conn.prepareStatement(
                "SELECT balance FROM accounts WHERE id=?");
        ps.setInt(1, accountId);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) return rs.getDouble("balance");
        throw new Exception("Account not found!");
    }

    private void updateBalance(Connection conn, int accountId, double newBalance) throws Exception {
        PreparedStatement ps = conn.prepareStatement(
                "UPDATE accounts SET balance=? WHERE id=?");
        ps.setDouble(1, newBalance);
        ps.setInt(2, accountId);
        ps.executeUpdate();
    }

    private void logTransaction(Connection conn, int accountId, String type, double amount) throws Exception {
        PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO transactions(account_id, type, amount, date) VALUES (?, ?, ?, datetime('now'))");
        ps.setInt(1, accountId);
        ps.setString(2, type);
        ps.setDouble(3, amount);
        ps.executeUpdate();
    }

    // ---------------------------------------------------
    // TRANSFER
    // ---------------------------------------------------
    private void transfer() {
        try {

            if (fromAccountBox.getValue() == null || toAccountBox.getValue() == null) {
                transferStatus.setText("Select both accounts");
                return;
            }

            if (fromAccountBox.getValue().equals(toAccountBox.getValue())) {
                transferStatus.setText("Cannot transfer to same account");
                return;
            }

            if (transferAmountField.getText().isEmpty()) {
                transferStatus.setText("Enter amount");
                return;
            }

            double amt = Double.parseDouble(transferAmountField.getText());
            int from = Integer.parseInt(fromAccountBox.getValue().split(" - ")[0]);
            int to = Integer.parseInt(toAccountBox.getValue().split(" - ")[0]);

            if (!confirm("Transfer P" + amt + " ?"))
                return;

            try (Connection conn = Database.connect()) {

                double bal = getBalance(conn, from);

                if (bal < amt) {
                    transferStatus.setText("Insufficient funds");
                    return;
                }

                updateBalance(conn, from, bal - amt);
                updateBalance(conn, to, getBalance(conn, to) + amt);

                logTransaction(conn, from, "Transfer", amt);

                transferStatus.setStyle("-fx-text-fill: green;");
                transferStatus.setText("Transfer successful!");

                clear();
            }

        } catch (Exception e) {
            transferStatus.setText("Error");
            e.printStackTrace();
        }
    }

    // ---------------------------------------------------
    // BILL PAYMENT
    // ---------------------------------------------------
    private void payBill() {
        try {
            if (billTypeBox.getValue() == null ||
                billAccountBox.getValue() == null ||
                billAccountField.getText().isEmpty() ||
                billAmountField.getText().isEmpty()) {

                paymentStatus.setText("Complete all fields");
                return;
            }

            double amt = Double.parseDouble(billAmountField.getText());
            int accountId = Integer.parseInt(billAccountBox.getValue().split(" - ")[0]);

            if (!confirm("Pay bill of P" + amt + " ?"))
                return;

            try (Connection conn = Database.connect()) {

                double bal = getBalance(conn, accountId);

                if (bal < amt) {
                    paymentStatus.setText("Insufficient funds");
                    return;
                }

                updateBalance(conn, accountId, bal - amt);

                logTransaction(conn, accountId, billTypeBox.getValue(), amt);

                paymentStatus.setStyle("-fx-text-fill: green;");
                paymentStatus.setText("Payment successful!");

                clear();
            }

        } catch (Exception e) {
            paymentStatus.setText("Error");
            e.printStackTrace();
        }
    }
}
