package utils;

import java.util.ArrayList;

import models.Account;
import models.Equipment;
import models.Rental;

public class DataArrays {
    
    private ArrayList<Equipment> equipData = new ArrayList<>();
    private ArrayList<Account> accountData = new ArrayList<>();
    private ArrayList<Rental> rentalData = new ArrayList<>();
    
    
    public ArrayList<Equipment> getEquipData() {
        return equipData;
    }
    public ArrayList<Account> getAccountData() {
        return accountData;
    }
    public ArrayList<Rental> getRentalData() {
        return rentalData;
    }
    public void setEquipData(ArrayList<Equipment> equipData) {
        this.equipData = equipData;
    }
    public void setAccountData(ArrayList<Account> accountData) {
        this.accountData = accountData;
    }
    public void setRentalData(ArrayList<Rental> rentalData) {
        this.rentalData = rentalData;
    }
}
