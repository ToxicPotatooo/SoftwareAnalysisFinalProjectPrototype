package models;

public class Account {

    private int accountId;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    
    
    public Account (int accountId, String firstName, String lastName, 
	    		String phoneNumber, String email) {
	
	this.accountId = accountId;
	this.firstName = firstName;
	this.lastName = lastName;
	this.phoneNumber = phoneNumber;
	this.email = email;
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

//    public String getHashPassword() {
//        return hashPassword;
//    }

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

//    public void setHashPassword(String hashPassword) {
//        this.hashPassword = hashPassword;
//    }
    
}
