package src;

public class InvestmentAccount extends Account {
    private static final double MONTHLY_INTEREST_RATE = 0.05; // 5%
    private static final double MIN_OPENING_BALANCE = 500.0;
    
    public InvestmentAccount(String accountNumber, double balance, String branch, Customer customer) {
        super(accountNumber, balance, branch, customer, "Investment");
    }
    
    @Override
    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println("✓ Withdrawn: BWP " + amount + " from Investment Account. New balance: BWP " + balance);
            return true;
        } else {
            System.out.println("✗ Invalid withdrawal amount or insufficient funds");
            return false;
        }
    }
    
    @Override
    public void payMonthlyInterest() {
        double interest = balance * MONTHLY_INTEREST_RATE;
        balance += interest;
        System.out.println("✓ Investment Account interest: BWP " + String.format("%.2f", interest) + 
                          " paid to " + accountNumber + ". New balance: BWP " + String.format("%.2f", balance));
    }
    
    @Override
    public boolean canOpenAccount() {
        if (balance >= MIN_OPENING_BALANCE) {
            System.out.println("✓ Investment account opening requirements met");
            return true;
        } else {
            System.out.println("✗ Investment account requires minimum BWP " + MIN_OPENING_BALANCE + " to open");
            return false;
        }
    }
}