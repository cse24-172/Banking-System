package src;

public class SavingsAccount extends Account {
    private static final double MONTHLY_INTEREST_RATE = 0.0005; // 0.05%
    
    public SavingsAccount(String accountNumber, double balance, String branch, Customer customer) {
        super(accountNumber, balance, branch, customer, "Savings");
    }
    
    @Override
    public boolean withdraw(double amount) {
        System.out.println("✗ Withdrawals not allowed from Savings Account");
        return false;
    }
    
    @Override
    public void payMonthlyInterest() {
        double interest = balance * MONTHLY_INTEREST_RATE;
        balance += interest;
        System.out.println("✓ Savings Account interest: BWP " + String.format("%.2f", interest) + 
                          " paid to " + accountNumber + ". New balance: BWP " + String.format("%.2f", balance));
    }
    
    @Override
    public boolean canOpenAccount() {
        // Savings account can be opened by anyone with any amount
        if (balance >= 0) {
            System.out.println("✓ Savings account opening requirements met");
            return true;
        }
        System.out.println("✗ Invalid balance for savings account");
        return false;
    }
}
