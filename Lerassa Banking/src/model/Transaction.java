package model;

public class Transaction {
    private int id;
    private int customerId;
    private String type;
    private double amount;
    private String datetime;

    public Transaction(int id, int customerId, String type, double amount, String datetime){
        this.id=id;
        this.customerId=customerId;
        this.type=type;
        this.amount=amount;
        this.datetime=datetime;
    }

    public int getId(){return id;}
    public int getCustomerId(){return customerId;}
    public String getType(){return type;}
    public double getAmount(){return amount;}
    public String getDatetime(){return datetime;}
}
