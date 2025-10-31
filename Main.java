package src;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    
    private static Bank bank = new Bank();
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initialize sample data for GUI
        initializeSampleData();
        
        // Load the login screen for GUI
        Parent root = FXMLLoader.load(getClass().getResource("/src/view/login.fxml"));
        primaryStage.setTitle("Banking System - CSE202");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }
    
    private void initializeSampleData() {
        // Create the same sample data as your console version
        Customer employedCustomer = new Customer("C001", "John", "Doe", "Gaborone", "19901234567", "ABC Corporation", "Gaborone CBD");
        Customer unemployedCustomer = new Customer("C002", "Jane", "Smith", "Francistown", "19911234567");
        
        bank.addCustomer(employedCustomer);
        bank.addCustomer(unemployedCustomer);
        
        bank.openSavingsAccount("SAV001", 100.0, "Gaborone Main", employedCustomer);
        bank.openInvestmentAccount("INV001", 600.0, "Gaborone Main", employedCustomer);
        bank.openChequeAccount("CHQ001", 200.0, "Gaborone Main", employedCustomer);
        
        // Add some initial transactions
        bank.depositToAccount("SAV001", 500.0);
        bank.depositToAccount("INV001", 1000.0);
        bank.depositToAccount("CHQ001", 300.0);
        
        System.out.println("=== SAMPLE DATA INITIALIZED ===");
        System.out.println("Test Customer IDs: C001 (employed), C002 (unemployed)");
    }
    
    public static Bank getBank() {
        return bank;
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}