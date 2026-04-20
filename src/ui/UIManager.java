package ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import managers.AccountManager;
import managers.EquipmentManager;
import managers.RentalManager;
import managers.ReportManager;
import models.Customer;
import models.Equipment;
import models.Rental;
import utils.CsvHandler;
import utils.DataArrays;

public class UIManager {
    
    private DataArrays data;
    private AccountManager accountManager;
    private EquipmentManager equipmentManager;
    private RentalManager rentalManager;
    private ReportManager reportManager;
    
    public UIManager() {
        data = CsvHandler.csvReader();
        accountManager = new AccountManager(data);
        equipmentManager = new EquipmentManager(data);
        rentalManager = new RentalManager(data);
        rentalManager.setManagers(equipmentManager, accountManager);
        reportManager = new ReportManager();
        showMainMenu();
    }
    
    public void showMainMenu() {
        while (true) {
            String menu = """
                    Village Rentals Prototype
                    1. Add Equipment
                    2. Delete Equipment
                    3. Add Customer
                    4. Display All Equipment
                    5. Display All Customers
                    6. Process Rental
                    7. Display All Rentals
                    0. Exit
                    Enter your choice:
                    """;
            
            String choice = JOptionPane.showInputDialog(null, menu, "Village Rentals", JOptionPane.PLAIN_MESSAGE);
            
            if (choice == null || choice.trim().equals("0")) {
                JOptionPane.showMessageDialog(null, "Goodbye!");
                break;
            }
            
            switch (choice.trim()) {
                case "1" -> addEquipment();
                case "2" -> deleteEquipment();
                case "3" -> addCustomer();
                case "4" -> showMessage(reportManager.buildEquipmentReport(equipmentManager.getListOfEquipment()), "Equipment List");
                case "5" -> showMessage(reportManager.buildAccountReport(accountManager.getListOfAccounts()), "Customer List");
                case "6" -> processRental();
                case "7" -> showMessage(reportManager.buildRentalReport(rentalManager.getListOfRentals()), "Rental List");
                default -> JOptionPane.showMessageDialog(null, "Invalid choice.");
            }
        }
    }
    
    private void addEquipment() {
        try {
            int id = equipmentManager.getNextId();
            int categoryId = Integer.parseInt(prompt("Enter category ID (10=Power Tools, 20=Yard Equipment, 30=Compressors, 40=Generators, 50=Air Tools):"));
            String name = prompt("Enter equipment name:");
            String desc = prompt("Enter description:");
            double cost = Double.parseDouble(prompt("Enter daily rental cost:"));
            
            Equipment equipment = new Equipment(id, categoryId, name, desc, cost);
            equipmentManager.createNewEquipment(equipment);
            JOptionPane.showMessageDialog(null, "Equipment added successfully.\nEquipment ID: " + id);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid number input.");
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    private void deleteEquipment() {
        try {
            int equipmentId = Integer.parseInt(prompt("Enter equipment ID to delete:"));
            boolean removed = equipmentManager.deleteEquipment(equipmentId);
            if (removed) {
                JOptionPane.showMessageDialog(null, "Equipment deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "Equipment ID not found.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid equipment ID.");
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    private void addCustomer() {
        try {
            int id = accountManager.getNextId();
            String firstName = prompt("Enter first name:");
            String lastName = prompt("Enter last name:");
            String phone = prompt("Enter phone number:");
            String email = prompt("Enter email:");
            String password = "default123";
            boolean isBanned = false;
            String creditCard = "XXXX-0000";
            
            Customer customer = new Customer(id, lastName, firstName, phone, email, password, isBanned, creditCard);
            accountManager.createNewAccount(customer);
            JOptionPane.showMessageDialog(null, "Customer added successfully.\nCustomer ID: " + id);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    private void processRental() {
        try {
            int customerId = Integer.parseInt(prompt("Enter customer ID:"));
            int equipmentId = Integer.parseInt(prompt("Enter equipment ID:"));
            
            Customer customer = accountManager.getCustomerById(customerId);
            Equipment equipment = equipmentManager.getEquipmentById(equipmentId);
            
            if (customer == null) {
                JOptionPane.showMessageDialog(null, "Customer not found.");
                return;
            }
            
            if (customer.isBanned()) {
                JOptionPane.showMessageDialog(null, "Customer is banned. Cannot rent.");
                return;
            }
            
            if (equipment == null) {
                JOptionPane.showMessageDialog(null, "Equipment not found.");
                return;
            }
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date rentalDate = sdf.parse(prompt("Enter rental date (yyyy-MM-dd):"));
            Date returnDate = sdf.parse(prompt("Enter return date (yyyy-MM-dd):"));
            
            Rental rental = rentalManager.processRental(customer, equipment, rentalDate, returnDate);
            JOptionPane.showMessageDialog(null,
                    "Rental created successfully.\nRental ID: " + rental.getId() +
                    "\nCost: $" + rental.getRentalCost());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid numeric input.");
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(null, "Invalid date format. Use yyyy-MM-dd.");
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    private String prompt(String message) {
        String input = JOptionPane.showInputDialog(null, message, "Village Rentals", JOptionPane.PLAIN_MESSAGE);
        if (input == null) {
            throw new IllegalArgumentException("Action cancelled.");
        }
        input = input.trim();
        if (input.isEmpty()) {
            throw new IllegalArgumentException("Input cannot be empty.");
        }
        return input;
    }
    
    private void showMessage(String message, String title) {
        JTextArea textArea = new JTextArea(message, 20, 50);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        JOptionPane.showMessageDialog(null, scrollPane, title, JOptionPane.INFORMATION_MESSAGE);
    }
}
