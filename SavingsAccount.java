public class SavingsAccount extends Account {
    private static final double MONTHLY_INTEREST_RATE = 0.0005; // 0.05%
    
    public SavingsAccount(String accountNumber, double initialBalance, String branch, Customer customer) {
        super(accountNumber, initialBalance, branch, customer, "Savings");
    }
    
    @Override
    public boolean withdraw(double amount) {
        System.out.println("ERROR: Withdrawals are not allowed from Savings Accounts");
        return false;
    }
    
    @Override
    public void payMonthlyInterest() {
        double interest = balance * MONTHLY_INTEREST_RATE;
        balance += interest;
        System.out.println("Monthly interest of BWP " + String.format("%.2f", interest) + " paid to Savings Account. New balance: BWP " + String.format("%.2f", balance));
    }
    
    @Override
    public boolean canOpenAccount() {
        return true;
    }
}
