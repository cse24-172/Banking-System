package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.*;
import model.Account;
import database.Database;
import util.SceneSwitcher;
import javafx.stage.Stage;

import java.sql.*;

public class AllAccountsController {

    @FXML private TableView<Account> accountsTable;
    @FXML private TableColumn<Account, Integer> idCol;
    @FXML private TableColumn<Account, Integer> customerCol;
    @FXML private TableColumn<Account, String> typeCol;
    @FXML private TableColumn<Account, Double> balanceCol;

    @FXML private Button backBtn;   // 🔥 Added

    @FXML
    public void initialize() {

        // ✔ Correct property methods (matching Account model)
        idCol.setCellValueFactory(data -> data.getValue().accountIdProperty().asObject());
        customerCol.setCellValueFactory(data -> data.getValue().customerIdProperty().asObject());
        typeCol.setCellValueFactory(data -> data.getValue().typeProperty());
        balanceCol.setCellValueFactory(data -> data.getValue().balanceProperty().asObject());

        loadAccounts();

        // 🔥 Back button action
        backBtn.setOnAction(e -> goBack());
    }

    private void loadAccounts() {
        ObservableList<Account> list = FXCollections.observableArrayList();

        try (Connection conn = Database.connect();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM accounts")) {

            while (rs.next()) {
                list.add(new Account(
                        rs.getInt("accountId"),
                        rs.getInt("customerId"),
                        rs.getString("type"),
                        rs.getDouble("balance")
                ));
            }

            accountsTable.setItems(list);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 🔥 Back button logic
    private void goBack() {
        Stage stage = (Stage) backBtn.getScene().getWindow();
        SceneSwitcher.switchTo(stage, "admin_dashboard.fxml");
    }
}
