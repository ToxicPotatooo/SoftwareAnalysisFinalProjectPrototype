package models;

public class Staff extends Account {

    private String position;
    
    public Staff(int accountId, String lastName, String firstName, 
                 String phoneNum, String email, String hashPassword, 
                 String position) {
        super(accountId, lastName, firstName, phoneNum, email, hashPassword);
        this.position = position;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
    
}