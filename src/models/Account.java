package models;

public class Account {

    private int accountId;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String hashPassword;
    
    
    public Account(int accountId, String lastName, String firstName, 
                    String phoneNumber, String email) {
        this(accountId, lastName, firstName, phoneNumber, email, null);
    }

    public Account(int accountId, String lastName, String firstName, 
                    String phoneNumber, String email, String hashPassword) {
        this.accountId = accountId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.hashPassword = hashPassword;
    }
    
    @Override
    public String toString() {
	return "Account [accountId=" + accountId + ", firstName=" + firstName + ", lastName=" + lastName
		+ ", phoneNumber=" + phoneNumber + ", email=" + email + "]";
    }

    
    public int getAccountId() {
        return accountId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getHashPassword() {
        return hashPassword;
    }

    public String toCSV() {
        return accountId + "," + lastName + "," + firstName + "," + phoneNumber + "," + email + "," + hashPassword;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }
    
}
