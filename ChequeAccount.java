public class ChequeAccount extends Account {
    
    public ChequeAccount(String accountNumber, double initialBalance, String branch, Customer customer) {
        super(accountNumber, initialBalance, branch, customer, "Cheque");
    }
    
    @Override
    public boolean withdraw(double amount) {
        // Assignment: "allows deposits and withdrawals of any amount"
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
        // Assignment doesn't mention interest for cheque accounts
        System.out.println("No interest paid on Cheque Accounts");
    }
    
    @Override
    public boolean canOpenAccount() {
        // Assignment: "can only be opened for any person who is working"
        if (customer.getEmploymentCompany() != null && !customer.getEmploymentCompany().trim().isEmpty()) {
            return true;
        } else {
            System.out.println("Cheque account can only be opened for employed individuals");
            return false;
        }
    }
}