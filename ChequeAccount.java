public class ChequeAccount extends Account {
    
    public ChequeAccount(String accountNumber, double initialBalance, String branch, Customer customer) {
        super(accountNumber, initialBalance, branch, customer, "Cheque");
    }
    
    @Override
    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println("Withdrawn: BWP " + amount + " from Cheque Account. New balance: BWP " + balance);
            return true;
        } else if (amount > balance) {
            System.out.println("Insufficient funds in Cheque Account");
            return false;
        } else {
            System.out.println("Withdrawal amount must be positive");
            return false;
        }
    }
    
    @Override
    public void payMonthlyInterest() {
        System.out.println("No interest paid on Cheque Accounts. Current balance: BWP " + balance);
    }
    
    @Override
    public boolean canOpenAccount() {
        if (customer.isEmployed()) {
            return true;
        } else {
            System.out.println("Cheque account can only be opened for employed individuals");
            return false;
        }
    }
}