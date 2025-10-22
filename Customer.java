public class Customer {
    private String customerId;
    private String firstName;
    private String surname;
    private String address;
    private String idNumber;
    private String employmentCompany;
    private String companyAddress;
    
    public Customer(String customerId, String firstName, String surname, String address, String idNumber) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.surname = surname;
        this.address = address;
        this.idNumber = idNumber;
    }
    
    public Customer(String customerId, String firstName, String surname, String address, 
                   String idNumber, String employmentCompany, String companyAddress) {
        this(customerId, firstName, surname, address, idNumber);
        this.employmentCompany = employmentCompany;
        this.companyAddress = companyAddress;
    }
    
    // Getters
    public String getCustomerId() { return customerId; }
    public String getFirstName() { return firstName; }
    public String getSurname() { return surname; }
    public String getAddress() { return address; }
    public String getIdNumber() { return idNumber; }
    public String getEmploymentCompany() { return employmentCompany; }
    public String getCompanyAddress() { return companyAddress; }
    
    public boolean isEmployed() {
        return employmentCompany != null && !employmentCompany.trim().isEmpty();
    }
    
    @Override
    public String toString() {
        return "Customer{ID='" + customerId + "', Name='" + firstName + " " + surname + "', Address='" + address + "'}";
    }
}