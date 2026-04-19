package managers;

import models.Account;
import models.Customer;
import utils.FileHandler;
import java.util.ArrayList;

public class AccountManager {
    
    private ArrayList<Customer> listOfAccounts;
    private FileHandler fileHandler;
    
    public AccountManager() {
        fileHandler = new FileHandler();
        listOfAccounts = fileHandler.loadCustomers();
    }
    
    public boolean createNewAccount(Customer customer) {
        for (Customer c : listOfAccounts) {
            if (c.getAccountId() == customer.getAccountId()) {
                return false;
            }
        }
        listOfAccounts.add(customer);
        fileHandler.saveCustomers(listOfAccounts);
        return true;
    }
    
    public boolean updateAccount(int accountId, Customer updatedCustomer) {
        for (int i = 0; i < listOfAccounts.size(); i++) {
            if (listOfAccounts.get(i).getAccountId() == accountId) {
                listOfAccounts.set(i, updatedCustomer);
                fileHandler.saveCustomers(listOfAccounts);
                return true;
            }
        }
        return false;
    }
    
    public boolean deleteAccount(int accountId) {
        for (int i = 0; i < listOfAccounts.size(); i++) {
            if (listOfAccounts.get(i).getAccountId() == accountId) {
                listOfAccounts.remove(i);
                fileHandler.saveCustomers(listOfAccounts);
                return true;
            }
        }
        return false;
    }
    
    public ArrayList<Customer> getListOfAccounts() {
        return listOfAccounts;
    }
    
    public Customer getCustomerById(int id) {
        for (Customer c : listOfAccounts) {
            if (c.getAccountId() == id) {
                return c;
            }
        }
        return null;
    }
    
    public Customer getCustomerByEmail(String email) {
        for (Customer c : listOfAccounts) {
            if (c.getEmail().equalsIgnoreCase(email)) {
                return c;
            }
        }
        return null;
    }
    
    public int getNextId() {
        int maxId = 0;
        for (Customer c : listOfAccounts) {
            if (c.getAccountId() > maxId) {
                maxId = c.getAccountId();
            }
        }
        return maxId + 1;
    }
    
    public boolean banCustomer(int accountId) {
        Customer customer = getCustomerById(accountId);
        if (customer != null) {
            customer.setBanned(true);
            fileHandler.saveCustomers(listOfAccounts);
            return true;
        }
        return false;
    }
    
    public boolean unbanCustomer(int accountId) {
        Customer customer = getCustomerById(accountId);
        if (customer != null) {
            customer.setBanned(false);
            fileHandler.saveCustomers(listOfAccounts);
            return true;
        }
        return false;
    }
}
