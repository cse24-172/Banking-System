package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import database.Database;
import model.TransactionRow;
import session.Session;
import util.SceneSwitcher;

import java.sql.*;

public class TransactionsController {

    @FXML private TableView<TransactionRow> transactionsTable;

    @FXML private TableColumn<TransactionRow, Integer> idCol;
    @FXML private TableColumn<TransactionRow, String> accCol;
    @FXML private TableColumn<TransactionRow, String> typeCol;
    @FXML private TableColumn<TransactionRow, Double> amountCol;
    @FXML private TableColumn<TransactionRow, String> dateCol;

    @FXML private Button backBtn;

    @FXML
    public void initialize() {

        // Bind columns
        idCol.setCellValueFactory(c -> c.getValue().idProperty().asObject());
        accCol.setCellValueFactory(c -> c.getValue().accountProperty());
        typeCol.setCellValueFactory(c -> c.getValue().typeProperty());
        amountCol.setCellValueFactory(c -> c.getValue().amountProperty().asObject());
        dateCol.setCellValueFactory(c -> c.getValue().dateProperty());

        loadTransactions();

        // Back button action
        backBtn.setOnAction(e -> {
            Stage stage = (Stage) backBtn.getScene().getWindow();
            SceneSwitcher.switchTo(stage, "customer_dashboard.fxml");
        });
    }

    private void loadTransactions() {

        String sql =
                """
                SELECT t.id, t.account_id, t.type, t.amount, t.date
                FROM transactions t
                JOIN accounts a ON t.account_id = a.id
                WHERE a.customer_id = ?
                ORDER BY t.date DESC
                """;

        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, Session.customerId);

            ResultSet rs = ps.executeQuery();
            transactionsTable.getItems().clear();

            while (rs.next()) {

                // Format: "123 - Savings"
                String accountLabel = rs.getInt("account_id") + "";

                transactionsTable.getItems().add(new TransactionRow(
                        rs.getInt("id"),
                        accountLabel,
                        rs.getString("type"),
                        rs.getDouble("amount"),
                        rs.getString("date")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
