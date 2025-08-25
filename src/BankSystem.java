// Interface for account operations
interface AccountOperations {
    void deposit(double amount);
    void withdraw(double amount);
    void calculateInterest();
}

// Abstract class Account
abstract class Account implements AccountOperations {
    protected String accountNumber;
    protected double balance;
    protected String branch;
    protected Customer customer;

    public Account(String accountNumber, String branch, Customer customer) {
        this.accountNumber = accountNumber;
        this.branch = branch;
        this.customer = customer;
        this.balance = 0.0;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    @Override
    public void deposit(double amount) {
        balance += amount;
        System.out.println(amount + " deposited. New balance: " + balance);
    }

    @Override
    public abstract void withdraw(double amount); // Different rules for each account
}

// Savings Account
class SavingsAccount extends Account {
    private static final double INTEREST_RATE = 0.0005; // 0.05% monthly

    public SavingsAccount(String accountNumber, String branch, Customer customer) {
        super(accountNumber, branch, customer);
    }

    @Override
    public void withdraw(double amount) {
        System.out.println("Withdrawals not allowed from Savings Account.");
    }

    @Override
    public void calculateInterest() {
        double interest = balance * INTEREST_RATE;
        balance += interest;
        System.out.println("Interest added: " + interest + " New balance: " + balance);
    }
}

// Investment Account
class InvestmentAccount extends Account {
    private static final double INTEREST_RATE = 0.05; // 5% monthly

    public InvestmentAccount(String accountNumber, String branch, Customer customer) {
        super(accountNumber, branch, customer);
        if (balance < 500) {
            balance = 500; // Initial deposit required
        }
    }

    @Override
    public void withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            System.out.println(amount + " withdrawn. New balance: " + balance);
        } else {
            System.out.println("Insufficient balance.");
        }
    }

    @Override
    public void calculateInterest() {
        double interest = balance * INTEREST_RATE;
        balance += interest;
        System.out.println("Interest added: " + interest + " New balance: " + balance);
    }
}

// Cheque Account
class ChequeAccount extends Account {
    private String employer;

    public ChequeAccount(String accountNumber, String branch, Customer customer, String employer) {
        super(accountNumber, branch, customer);
        this.employer = employer;
    }

    @Override
    public void withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            System.out.println(amount + " withdrawn. New balance: " + balance);
        } else {
            System.out.println("Insufficient balance.");
        }
    }

    @Override
    public void calculateInterest() {
        System.out.println("Cheque Account does not earn interest.");
    }
}

// Customer class
class Customer {
    private String firstname;
    private String surname;
    private String address;

    public Customer(String firstname, String surname, String address) {
        this.firstname = firstname;
        this.surname = surname;
        this.address = address;
    }

    public String getFullName() {
        return firstname + " " + surname;
    }
}

// Main class
public class BankSystem {
    public static void main(String[] args) {
        Customer c1 = new Customer("Laressa", "Motonto", "Gaborone");

        SavingsAccount sa = new SavingsAccount("SA001", "Main Branch", c1);
        InvestmentAccount ia = new InvestmentAccount("IA001", "Main Branch", c1);
        ChequeAccount ca = new ChequeAccount("CA001", "Main Branch", c1, "ABC Company");

        // Deposits
        sa.deposit(1000);
        ia.deposit(2000);
        ca.deposit(5000);

        // Interest Calculation
        sa.calculateInterest();
        ia.calculateInterest();
        ca.calculateInterest();

        // Withdrawals
        ia.withdraw(1000);
        ca.withdraw(2000);
        sa.withdraw(500); // Not allowed
    }
}
