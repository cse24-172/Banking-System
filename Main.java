package src;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import src.model.Customer;

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
    }
    
    public static Bank getBank() {
        return bank;
    }
    
    // KEEP YOUR ORIGINAL CONSOLE CODE AS A SEPARATE METHOD
    public static void runConsoleVersion() {
        Bank consoleBank = new Bank();
        
        System.out.println("=== BANKING SYSTEM (CONSOLE VERSION) ===");
        System.out.println("CSE202 - Object Oriented Analysis & Design\n");
        
        // Create customers
        Customer employedCustomer = new Customer("C001", "John", "Doe", "Gaborone", "19901234567", "ABC Corporation", "Gaborone CBD");
        Customer unemployedCustomer = new Customer("C002", "Jane", "Smith", "Francistown", "19911234567");
        
        // Add customers
        consoleBank.addCustomer(employedCustomer);
        consoleBank.addCustomer(unemployedCustomer);
        
        System.out.println("\n=== OPENING ACCOUNTS ===");
        
        // Open accounts
        consoleBank.openSavingsAccount("SAV001", 100.0, "Gaborone Main", employedCustomer);
        consoleBank.openInvestmentAccount("INV001", 600.0, "Gaborone Main", employedCustomer);
        consoleBank.openChequeAccount("CHQ001", 200.0, "Gaborone Main", employedCustomer);
        
        // These should fail
        consoleBank.openInvestmentAccount("INV002", 400.0, "Gaborone Main", unemployedCustomer);
        consoleBank.openChequeAccount("CHQ002", 200.0, "Gaborone Main", unemployedCustomer);
        
        System.out.println("\n=== TRANSACTIONS ===");
        
        // Deposits
        consoleBank.depositToAccount("SAV001", 500.0);
        consoleBank.depositToAccount("INV001", 1000.0);
        consoleBank.depositToAccount("CHQ001", 300.0);
        
        // Withdrawals
        consoleBank.withdrawFromAccount("SAV001", 100.0); // Should fail
        consoleBank.withdrawFromAccount("INV001", 200.0); // Should work
        consoleBank.withdrawFromAccount("CHQ001", 150.0); // Should work
        
        System.out.println("\n=== INTEREST PAYMENT ===");
        consoleBank.payMonthlyInterest();
        
        System.out.println("\n=== FINAL SUMMARY ===");
        consoleBank.displayAllCustomers();
        consoleBank.displayAllAccounts();
        
        System.out.println("\n=== OOP PRINCIPLES DEMONSTRATED ===");
        System.out.println("1. Inheritance: Account ← Savings/Investment/Cheque Accounts");
        System.out.println("2. Polymorphism: Bank handles all accounts through Account reference");
        System.out.println("3. Abstraction: Account has abstract methods");
        System.out.println("4. Encapsulation: Private fields with public methods");
    }
    
    public static void main(String[] args) {
        // Check if user wants console version or GUI version
        if (args.length > 0 && args[0].equals("console")) {
            // Run console version
            runConsoleVersion();
        } else {
            // Run GUI version (default)
            launch(args);
        }
    }
}