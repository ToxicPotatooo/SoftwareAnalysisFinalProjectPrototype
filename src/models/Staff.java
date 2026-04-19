package models;

public class Staff extends Account {

    private int staffId;
    private String position;

    public Staff(int staffId, String lastName, String firstName, String phoneNum, String email, String hashPassword, String position) {
        super(staffId, lastName, firstName, phoneNum, email, hashPassword);
        this.staffId = staffId;
        this.position = position;
    }

    // Getters and Setters
    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

}
