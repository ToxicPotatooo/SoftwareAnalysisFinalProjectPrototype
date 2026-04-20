package managers;

import models.Rental;
import models.Equipment;
import models.Customer;
import utils.CsvHandler;
import utils.DataArrays;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class RentalManager {
    
    private ArrayList<Rental> listOfRentals;
    private DataArrays data;
    private EquipmentManager equipmentManager;
    private AccountManager accountManager;
    
    public RentalManager(DataArrays data) {
        this.data = data;
        this.listOfRentals = data.getRentalData();
    }
    
    public void setManagers(EquipmentManager equipmentManager, AccountManager accountManager) {
        this.equipmentManager = equipmentManager;
        this.accountManager = accountManager;
    }
    
    private void saveData() {
        data.setRentalData(listOfRentals);
        CsvHandler.csvWriter(data);
    }
    
    public boolean createRental(Rental rental) {
        listOfRentals.add(rental);
        saveData();
        return true;
    }
    
    public boolean updateRental(int rentalId, Rental updatedRental) {
        for (int i = 0; i < listOfRentals.size(); i++) {
            if (listOfRentals.get(i).getId() == rentalId) {
                listOfRentals.set(i, updatedRental);
                saveData();
                return true;
            }
        }
        return false;
    }
    
    public boolean deleteRental(int rentalId) {
        for (int i = 0; i < listOfRentals.size(); i++) {
            if (listOfRentals.get(i).getId() == rentalId) {
                listOfRentals.remove(i);
                saveData();
                return true;
            }
        }
        return false;
    }
    
    public ArrayList<Rental> getListOfRentals() {
        return listOfRentals;
    }
    
    public Rental getRentalById(int id) {
        for (Rental r : listOfRentals) {
            if (r.getId() == id) {
                return r;
            }
        }
        return null;
    }
    
    public ArrayList<Rental> getRentalsByCustomerId(int customerId) {
        ArrayList<Rental> result = new ArrayList<>();
        for (Rental r : listOfRentals) {
            if (r.getCusId() == customerId) {
                result.add(r);
            }
        }
        return result;
    }
    
    public ArrayList<Rental> getRentalsByDateRange(Date startDate, Date endDate) {
        ArrayList<Rental> result = new ArrayList<>();
        for (Rental r : listOfRentals) {
            if (r.getRentalDate().after(startDate) && r.getRentalDate().before(endDate)) {
                result.add(r);
            }
        }
        return result;
    }
    
    public double calculateRentalCost(Rental rental) {
        long diffInMillies = rental.getReturnDate().getTime() - rental.getRentalDate().getTime();
        long days = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        if (days < 1) days = 1;
        
        Equipment e = equipmentManager.getEquipmentById(rental.getEquipId());
        if (e != null) {
            return e.getDailyRentCost() * days;
        }
        return 0;
    }
    
    public boolean canCustomerRent(int customerId) {
        Customer customer = accountManager.getCustomerById(customerId);
        if (customer == null) {
            return false;
        }
        if (customer.isBanned()) {
            return false;
        }
        return true;
    }
    
    public Rental processRental(Customer customer, Equipment equipment, Date rentalDate, Date returnDate) {
        if (customer.isBanned()) {
            throw new IllegalArgumentException("Customer is banned.");
        }
        
        int rentalId = getNextId();
        Date curDate = new Date();
        
        Rental rental = new Rental(rentalId, curDate, customer.getAccountId(), equipment.getEquipmentId(), rentalDate, returnDate, 0);
        double cost = calculateRentalCost(rental);
        rental.setRentalCost(cost);
        
        createRental(rental);
        return rental;
    }
    
    public int getNextId() {
        int maxId = 0;
        for (Rental r : listOfRentals) {
            if (r.getId() > maxId) {
                maxId = r.getId();
            }
        }
        return maxId + 1;
    }
}
