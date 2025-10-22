public class InvestmentAccount extends Account {
    private static final double MONTHLY_INTEREST_RATE = 0.05; // 5%
    private static final double MINIMUM_OPENING_BALANCE = 500.00;
    
    public InvestmentAccount(String accountNumber, double initialBalance, String branch, Customer customer) {
        super(accountNumber, initialBalance, branch, customer, "Investment");
    }
    
    @Override
    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println("Withdrawn: BWP " + amount + " from Investment Account. New balance: BWP " + balance);
            return true;
        } else if (amount > balance) {
            System.out.println("Insufficient funds in Investment Account");
            return false;
        } else {
            System.out.println("Withdrawal amount must be positive");
            return false;
        }
    }
    
    @Override
    public void payMonthlyInterest() {
        double interest = balance * MONTHLY_INTEREST_RATE;
        balance += interest;
        System.out.println("Monthly interest of BWP " + String.format("%.2f", interest) + " paid to Investment Account. New balance: BWP " + String.format("%.2f", balance));
    }
    
    @Override
    public boolean canOpenAccount() {
        if (balance >= MINIMUM_OPENING_BALANCE) {
            return true;
        } else {
            System.out.println("Investment account requires minimum BWP " + MINIMUM_OPENING_BALANCE + " to open");
            return false;
        }
    }
}