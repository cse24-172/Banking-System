import java.util.ArrayList;
import java.util.List;

public class Bank {
    private List<Customer> customers;
    private List<Account> accounts;
    
    public Bank() {
        this.customers = new ArrayList<>();
        this.accounts = new ArrayList<>();
    }
    
    // Customer management
    public boolean addCustomer(Customer customer) {
        if (findCustomerById(customer.getCustomerId()) == null) {
            customers.add(customer);
            System.out.println("Customer registered: " + customer.getFirstName() + " " + customer.getSurname());
            return true;
        }
        System.out.println("Customer ID already exists");
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
    
    // Account management with validation
    public boolean openSavingsAccount(String accountNumber, double initialBalance, String branch, Customer customer) {
        SavingsAccount account = new SavingsAccount(accountNumber, initialBalance, branch, customer);
        if (account.canOpenAccount()) {
            accounts.add(account);
            System.out.println("Savings Account opened successfully");
            return true;
        }
        return false;
    }
    
    public boolean openInvestmentAccount(String accountNumber, double initialBalance, String branch, Customer customer) {
        InvestmentAccount account = new InvestmentAccount(accountNumber, initialBalance, branch, customer);
        if (account.canOpenAccount()) {
            accounts.add(account);
            System.out.println("Investment Account opened successfully");
            return true;
        }
        return false;
    }
    
    public boolean openChequeAccount(String accountNumber, double initialBalance, String branch, Customer customer) {
        ChequeAccount account = new ChequeAccount(accountNumber, initialBalance, branch, customer);
        if (account.canOpenAccount()) {
            accounts.add(account);
            System.out.println("Cheque Account opened successfully");
            return true;
        }
        return false;
    }
    
    // Get customer accounts
    public List<Account> getCustomerAccounts(String customerId) {
        List<Account> customerAccounts = new ArrayList<>();
        for (Account account : accounts) {
            if (account.getCustomer().getCustomerId().equals(customerId)) {
                customerAccounts.add(account);
            }
        }
        return customerAccounts;
    }
    
    public Account findAccountByNumber(String accountNumber) {
        for (Account account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        return null;
    }
    
    // Transaction methods
    public boolean depositToAccount(String accountNumber, double amount) {
        Account account = findAccountByNumber(accountNumber);
        if (account != null) {
            account.deposit(amount);
            return true;
        }
        System.out.println("Account not found: " + accountNumber);
        return false;
    }
    
    public boolean withdrawFromAccount(String accountNumber, double amount) {
        Account account = findAccountByNumber(accountNumber);
        if (account != null) {
            return account.withdraw(amount);
        }
        System.out.println("Account not found: " + accountNumber);
        return false;
    }
    
    // Monthly interest payment
    public void payMonthlyInterest() {
        System.out.println("\n=== PAYING MONTHLY INTEREST ===");
        for (Account account : accounts) {
            account.payMonthlyInterest();
        }
    }
    
    // Display methods
    public void displayAllCustomers() {
        System.out.println("\n=== ALL CUSTOMERS ===");
        for (Customer customer : customers) {
            System.out.println(customer);
        }
    }
    
    public void displayAllAccounts() {
        System.out.println("\n=== ALL ACCOUNTS ===");
        for (Account account : accounts) {
            System.out.println(account);
        }
    }
    
    public void displayCustomerAccounts(String customerId) {
        List<Account> customerAccounts = getCustomerAccounts(customerId);
        System.out.println("\n=== ACCOUNTS FOR CUSTOMER: " + customerId + " ===");
        for (Account account : customerAccounts) {
            System.out.println(account);
        }
    }
    
    // Getters
    public List<Customer> getCustomers() { return new ArrayList<>(customers); }
    public List<Account> getAccounts() { return new ArrayList<>(accounts); }
}