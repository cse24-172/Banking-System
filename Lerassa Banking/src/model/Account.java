package model;

import javafx.beans.property.*;

public class Account {

    private IntegerProperty accountId;
    private IntegerProperty customerId;
    private StringProperty type;
    private DoubleProperty balance;

    public Account(int accountId, int customerId, String type, double balance) {
        this.accountId = new SimpleIntegerProperty(accountId);
        this.customerId = new SimpleIntegerProperty(customerId);
        this.type = new SimpleStringProperty(type);
        this.balance = new SimpleDoubleProperty(balance);
    }

    // Getters
    public int getAccountId() { return accountId.get(); }
    public int getCustomerId() { return customerId.get(); }
    public String getType() { return type.get(); }
    public double getBalance() { return balance.get(); }

    // Properties
    public IntegerProperty accountIdProperty() { return accountId; }
    public IntegerProperty customerIdProperty() { return customerId; }
    public StringProperty typeProperty() { return type; }
    public DoubleProperty balanceProperty() { return balance; }
}
