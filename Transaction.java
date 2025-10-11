import java.util.Date;

/**
 * Represents a financial transaction in the banking system
 */
public class Transaction {
    private String transactionId;
    private String accountNumber;
    private String type; // DEPOSIT, WITHDRAWAL, INTEREST, TRANSFER
    private double amount;
    private Date timestamp;
    private String description;
    private double balanceAfterTransaction;
    
    public Transaction(String transactionId, String accountNumber, String type, 
                      double amount, String description, double balanceAfterTransaction) {
        this.transactionId = transactionId;
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.timestamp = new Date(); // Current date and time
        this.description = description;
        this.balanceAfterTransaction = balanceAfterTransaction;
    }
    
    // Getters
    public String getTransactionId() { return transactionId; }
    public String getAccountNumber() { return accountNumber; }
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public Date getTimestamp() { return timestamp; }
    public String getDescription() { return description; }
    public double getBalanceAfterTransaction() { return balanceAfterTransaction; }
    
    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId='" + transactionId + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", type='" + type + '\'' +
                ", amount=BWP " + amount +
                ", timestamp=" + timestamp +
                ", description='" + description + '\'' +
                ", balanceAfterTransaction=BWP " + balanceAfterTransaction +
                '}';
    }
    
    // Helper method to format transaction for display
    public String toFormattedString() {
        return String.format("%s | %-12s | BWP %10.2f | BWP %10.2f | %s", 
            timestamp, type, amount, balanceAfterTransaction, description);
    }
    
    // Static helper methods for creating transactions
    public static Transaction createDeposit(String accountNumber, double amount, double newBalance, String description) {
        String transactionId = "DEP" + System.currentTimeMillis();
        return new Transaction(transactionId, accountNumber, "DEPOSIT", amount, description, newBalance);
    }
    
    public static Transaction createWithdrawal(String accountNumber, double amount, double newBalance, String description) {
        String transactionId = "WDL" + System.currentTimeMillis();
        return new Transaction(transactionId, accountNumber, "WITHDRAWAL", amount, description, newBalance);
    }
    
    public static Transaction createInterest(String accountNumber, double amount, double newBalance) {
        String transactionId = "INT" + System.currentTimeMillis();
        return new Transaction(transactionId, accountNumber, "INTEREST", amount, "Monthly Interest Payment", newBalance);
    }
}