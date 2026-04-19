package models;

import java.util.Date;

public class Rental {

    private int id;
    private Date curDate;
    private int cusId;
    private int equipId;
    private Date rentalDate;
    private Date returnDate;
    //private ArrayList<Equipment> equipmentRented;
    private double rentalCost;
    
    
    public Rental(int id, Date curDate, int cusId, int equipId, Date rentalDate, Date returnDate,
	    double rentalCost) {
	
	this.id = id;
	this.curDate = curDate;
	this.cusId = cusId;
	this.equipId = equipId;
	this.rentalDate = rentalDate;
	this.returnDate = returnDate;
	this.rentalCost = rentalCost;
    }
    
    @Override
    public String toString() {
	return "Rental [id=" + id + ", curDate=" + curDate + ", cusId=" + cusId + ", equipId=" + equipId
		+ ", rentalDate=" + rentalDate + ", returnDate=" + returnDate + ", rentalCost=" + rentalCost + "]";
    }

    public int getId() {
        return id;
    }

    public Date getCurDate() {
        return curDate;
    }

    public int getCusId() {
        return cusId;
    }

    public int getEquipId() {
        return equipId;
    }

    public Date getRentalDate() {
        return rentalDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

//    public ArrayList<Equipment> getEquipmentRented() {
//        return equipmentRented;
//    }

    public double getRentalCost() {
        return rentalCost;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCurDate(Date curDate) {
        this.curDate = curDate;
    }

    public void setCusId(int cusId) {
        this.cusId = cusId;
    }

    public void setEquipId(int equipId) {
        this.equipId = equipId;
    }

    public void setRentalDate(Date rentalDate) {
        this.rentalDate = rentalDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

//    public void setEquipmentRented(ArrayList<Equipment> equipmentRented) {
//        this.equipmentRented = equipmentRented;
//    }

    public void setRentalCost(double rentalCost) {
        this.rentalCost = rentalCost;
    }

        
}
