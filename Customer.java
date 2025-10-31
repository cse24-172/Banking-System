package src;

public class Customer {
    private String customerId;
    private String firstName;
    private String surname;
    private String address;
    private String idNumber;
    private String employer;
    private String employerAddress;
    
    // Constructor for employed customers
    public Customer(String customerId, String firstName, String surname, String address, 
                   String idNumber, String employer, String employerAddress) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.surname = surname;
        this.address = address;
        this.idNumber = idNumber;
        this.employer = employer;
        this.employerAddress = employerAddress;
    }
    
    // Constructor for unemployed customers
    public Customer(String customerId, String firstName, String surname, String address, String idNumber) {
        this(customerId, firstName, surname, address, idNumber, null, null);
    }
    
    // Getters
    public String getCustomerId() { return customerId; }
    public String getFirstName() { return firstName; }
    public String getSurname() { return surname; }
    public String getAddress() { return address; }
    public String getIdNumber() { return idNumber; }
    public String getEmployer() { return employer; }
    public String getEmployerAddress() { return employerAddress; }
    
    // Check if customer is employed
    public boolean isEmployed() {
        return employer != null && !employer.isEmpty();
    }
    
    @Override
    public String toString() {
        String basicInfo = "Customer{ID='" + customerId + "', Name='" + firstName + " " + surname + 
                          "', Address='" + address + "', ID Number='" + idNumber + "'";
        
        if (isEmployed()) {
            return basicInfo + ", Employer='" + employer + "', Employer Address='" + employerAddress + "'}";
        } else {
            return basicInfo + ", Employment Status='Unemployed'}";
        }
    }
}