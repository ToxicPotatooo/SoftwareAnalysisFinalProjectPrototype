public class Account {

  private int accountId;
  private String lastName;
  private String firstName;
  private String phoneNum;
  private String email;
  private String hashPassword;
  public Account(int accountId, String lastName, String firstName, String phoneNum, String email, String hashPassword) {
    this.accountId = accountId;
    this.lastName = lastName;
    this.firstName = firstName;
    this.phoneNum = phoneNum;
    this.email = email;
    this.hashPassword = hashPassword;
  }

  public int getAccountId() {
    return accountId;
  }

  public void setAccountId(int accountId) {
    this.accountId = accountId;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getPhoneNum() {
    return phoneNum;
  }

  public void setPhoneNum(String phoneNum) {
    this.phoneNum = phoneNum;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getHashPassword() {
    return hashPassword;
  }

  public void setHashPassword(String hashPassword) {
    this.hashPassword = hashPassword;
  }

      @Override
    public String toString() {
        return accountId + " | " + firstName + " " + lastName + " | " + email;
    }
    
    public String toCSV() {
        return accountId + "," + lastName + "," + firstName + "," + 
               phoneNum + "," + email + "," + hashPassword;
    }
}