public class Customer {
    private String firstName;
    private String surname;
    private String address;
    private String customerId;
    private String employmentCompany; // Specific for cheque accounts
    private String companyAddress;    // Specific for cheque accounts
    
    public Customer(String firstName, String surname, String address, String customerId) {
        this.firstName = firstName;
        this.surname = surname;
        this.address = address;
        this.customerId = customerId;
    }
    
    // Getters and setters
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    
    public String getEmploymentCompany() { return employmentCompany; }
    public void setEmploymentCompany(String employmentCompany) { this.employmentCompany = employmentCompany; }
    
    public String getCompanyAddress() { return companyAddress; }
    public void setCompanyAddress(String companyAddress) { this.companyAddress = companyAddress; }
    
    // Method to set employment info for cheque accounts
    public void setEmploymentInfo(String company, String address) {
        this.employmentCompany = company;
        this.companyAddress = address;
    }
    
    @Override
    public String toString() {
        return "Customer{" +
                "firstName='" + firstName + '\'' +
                ", surname='" + surname + '\'' +
                ", address='" + address + '\'' +
                ", customerId='" + customerId + '\'' +
                '}';
    }
}