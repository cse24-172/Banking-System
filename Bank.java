import java.util.ArrayList;
import java.util.List;

public class Bank {
    private List<Customer> customers;
    private List<Account> accounts;
    
    public Bank() {
        this.customers = new ArrayList<>();
        this.accounts = new ArrayList<>();
    }
    
    public boolean addCustomer(Customer customer) {
        if (findCustomerById(customer.getCustomerId()) == null) {
            customers.add(customer);
            System.out.println("✓ Customer registered: " + customer.getFirstName() + " " + customer.getSurname());
            return true;
        }
        System.out.println("✗ Customer ID already exists: " + customer.getCustomerId());
        return false;
    }
    
    public Customer findCustomerById(String customerId) {
        for (Customer customer : customers) {
            if (customer.getCustomerId().equals(customerId)) {
                return customer;
            }
        }
        return null;
    }
    
    public boolean openSavingsAccount(String accountNumber, double initialBalance, String branch, Customer customer) {
        if (findAccountByNumber(accountNumber) != null) {
            System.out.println("✗ Account number already exists: " + accountNumber);
            return false;
        }
        
        SavingsAccount account = new SavingsAccount(accountNumber, initialBalance, branch, customer);
        if (account.canOpenAccount()) {
            accounts.add(account);
            System.out.println("✓ Savings Account opened: " + accountNumber + " for " + customer.getFirstName());
            return true;
        }
        return false;
    }
    
    public boolean openInvestmentAccount(String accountNumber, double initialBalance, String branch, Customer customer) {
        if (findAccountByNumber(accountNumber) != null) {
            System.out.println("✗ Account number already exists: " + accountNumber);
            return false;
        }
        
        InvestmentAccount account = new InvestmentAccount(accountNumber, initialBalance, branch, customer);
        if (account.canOpenAccount()) {
            accounts.add(account);
            System.out.println("✓ Investment Account opened: " + accountNumber + " for " + customer.getFirstName());
            return true;
        }
        return false;
    }
    
    public boolean openChequeAccount(String accountNumber, double initialBalance, String branch, Customer customer) {
        if (findAccountByNumber(accountNumber) != null) {
            System.out.println("✗ Account number already exists: " + accountNumber);
            return false;
        }
        
        ChequeAccount account = new ChequeAccount(accountNumber, initialBalance, branch, customer);
        if (account.canOpenAccount()) {
            accounts.add(account);
            System.out.println("✓ Cheque Account opened: " + accountNumber + " for " + customer.getFirstName());
            return true;
        }
        return false;
    }
    
    public Account findAccountByNumber(String accountNumber) {
        for (Account account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        return null;
    }
    
    public boolean depositToAccount(String accountNumber, double amount) {
        Account account = findAccountByNumber(accountNumber);
        if (account != null) {
            account.deposit(amount);
            return true;
        }
        System.out.println("✗ Account not found: " + accountNumber);
        return false;
    }
    
    public boolean withdrawFromAccount(String accountNumber, double amount) {
        Account account = findAccountByNumber(accountNumber);
        if (account != null) {
            return account.withdraw(amount);
        }
        System.out.println("✗ Account not found: " + accountNumber);
        return false;
    }
    
    public void payMonthlyInterest() {
        System.out.println("=== PROCESSING MONTHLY INTEREST ===");
        for (Account account : accounts) {
            account.payMonthlyInterest();
        }
    }
    
    public void displayAllCustomers() {
        System.out.println("\n=== REGISTERED CUSTOMERS ===");
        if (customers.isEmpty()) {
            System.out.println("No customers registered.");
        } else {
            for (Customer customer : customers) {
                System.out.println(customer);
            }
        }
    }
    
    public void displayAllAccounts() {
        System.out.println("\n=== ALL ACCOUNTS ===");
        if (accounts.isEmpty()) {
            System.out.println("No accounts opened.");
        } else {
            for (Account account : accounts) {
                System.out.println(account);
            }
        }
    }
}