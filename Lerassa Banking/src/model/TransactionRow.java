package model;

import javafx.beans.property.*;

public class TransactionRow {

    private IntegerProperty id;
    private StringProperty account;
    private StringProperty type;
    private DoubleProperty amount;
    private StringProperty date;

    public TransactionRow(int id, String account, String type, double amount, String date) {
        this.id = new SimpleIntegerProperty(id);
        this.account = new SimpleStringProperty(account);
        this.type = new SimpleStringProperty(type);
        this.amount = new SimpleDoubleProperty(amount);
        this.date = new SimpleStringProperty(date);
    }

    public IntegerProperty idProperty() { return id; }
    public StringProperty accountProperty() { return account; }
    public StringProperty typeProperty() { return type; }
    public DoubleProperty amountProperty() { return amount; }
    public StringProperty dateProperty() { return date; }
}
