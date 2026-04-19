public class Customer extends Account {
    private boolean isBanned;
    private String creditCard;
    
    public Customer(int accountId, String lastName, String firstName, 
                    String phoneNum, String email, String hashPassword,
                    boolean isBanned, String creditCard) {
        super(accountId, lastName, firstName, phoneNum, email, hashPassword);
        this.isBanned = isBanned;
        this.creditCard = creditCard;
    }
    
    public boolean isBanned() { return isBanned; }
    public String getCreditCard() { return creditCard; }
    
    public void setBanned(boolean banned) { isBanned = banned; }
    public void setCreditCard(String creditCard) { this.creditCard = creditCard; }
    
    @Override
    public String toString() {
        return super.toString() + (isBanned ? " [BANNED]" : "");
    }
    
    public String toCSV() {
        return super.toCSV() + "," + isBanned + "," + creditCard;
    }
}