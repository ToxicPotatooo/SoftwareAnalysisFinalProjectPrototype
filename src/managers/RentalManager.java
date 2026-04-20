package managers;

import models.Rental;
import models.Equipment;
import models.Customer;
import models.Account;
import utils.CsvHandler;
import utils.DataArrays;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Manages rental transactions, including creation, retrieval, deletion, and cost calculation.
 * This class coordinates with EquipmentManager and AccountManager to validate rentals
 * and calculate accurate pricing based on rental duration.
 */
public class RentalManager {
    
    private ArrayList<Rental> listOfRentals;
    private DataArrays data;
    private EquipmentManager equipmentManager;
    private AccountManager accountManager;
    
    /**
     * Constructs a RentalManager with the given data container.
     * Initializes the internal rental list from the data source.
     *
     * @param data the data container holding rental information
     */
    public RentalManager(DataArrays data) {
        this.data = data;
        this.listOfRentals = data.getRentalData();
        if (this.listOfRentals == null) {
            this.listOfRentals = new ArrayList<>();
        }
    }
    
    /**
     * Sets the dependent managers required for rental operations.
     * Must be called before performing rental processing.
     *
     * @param equipmentManager the manager handling equipment operations
     * @param accountManager   the manager handling customer account operations
     */
    public void setManagers(EquipmentManager equipmentManager, AccountManager accountManager) {
        this.equipmentManager = equipmentManager;
        this.accountManager = accountManager;
    }
    
    /**
     * Persists the current rental list to the CSV data source.
     */
    private void saveData() {
        data.setRentalData(listOfRentals);
        CsvHandler.csvWriter(data);
    }
    
    /**
     * Adds a rental to the system and persists the change.
     *
     * @param rental the rental object to add
     */
    public void addRental(Rental rental) {
        listOfRentals.add(rental);
        saveData();
    }
    
    /**
     * Creates a new rental entry. Synonym for {@link #addRental(Rental)}.
     *
     * @param rental the rental object to create
     */
    public void createRental(Rental rental) {
        addRental(rental);
    }
    
    /**
     * Deletes a rental from the system by its ID.
     *
     * @param rentalId the ID of the rental to delete
     * @return {@code true} if the rental was found and deleted, {@code false} otherwise
     */
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
    
    /**
     * Returns the complete list of rentals.
     *
     * @return an ArrayList of all rentals
     */
    public ArrayList<Rental> getListOfRentals() {
        return listOfRentals;
    }
    
    /**
     * Returns the complete list of rentals. Synonym for {@link #getListOfRentals()}.
     *
     * @return an ArrayList of all rentals
     */
    public ArrayList<Rental> getRentals() {
        return listOfRentals;
    }
    
    /**
     * Retrieves a rental by its unique ID.
     *
     * @param id the rental ID to search for
     * @return the matching Rental object, or {@code null} if not found
     */
    public Rental getRentalById(int id) {
        for (Rental r : listOfRentals) {
            if (r.getId() == id) {
                return r;
            }
        }
        return null;
    }
    
    /**
     * Calculates the total cost of a rental based on the equipment's daily rate and rental duration.
     * Minimum rental duration is one day.
     *
     * @param rental the rental to calculate cost for
     * @return the total rental cost, or {@code 0} if the associated equipment cannot be found
     */
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
    
    /**
     * Checks whether a customer is eligible to rent equipment.
     * A customer cannot rent if they do not exist or are banned.
     *
     * @param customerId the ID of the customer to check
     * @return {@code true} if the customer exists and is not banned, {@code false} otherwise
     */
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
    
    /**
     * Processes a new rental for a customer and equipment.
     * Generates a new rental ID, calculates the cost, and persists the rental.
     *
     * @param customer    the customer making the rental
     * @param equipment   the equipment being rented
     * @param rentalDate  the date the rental begins
     * @param returnDate  the date the rental is expected to end
     * @return the newly created Rental object
     * @throws IllegalArgumentException if the customer is banned
     */
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
    
    /**
     * Processes a new rental using an Account object instead of a Customer.
     * Retrieves the corresponding Customer and delegates to {@link #processRental(Customer, Equipment, Date, Date)}.
     *
     * @param account     the account of the customer making the rental
     * @param equipment   the equipment being rented
     * @param rentalDate  the date the rental begins
     * @param returnDate  the date the rental is expected to end
     * @return the newly created Rental object
     * @throws IllegalArgumentException if the customer is not found or is banned
     */
    public Rental processRental(Account account, Equipment equipment, Date rentalDate, Date returnDate) {
        Customer customer = accountManager.getCustomerById(account.getAccountId());
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found.");
        }
        return processRental(customer, equipment, rentalDate, returnDate);
    }
    
    /**
     * Calculates the next available rental ID based on the maximum existing ID plus one.
     *
     * @return the next available rental ID
     */
    public int getNextId() {
        int maxId = 0;
        for (Rental r : listOfRentals) {
            if (r.getId() > maxId) {
                maxId = r.getId();
            }
        }
        return maxId + 1;
    }
    
    /**
     * Calculates the next available rental ID. Synonym for {@link #getNextId()}.
     *
     * @return the next available rental ID
     */
    public int getNextRentalId() {
        return getNextId();
    }
}
