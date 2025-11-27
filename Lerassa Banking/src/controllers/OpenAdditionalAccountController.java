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

public class OpenAdditionalAccountController {

    @FXML private ChoiceBox<String> accountTypeBox;
    @FXML private TextField depositField;
    @FXML private Button createBtn;
    @FXML private Button backBtn;
    @FXML private Label statusLabel;

    @FXML
    public void initialize() {
        accountTypeBox.getItems().addAll("Savings", "Investment", "Cheque");
        depositField.setPromptText("Initial Deposit (Optional)");

        createBtn.setOnAction(e -> createAccount());
        backBtn.setOnAction(e -> SceneSwitcher.switchTo(e, "customer_dashboard.fxml"));
    }

    private void createAccount() {

        String type = accountTypeBox.getValue();
        String depoText = depositField.getText().trim();

        if (type == null) {
            showError("Please select an account type.");
            return;
        }

        double deposit = 0;
        if (!depoText.isEmpty()) {
            try {
                deposit = Double.parseDouble(depoText);
                if (deposit < 0) {
                    showError("Deposit cannot be negative.");
                    return;
                }
            } catch (Exception e) {
                showError("Deposit must be numeric.");
                return;
            }
        }

        try (Connection conn = Database.connect()) {

            int mainAccId = -1;
            double mainBalance = 0;

            // ----------------------------------------------------
            // 1️⃣ Find MAIN ACCOUNT (Cheque first, then Savings)
            // ----------------------------------------------------
            PreparedStatement psMain = conn.prepareStatement(
                    "SELECT id, balance FROM accounts " +
                    "WHERE customer_id = ? AND account_type IN ('Cheque','Savings') " +
                    "ORDER BY account_type='Cheque' DESC LIMIT 1"
            );
            psMain.setInt(1, Session.customerId);

            ResultSet rsMain = psMain.executeQuery();

            if (rsMain.next()) {
                mainAccId = rsMain.getInt("id");
                mainBalance = rsMain.getDouble("balance");
            } else {
                showError("You need a Cheque or Savings account to fund deposits.");
                return;
            }

            // ----------------------------------------------------
            // 2️⃣ Make sure deposit is affordable
            // ----------------------------------------------------
            if (deposit > 0 && mainBalance < deposit) {
                showError("Insufficient funds in main account.");
                return;
            }

            // ----------------------------------------------------
            // 3️⃣ Deduct deposit from main account
            // ----------------------------------------------------
            if (deposit > 0) {
                double newBalance = mainBalance - deposit;

                PreparedStatement updateMain = conn.prepareStatement(
                        "UPDATE accounts SET balance = ? WHERE id = ?"
                );
                updateMain.setDouble(1, newBalance);
                updateMain.setInt(2, mainAccId);
                updateMain.executeUpdate();

                // Log transaction
                PreparedStatement log = conn.prepareStatement(
                    "INSERT INTO transactions (account_id, type, amount, date) VALUES (?, ?, ?, datetime('now'))"
                );
                log.setInt(1, mainAccId);
                log.setString(2, "Transfer to New Account");
                log.setDouble(3, deposit);
                log.executeUpdate();
            }

            // ----------------------------------------------------
            // 4️⃣ Create NEW ACCOUNT
            // ----------------------------------------------------
            String accNum = "AC" + System.currentTimeMillis();

            PreparedStatement createAcc = conn.prepareStatement(
                    "INSERT INTO accounts (customer_id, account_number, account_type, balance) " +
                    "VALUES (?, ?, ?, ?)"
            );
            createAcc.setInt(1, Session.customerId);
            createAcc.setString(2, accNum);
            createAcc.setString(3, type);
            createAcc.setDouble(4, deposit);
            createAcc.executeUpdate();

            // ----------------------------------------------------
            // 5️⃣ Done! Redirect user
            // ----------------------------------------------------
            showSuccess(type + " account created!");

            SceneSwitcher.switchTo((Stage) createBtn.getScene().getWindow(),
                    "customer_dashboard.fxml");

        } catch (Exception e) {
            e.printStackTrace();
            showError("Database error occurred.");
        }
    }

    private void showError(String msg) {
        statusLabel.setText(msg);
        statusLabel.setStyle("-fx-text-fill: red;");
    }

    private void showSuccess(String msg) {
        statusLabel.setText(msg);
        statusLabel.setStyle("-fx-text-fill: green;");
    }
}
