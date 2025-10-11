public abstract class Account {
    protected String accountNumber;
    protected double balance;
    protected String branch;
    protected Customer customer;
    protected String accountType;
    
    public Account(String accountNumber, double balance, String branch, Customer customer, String accountType) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.branch = branch;
        this.customer = customer;
        this.accountType = accountType;
    }
    
    // Common methods for all accounts
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposited: BWP " + amount + " to " + accountType + " Account. New balance: BWP " + balance);
        } else {
            System.out.println("Deposit amount must be positive");
        }
    }
    
    // Abstract methods for account-specific behavior
    public abstract boolean withdraw(double amount);
    public abstract void payMonthlyInterest();
    public abstract boolean canOpenAccount();
    
    // Getters
    public double getBalance() { return balance; }
    public String getAccountNumber() { return accountNumber; }
    public String getBranch() { return branch; }
    public Customer getCustomer() { return customer; }
    public String getAccountType() { return accountType; }
    
    @Override
    public String toString() {
        return accountType + " Account{" +
                "accountNumber='" + accountNumber + '\'' +
                ", balance=BWP " + balance +
                ", branch='" + branch + '\'' +
                ", customer=" + customer.getFirstName() + " " + customer.getSurname() +
                '}';
    }
}