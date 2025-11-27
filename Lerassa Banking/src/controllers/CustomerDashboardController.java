package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import session.Session;
import util.SceneSwitcher;
import database.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CustomerDashboardController {

    @FXML private Label welcomeLabel;
    @FXML private Label balanceLabel;
    @FXML private Label accountsLabel;

    @FXML private ImageView profileImage;
    @FXML private ImageView bankLogo;

    @FXML private VBox paymentsCard;
    @FXML private VBox accountsCard;
    @FXML private VBox transactionsCard;
    @FXML private VBox loanCard;
    @FXML private VBox settingsCard;

    @FXML private Button openAccountBtn;
    @FXML private Button depositBtn;
    @FXML private Button withdrawBtn;
    @FXML private Button logoutBtn;

    @FXML
    public void initialize() {

        loadCustomerName();     // ✅ FIXED (Loads from DB, not session)
        loadBalance();          // ✅ Updated after new accounts
        loadAccountCount();     // ✅ Updated after new accounts

        loadImages();

        // ----------------------------
        // Navigation Buttons
        // ----------------------------
        openAccountBtn.setOnAction(e ->
                SceneSwitcher.switchTo(e, "open_additional_account.fxml")   
        );

        depositBtn.setOnAction(e ->
                SceneSwitcher.switchTo(e, "deposit.fxml")
        );

        withdrawBtn.setOnAction(e ->
                SceneSwitcher.switchTo(e, "withdraw.fxml")
        );

        accountsCard.setOnMouseClicked(e ->
                SceneSwitcher.switchTo(e, "all_accounts.fxml")   // Optional accounts list screen
        );

        paymentsCard.setOnMouseClicked(e ->
                SceneSwitcher.switchTo(e, "payments.fxml")
        );

        transactionsCard.setOnMouseClicked(e ->
                SceneSwitcher.switchTo(e, "transactions.fxml")
        );

        loanCard.setOnMouseClicked(e ->
                SceneSwitcher.switchTo(e, "loan_inquiry.fxml")
        );

        settingsCard.setOnMouseClicked(e ->
                SceneSwitcher.switchTo(e, "settings.fxml")
        );

        logoutBtn.setOnAction(e ->
                SceneSwitcher.switchTo(e, "login.fxml")
        );
    }

    // ----------------------------------------------------
    // Load Customer Name from DB
    // ----------------------------------------------------
    private void loadCustomerName() {
        try (Connection conn = Database.connect()) {

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT fullName FROM customers WHERE id = ?"
            );
            ps.setInt(1, Session.customerId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                welcomeLabel.setText("Welcome, " + rs.getString("fullName"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ----------------------------------------------------
    // Load Total Balance (ALL ACCOUNTS)
    // ----------------------------------------------------
    private void loadBalance() {
        String sql = "SELECT SUM(balance) FROM accounts WHERE customer_id = ?";

        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, Session.customerId);
            ResultSet rs = ps.executeQuery();

            double balance = rs.getDouble(1);
            balanceLabel.setText("P" + String.format("%.2f", balance));

        } catch (Exception e) {
            System.out.println("DB Error: " + e.getMessage());
        }
    }

    // ----------------------------------------------------
    // Load Total Number of Accounts
    // ----------------------------------------------------
    private void loadAccountCount() {
        String sql = "SELECT COUNT(*) FROM accounts WHERE customer_id = ?";

        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, Session.customerId);
            ResultSet rs = ps.executeQuery();

            accountsLabel.setText(String.valueOf(rs.getInt(1)));

        } catch (Exception e) {
            System.out.println("DB Error: " + e.getMessage());
        }
    }

    // ----------------------------------------------------
    // Load Images Safely
    // ----------------------------------------------------
    private void loadImages() {
        try {
            if (profileImage != null)
                profileImage.setImage(new Image(getClass().getResourceAsStream("/img/profile_b.png")));

            if (bankLogo != null)
                bankLogo.setImage(new Image(getClass().getResourceAsStream("/img/bank_icon.jpg")));

        } catch (Exception e) {
            System.out.println("⚠ Image missing: " + e.getMessage());
        }
    }
}
