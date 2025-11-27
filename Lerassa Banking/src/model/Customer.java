package model;

import javafx.beans.property.*;

public class Customer {

    private IntegerProperty id;
    private StringProperty title;
    private StringProperty fullName;
    private StringProperty surname;
    private StringProperty phone;
    private StringProperty email;
    private StringProperty address;
    private StringProperty idNumber;
    private StringProperty dob;

    public Customer(int id, String title, String fullName, String surname,
                    String phone, String email, String address,
                    String idNumber, String dob) {

        this.id = new SimpleIntegerProperty(id);
        this.title = new SimpleStringProperty(title);
        this.fullName = new SimpleStringProperty(fullName);
        this.surname = new SimpleStringProperty(surname);
        this.phone = new SimpleStringProperty(phone);
        this.email = new SimpleStringProperty(email);
        this.address = new SimpleStringProperty(address);
        this.idNumber = new SimpleStringProperty(idNumber);
        this.dob = new SimpleStringProperty(dob);
    }

    // --- Getters ---
    public int getId() { return id.get(); }
    public String getTitle() { return title.get(); }
    public String getFullName() { return fullName.get(); }
    public String getSurname() { return surname.get(); }
    public String getPhone() { return phone.get(); }
    public String getEmail() { return email.get(); }
    public String getAddress() { return address.get(); }
    public String getIdNumber() { return idNumber.get(); }
    public String getDob() { return dob.get(); }

    // --- Properties ---
    public IntegerProperty idProperty() { return id; }
    public StringProperty titleProperty() { return title; }
    public StringProperty fullNameProperty() { return fullName; }
    public StringProperty surnameProperty() { return surname; }
    public StringProperty phoneProperty() { return phone; }
    public StringProperty emailProperty() { return email; }
    public StringProperty addressProperty() { return address; }
    public StringProperty idNumberProperty() { return idNumber; }
    public StringProperty dobProperty() { return dob; }
}
