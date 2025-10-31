package src;

public class ChequeAccount extends Account {
    
    public ChequeAccount(String accountNumber, double balance, String branch, Customer customer) {
        super(accountNumber, balance, branch, customer, "Cheque");
    }
    
    @Override
    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println("✓ Withdrawn: BWP " + amount + " from Cheque Account. New balance: BWP " + balance);
            return true;
        } else {
            System.out.println("✗ Invalid withdrawal amount or insufficient funds");
            return false;
        }
    }
    
    @Override
    public void payMonthlyInterest() {
        // Cheque accounts don't pay interest
        System.out.println("✗ No interest paid on Cheque Account: " + accountNumber);
    }
    
    @Override
    public boolean canOpenAccount() {
        // Cheque account can only be opened by employed customers
        if (customer.isEmployed()) {
            System.out.println("✓ Cheque account opening requirements met");
            return true;
        } else {
            System.out.println("✗ Cheque account can only be opened by employed customers");
            return false;
        }
    }
}