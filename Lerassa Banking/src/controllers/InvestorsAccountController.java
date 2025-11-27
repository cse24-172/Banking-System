package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import util.SceneSwitcher;
import database.Database;
import session.Session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class InvestorsAccountController {

    @FXML private Label balanceLabel;
    @FXML private Label returnsLabel;
    @FXML private Button backBtn;

    @FXML
    public void initialize() {
        loadInvestmentDetails();

        backBtn.setOnAction(e -> {
            Stage stage = (Stage) backBtn.getScene().getWindow();
            SceneSwitcher.switchTo(stage, "customer_dashboard.fxml");
        });
    }

    private void loadInvestmentDetails() {

        String sql = "SELECT balance FROM accounts WHERE customer_id = ? AND type = 'Investment'";

        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, Session.customerId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                double balance = rs.getDouble("balance");
                balanceLabel.setText("P" + String.format("%.2f", balance));

                double estimatedReturns = balance * 0.08; // 8% example
                returnsLabel.setText("Est. Annual Returns: P" + String.format("%.2f", estimatedReturns));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
