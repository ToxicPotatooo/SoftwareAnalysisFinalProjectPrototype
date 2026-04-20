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

/**
 * Main user interface manager for the Village Rentals application.
 * Provides a menu-driven console interface using Swing dialogs for all user interactions,
 * including equipment management, customer management, rental processing, and report viewing.
 */
public class UIManager {
    
    private DataArrays data;
    private AccountManager accountManager;
    private EquipmentManager equipmentManager;
    private RentalManager rentalManager;
    private ReportManager reportManager;
    
    /**
     * Constructs a UIManager and initializes the application.
     * Loads data from CSV, initializes all managers, sets up manager dependencies,
     * and displays the main menu.
     */
    public UIManager() {
        data = CsvHandler.csvReader();
        accountManager = new AccountManager(data);
        equipmentManager = new EquipmentManager(data);
        rentalManager = new RentalManager(data);
        rentalManager.setManagers(equipmentManager, accountManager);
        reportManager = new ReportManager();
        showMainMenu();
    }
    
    /**
     * Displays the main application menu and handles user selections.
     * Continues to show the menu until the user chooses to exit.
     * Options include equipment management, customer management, rental processing, and reports.
     */
    public void showMainMenu() {
        while (true) {
            String menu = """
                    Village Rentals Prototype
                    1. Add Equipment
                    2. Delete Equipment
                    3. Add Customer
                    4. Process Rental
                    5. Reports
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
                case "4" -> processRental();
                case "5" -> showReportsMenu();
                default -> JOptionPane.showMessageDialog(null, "Invalid choice.");
            }
        }
    }
    
    /**
     * Displays the reports submenu with options for generating various business reports.
     * Allows users to choose between sales by date, sales by customer, or equipment by category reports.
     * Returns to the main menu when the user selects the back option.
     */
    private void showReportsMenu() {
        while (true) {
            String reportsMenu = """
                    === REPORTS MENU ===
                    1. Sales by Date Report
                    2. Sales by Customer Report
                    3. Equipment by Category Report
                    0. Back to Main Menu
                    Enter your choice:
                    """;
            
            String choice = JOptionPane.showInputDialog(null, reportsMenu, "Village Rentals - Reports", JOptionPane.PLAIN_MESSAGE);
            
            if (choice == null || choice.trim().equals("0")) {
                return;
            }
            
            switch (choice.trim()) {
                case "1" -> showSalesByDateReport();
                case "2" -> showSalesByCustomerReport();
                case "3" -> showItemsByCategoryReport();
                default -> JOptionPane.showMessageDialog(null, "Invalid choice.");
            }
        }
    }
    
    /**
     * Prompts the user for equipment details and adds a new equipment item to the system.
     * Automatically assigns the next available equipment ID.
     */
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
    
    /**
     * Prompts the user for an equipment ID and deletes the corresponding equipment from the system.
     * Displays success or failure messages based on whether the equipment was found.
     */
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
    
    /**
     * Prompts the user for customer details and adds a new customer account to the system.
     * Automatically assigns the next available customer ID and uses default values for password and credit card.
     */
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
    
    /**
     * Processes a rental transaction by prompting the user for customer ID, equipment ID, and rental dates.
     * Validates that the customer exists, is not banned, and that the equipment exists.
     * Displays the calculated rental cost upon successful creation.
     */
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
    
    /**
     * Displays the Sales by Date Report.
     * Prompts the user for start and end dates, then generates and displays the report.
     */
    private void showSalesByDateReport() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = sdf.parse(prompt("Enter start date (yyyy-MM-dd):"));
            Date endDate = sdf.parse(prompt("Enter end date (yyyy-MM-dd):"));
            
            String report = reportManager.buildSalesByDateReport(
                rentalManager.getListOfRentals(),
                equipmentManager.getListOfEquipment(),
                accountManager.getListOfAccounts(),
                startDate,
                endDate
            );
            showMessage(report, "Sales by Date Report");
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(null, "Invalid date format. Use yyyy-MM-dd.");
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    /**
     * Displays the Sales by Customer Report.
     * Generates and displays a report showing all customers with their rental history and totals.
     */
    private void showSalesByCustomerReport() {
        String report = reportManager.buildSalesByCustomerReport(
            rentalManager.getListOfRentals(),
            equipmentManager.getListOfEquipment(),
            accountManager.getListOfAccounts()
        );
        showMessage(report, "Sales by Customer Report");
    }
    
    /**
     * Displays the Equipment by Category Report.
     * Generates and displays a report showing all equipment organized by their categories.
     */
    private void showItemsByCategoryReport() {
        String report = reportManager.buildItemsByCategoryReport(
            equipmentManager.getListOfEquipment()
        );
        showMessage(report, "Equipment by Category Report");
    }
    
    /**
     * Displays a prompt dialog and returns the user's input.
     *
     * @param message the message to display in the prompt dialog
     * @return the trimmed user input
     * @throws IllegalArgumentException if the user cancels or enters an empty string
     */
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
    
    /**
     * Displays a message in a scrollable text area dialog for viewing large amounts of text.
     *
     * @param message the message content to display
     * @param title the title of the dialog window
     */
    private void showMessage(String message, String title) {
        JTextArea textArea = new JTextArea(message, 20, 50);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        JOptionPane.showMessageDialog(null, scrollPane, title, JOptionPane.INFORMATION_MESSAGE);
    }
}
