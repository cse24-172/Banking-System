package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import util.SceneSwitcher;

public class AdminDashboardController {

    @FXML private Button createCustomerBtn;
    @FXML private Button allCustomersBtn;
    @FXML private Button allAccountsBtn;
    @FXML private Button searchCustomerBtn;
    @FXML private Button transactionsBtn;
    @FXML private Button reportsBtn;
    @FXML private Button logoutBtn;

    @FXML
    public void initialize() {

        createCustomerBtn.setOnAction(e -> switchTo("open_account.fxml"));
        allCustomersBtn.setOnAction(e -> switchTo("all_customers.fxml"));
        allAccountsBtn.setOnAction(e -> switchTo("all_accounts.fxml"));
        searchCustomerBtn.setOnAction(e -> switchTo("search_customer.fxml"));
        transactionsBtn.setOnAction(e -> switchTo("transactions.fxml"));
        reportsBtn.setOnAction(e -> switchTo("reports.fxml"));

        logoutBtn.setOnAction(e -> switchTo("login.fxml"));
    }

    private void switchTo(String fxml) {
        Stage stage = (Stage) createCustomerBtn.getScene().getWindow();
        SceneSwitcher.switchTo(stage, fxml);
    }
}
