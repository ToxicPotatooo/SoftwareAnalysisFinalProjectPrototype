package managers;

import models.Account;
import models.Customer;
import utils.CsvHandler;
import utils.DataArrays;
import java.util.ArrayList;

/**
 * Manages customer accounts, including creation, deletion, retrieval, and ban/unban operations.
 * This class serves as the primary interface for account-related business logic and persistence.
 */
public class AccountManager {
    
    private ArrayList<Customer> listOfAccounts;
    private DataArrays data;
    
    /**
     * Constructs an AccountManager with the given data container.
     * Initializes the internal account list by converting Account objects into Customer objects.
     *
     * @param data the data container holding raw account information
     */
    public AccountManager(DataArrays data) {
        this.data = data;
        this.listOfAccounts = new ArrayList<>();
        
        for (Account acc : data.getAccountData()) {
            Customer customer = new Customer(
                acc.getAccountId(),
                acc.getLastName(),
                acc.getFirstName(),
                acc.getPhoneNumber(),
                acc.getEmail(),
                "",
                false,
                ""
            );
            listOfAccounts.add(customer);
        }
    }
    
    /**
     * Persists the current list of customer accounts to the CSV data source.
     * Converts Customer objects back to the data structure expected by the CSV handler.
     */
    private void saveData() {
        data.setAccountData(new ArrayList<>());
        for (Customer c : listOfAccounts) {
            data.getAccountData().add(c);
        }
        CsvHandler.csvWriter(data);
    }
    
    /**
     * Adds a new customer account to the manager and persists the change.
     *
     * @param customer the customer account to add
     * @throws IllegalArgumentException if an account with the same ID already exists
     */
    public void addAccount(Customer customer) {
        for (Customer c : listOfAccounts) {
            if (c.getAccountId() == customer.getAccountId()) {
                throw new IllegalArgumentException("Account ID already exists.");
            }
        }
        listOfAccounts.add(customer);
        saveData();
    }
    
    /**
     * Deletes a customer account by its ID.
     *
     * @param accountId the ID of the account to delete
     * @return {@code true} if the account was found and deleted, {@code false} otherwise
     */
    public boolean deleteAccount(int accountId) {
        for (int i = 0; i < listOfAccounts.size(); i++) {
            if (listOfAccounts.get(i).getAccountId() == accountId) {
                listOfAccounts.remove(i);
                saveData();
                return true;
            }
        }
        return false;
    }
    
    /**
     * Returns the list of all customer accounts.
     *
     * @return an ArrayList of all customers
     */
    public ArrayList<Customer> getListOfAccounts() {
        return listOfAccounts;
    }
    
    /**
     * Returns the list of all customer accounts.
     *
     * @return an ArrayList of all customers
     */
    public ArrayList<Customer> getAccounts() {
        return listOfAccounts;
    }
    
    /**
     * Retrieves a customer account by its unique ID.
     *
     * @param id the account ID to search for
     * @return the matching Customer object, or {@code null} if not found
     */
    public Customer getCustomerById(int id) {
        for (Customer c : listOfAccounts) {
            if (c.getAccountId() == id) {
                return c;
            }
        }
        return null;
    }
    
    /**
     * Finds an account by its ID. Synonym for {@link #getCustomerById(int)}.
     *
     * @param id the account ID to search for
     * @return the matching Customer object, or {@code null} if not found
     */
    public Customer findAccountById(int id) {
        return getCustomerById(id);
    }
    
    /**
     * Calculates the next available account ID based on the maximum existing ID plus one.
     *
     * @return the next available account ID
     */
    public int getNextId() {
        int maxId = 0;
        for (Customer c : listOfAccounts) {
            if (c.getAccountId() > maxId) {
                maxId = c.getAccountId();
            }
        }
        return maxId + 1;
    }
    
    /**
     * Calculates the next available account ID. Synonym for {@link #getNextId()}.
     *
     * @return the next available account ID
     */
    public int getNextAccountId() {
        return getNextId();
    }
    
    /**
     * Creates a new account by delegating to {@link #addAccount(Customer)}.
     *
     * @param customer the customer account to create
     */
    public void createNewAccount(Customer customer) {
        addAccount(customer);
    }
    
    /**
     * Bans a customer account by setting its banned status to {@code true}.
     *
     * @param accountId the ID of the account to ban
     * @return {@code true} if the account was found and banned, {@code false} otherwise
     */
    public boolean banCustomer(int accountId) {
        Customer customer = getCustomerById(accountId);
        if (customer != null) {
            customer.setBanned(true);
            saveData();
            return true;
        }
        return false;
    }
    
    /**
     * Unbans a customer account by setting its banned status to {@code false}.
     *
     * @param accountId the ID of the account to unban
     * @return {@code true} if the account was found and unbanned, {@code false} otherwise
     */
    public boolean unbanCustomer(int accountId) {
        Customer customer = getCustomerById(accountId);
        if (customer != null) {
            customer.setBanned(false);
            saveData();
            return true;
        }
        return false;
    }
}
