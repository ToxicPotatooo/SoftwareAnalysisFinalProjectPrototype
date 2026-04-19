public class Customer  extends Account {

    private int customerId;
    private boolean isBanned;
    private String creditCard;

    public Customer(int customerId, String lastName, String firstName, String phoneNum, String email, String hashPassword, boolean isBanned, String creditCard) {
        super(customerId, lastName, firstName, phoneNum, email, hashPassword);
        this.customerId = customerId;
        this.isBanned = isBanned;
        this.creditCard = creditCard;
    }

    // Getters and Setters
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public boolean isBanned() {
        return isBanned;
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
    }

    public String getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    @Override
    public String toString() {
        return super.toString() + (isBanned ? " [BANNED]" : "");
    }
    
    public String toCSV() {
        return super.toCSV() + "," + isBanned + "," + creditCard;
    }
}