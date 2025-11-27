package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import session.Session;
import util.SceneSwitcher;
import database.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoanInquiryController {

    @FXML private Button personalLoanBtn;
    @FXML private Button businessLoanBtn;
    @FXML private Button studentLoanBtn;
    @FXML private Button backBtn;

    private String accountType = "Unknown";

    @FXML
    public void initialize() {

        loadCustomerAccountType();

        personalLoanBtn.setOnAction(e -> showEligibility("Personal Loan"));
        businessLoanBtn.setOnAction(e -> showEligibility("Business Loan"));
        studentLoanBtn.setOnAction(e -> showEligibility("Student Loan"));

        backBtn.setOnAction(e -> {
            Stage stage = (Stage) backBtn.getScene().getWindow();
            SceneSwitcher.switchTo(stage, "customer_dashboard.fxml");
        });
    }

    private void loadCustomerAccountType() {
        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT type FROM accounts WHERE customerId = ?")) {

            ps.setInt(1, Session.customerId);
            ResultSet rs = ps.executeQuery();

            String bestType = "Savings";  // lowest tier default

            while (rs.next()) {
                String type = rs.getString("type");

                // Priority: Investment > Cheque > Savings
                if ("Investment".equalsIgnoreCase(type)) {
                    bestType = "Investment";
                    break; // highest tier found
                }
                if ("Cheque".equalsIgnoreCase(type) && !"Investment".equals(bestType)) {
                    bestType = "Cheque";
                }
            }

            accountType = bestType;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showEligibility(String loanType) {

        String message;

        switch (accountType) {
            case "Investment" ->
                    message = """
                            Approved Fast Track!

                            ✔ Investment customers get PRIORITY approval
                            ✔ Reduced interest rate
                            ✔ Maximum loan limit increased
                            ✔ Faster processing time
                            """;

            case "Cheque" ->
                    message = """
                            Eligible with employer verification.

                            ✔ Requires salary confirmation
                            ✔ Normal interest rate
                            ✔ Processing: 24–48 hours
                            """;

            case "Savings" ->
                    message = """
                            Eligible with standard conditions.

                            ✔ Standard interest rate
                            ✔ Normal processing time (48–72 hours)
                            """;

            default ->
                    message = "Unable to determine account type. Please try again.";
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Loan Inquiry");
        alert.setHeaderText(loanType + " Eligibility");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
