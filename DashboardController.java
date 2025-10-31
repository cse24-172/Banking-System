package src.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import src.Main;
import src.model.Customer;
import src.model.Account;

public class DashboardController {

    @FXML private Label welcomeLabel;
    @FXML private TableView<Account> accountsTable;
    @FXML private ComboBox<String> accountSelector;
    @FXML private TableView<?> transactionsTable;
    @FXML private Label custIdLabel;
    @FXML private Label nameLabel;
    @FXML private Label addressLabel;
    @FXML private Label idNumberLabel;
    @FXML private Label employmentLabel;

    private Customer customer;

    public void setCustomer(Customer customer) {
        this.customer = customer;
        initializeDashboard();
    }

    private void initializeDashboard() {
        welcomeLabel.setText("Welcome, " + customer.getFirstName() + " " + customer.getSurname());
        updateCustomerInfo();
        loadAccounts();
    }

    private void updateCustomerInfo() {
        custIdLabel.setText(customer.getCustomerId());
        nameLabel.setText(customer.getFirstName() + " " + customer.getSurname());
        addressLabel.setText(customer.getAddress());
        idNumberLabel.setText(customer.getIdNumber());
        
        if (customer.getEmployer() != null && !customer.getEmployer().isEmpty()) {
            employmentLabel.setText(customer.getEmployer() + ", " + customer.getEmployerAddress());
        } else {
            employmentLabel.setText("Not Employed");
        }
    }

    private void loadAccounts() {
        // Clear existing data
        accountsTable.getItems().clear();
        accountSelector.getItems().clear();
        
        // Load accounts for this customer
        var accounts = Main.getBank().getAccountsForCustomer(customer.getCustomerId());
        accountsTable.getItems().addAll(accounts);
        
        // Populate account selector
        for (Account account : accounts) {
            accountSelector.getItems().add(account.getAccountNumber() + " - " + account.getType());
        }
    }

    @FXML
    private void handleOpenAccount() {
        // Implementation for opening new account
        showAlert("Info", "Open Account feature will be implemented here");
    }

    @FXML
    private void handleRefresh() {
        loadAccounts();
    }

    @FXML
    private void handleShowTransactions() {
        // Implementation for showing transactions
        showAlert("Info", "Show Transactions feature will be implemented here");
    }

    @FXML
    private void handleDeposit() {
        // Implementation for deposit
        showAlert("Info", "Deposit feature will be implemented here");
    }

    @FXML
    private void handleWithdrawal() {
        // Implementation for withdrawal
        showAlert("Info", "Withdrawal feature will be implemented here");
    }

    @FXML
    private void handleLogout() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/src/view/login.fxml"));
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Banking System - CSE202");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}