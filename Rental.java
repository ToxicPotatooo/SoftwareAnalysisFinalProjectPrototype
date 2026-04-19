import java.util.ArrayList;
import java.util.Date;

public class Rental {
    private int id;
    private Date curDate;
    private int customerId;
    private Date rentalDate;
    private Date returnDate;
    private ArrayList<Integer> listOfEquipmentRented;
    private double rentalCost;
    
    public Rental(int id, Date curDate, int customerId, Date rentalDate, 
                  Date returnDate, ArrayList<Integer> listOfEquipmentRented, double rentalCost) {
        this.id = id;
        this.curDate = curDate;
        this.customerId = customerId;
        this.rentalDate = rentalDate;
        this.returnDate = returnDate;
        this.listOfEquipmentRented = listOfEquipmentRented;
        this.rentalCost = rentalCost;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCurDate() {
        return curDate;
    }

    public void setCurDate(Date curDate) {
        this.curDate = curDate;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public Date getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(Date rentalDate) {
        this.rentalDate = rentalDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public ArrayList<Integer> getListOfEquipmentRented() {
        return listOfEquipmentRented;
    }

    public void setListOfEquipmentRented(ArrayList<Integer> listOfEquipmentRented) {
        this.listOfEquipmentRented = listOfEquipmentRented;
    }

    public double getRentalCost() {
        return rentalCost;
    }

    public void setRentalCost(double rentalCost) {
        this.rentalCost = rentalCost;
    }

    @Override
    public String toString() {
        return "Rental #" + id + " | Customer ID: " + customerId + " | Cost: $" + rentalCost;
    }

    public String toCSV() {
        StringBuilder equipStr = new StringBuilder();
        for (int i = 0; i < listOfEquipmentRented.size(); i++) {
            if (i > 0) equipStr.append(";");
            equipStr.append(listOfEquipmentRented.get(i));
        }
        
        return id + "," + curDate.getTime() + "," + customerId + "," + 
               rentalDate.getTime() + "," + returnDate.getTime() + "," + 
               equipStr.toString() + "," + rentalCost;
    }
}