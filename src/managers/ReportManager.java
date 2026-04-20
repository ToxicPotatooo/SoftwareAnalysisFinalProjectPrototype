package managers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;

import models.Customer;
import models.Equipment;
import models.Rental;

/**
 * Manages the generation of various business reports for the Village Rentals application.
 * Provides reporting capabilities including sales analysis by date range, customer spending
 * summaries, and equipment inventory organization by category.
 */
public class ReportManager {
    
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
    /**
     * Constructs a new ReportManager instance.
     */
    public ReportManager() {}
    
    /**
     * Generates a sales report filtered by a specific date range.
     * Displays all rentals occurring between the start and end dates, including
     * rental details, customer information, equipment rented, and total revenue.
     *
     * @param rentals the complete list of all rental transactions
     * @param equipmentList the complete list of all equipment for name lookup
     * @param customerList the complete list of all customers for name lookup
     * @param startDate the beginning date of the report range (inclusive)
     * @param endDate the ending date of the report range (inclusive)
     * @return a formatted string containing the sales report for the specified date range
     */
    public String buildSalesByDateReport(ArrayList<Rental> rentals, 
                                          ArrayList<Equipment> equipmentList,
                                          ArrayList<Customer> customerList,
                                          Date startDate, 
                                          Date endDate) {
        
        if (rentals == null || rentals.isEmpty()) {
            return "No rental data available.";
        }
        
        ArrayList<Rental> filteredRentals = new ArrayList<>();
        double totalRevenue = 0;
        
        for (Rental rental : rentals) {
            Date rentalDate = rental.getRentalDate();
            if (rentalDate != null && !rentalDate.before(startDate) && !rentalDate.after(endDate)) {
                filteredRentals.add(rental);
                totalRevenue += rental.getRentalCost();
            }
        }
        
        if (filteredRentals.isEmpty()) {
            return "No sales found between " + dateFormat.format(startDate) + " and " + dateFormat.format(endDate);
        }
        
        StringBuilder report = new StringBuilder();
        report.append("=== SALES REPORT BY DATE ===\n");
        report.append("Period: ").append(dateFormat.format(startDate))
              .append(" to ").append(dateFormat.format(endDate)).append("\n");
        report.append("-".repeat(60)).append("\n\n");
        
        report.append(String.format("%-10s %-12s %-20s %-15s %-10s\n", 
                    "Rental ID", "Date", "Customer", "Equipment", "Cost"));
        report.append("-".repeat(60)).append("\n");
        
        for (Rental rental : filteredRentals) {
            String customerName = getCustomerNameById(customerList, rental.getCusId());
            String equipmentName = getEquipmentNameById(equipmentList, rental.getEquipId());
            String rentalDate = dateFormat.format(rental.getRentalDate());
            
            report.append(String.format("%-10d %-12s %-20s %-15s $%-9.2f\n",
                        rental.getId(),
                        rentalDate,
                        truncateString(customerName, 20),
                        truncateString(equipmentName, 15),
                        rental.getRentalCost()));
        }
        
        report.append("-".repeat(60)).append("\n");
        report.append(String.format("Total Revenue: $%.2f\n", totalRevenue));
        report.append(String.format("Total Rentals: %d\n", filteredRentals.size()));
        
        return report.toString();
    }
    
    /**
     * Generates a sales report grouped by customer.
     * Displays each customer's total spending, rental count, and detailed rental history,
     * sorted by total amount spent from highest to lowest.
     *
     * @param rentals the complete list of all rental transactions
     * @param equipmentList the complete list of all equipment for name lookup
     * @param customerList the complete list of all customers for name lookup
     * @return a formatted string containing customer sales summary with details
     */
    public String buildSalesByCustomerReport(ArrayList<Rental> rentals,
                                              ArrayList<Equipment> equipmentList,
                                              ArrayList<Customer> customerList) {
        
        if (rentals == null || rentals.isEmpty()) {
            return "No rental data available.";
        }
        
        if (customerList == null || customerList.isEmpty()) {
            return "No customer data available.";
        }
        
        Map<Integer, ArrayList<Rental>> customerRentals = new HashMap<>();
        Map<Integer, Double> customerTotalSpent = new HashMap<>();
        
        for (Rental rental : rentals) {
            int customerId = rental.getCusId();
            
            customerRentals.computeIfAbsent(customerId, k -> new ArrayList<>()).add(rental);
            customerTotalSpent.merge(customerId, rental.getRentalCost(), Double::sum);
        }
        
        StringBuilder report = new StringBuilder();
        report.append("=== SALES REPORT BY CUSTOMER ===\n\n");
        
        double grandTotal = 0;
        int totalRentals = 0;
        
        ArrayList<Map.Entry<Integer, Double>> sortedCustomers = new ArrayList<>(customerTotalSpent.entrySet());
        sortedCustomers.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));
        
        for (Map.Entry<Integer, Double> entry : sortedCustomers) {
            int customerId = entry.getKey();
            double totalSpent = entry.getValue();
            ArrayList<Rental> customerRentalList = customerRentals.get(customerId);
            
            String customerName = getCustomerNameById(customerList, customerId);
            
            report.append("Customer: ").append(customerName).append(" (ID: ").append(customerId).append(")\n");
            report.append("Total Spent: $").append(String.format("%.2f", totalSpent)).append("\n");
            report.append("Rental Count: ").append(customerRentalList.size()).append("\n");
            report.append("Rental Details:\n");
            
            for (Rental rental : customerRentalList) {
                String equipmentName = getEquipmentNameById(equipmentList, rental.getEquipId());
                String rentalDate = dateFormat.format(rental.getRentalDate());
                report.append(String.format("  - %s: %s ($%.2f)\n", 
                            rentalDate, equipmentName, rental.getRentalCost()));
            }
            
            report.append("\n");
            grandTotal += totalSpent;
            totalRentals += customerRentalList.size();
        }
        
        report.append("=".repeat(50)).append("\n");
        report.append("SUMMARY\n");
        report.append("Total Customers with Rentals: ").append(sortedCustomers.size()).append("\n");
        report.append("Total Rentals: ").append(totalRentals).append("\n");
        report.append("Total Revenue: $").append(String.format("%.2f", grandTotal)).append("\n");
        
        return report.toString();
    }
    
    /**
     * Generates an inventory report organizing equipment by category.
     * Displays all equipment grouped by their category ID, including names,
     * daily rental rates, descriptions, and item counts per category.
     *
     * @param equipmentList the complete list of all equipment to categorize
     * @return a formatted string containing equipment organized by category
     */
    public String buildItemsByCategoryReport(ArrayList<Equipment> equipmentList) {
        
        if (equipmentList == null || equipmentList.isEmpty()) {
            return "No equipment data available.";
        }
        
        Map<Integer, ArrayList<Equipment>> categoryMap = new HashMap<>();
        
        for (Equipment equipment : equipmentList) {
            int categoryId = equipment.getCategoryId();
            categoryMap.computeIfAbsent(categoryId, k -> new ArrayList<>()).add(equipment);
        }
        
        StringBuilder report = new StringBuilder();
        report.append("=== EQUIPMENT BY CATEGORY ===\n\n");
        
        ArrayList<Integer> sortedCategories = new ArrayList<>(categoryMap.keySet());
        Collections.sort(sortedCategories);
        
        int totalItems = 0;
        
        for (int categoryId : sortedCategories) {
            ArrayList<Equipment> items = categoryMap.get(categoryId);
            String categoryName = getCategoryName(categoryId);
            
            report.append(categoryName).append(" (Category ID: ").append(categoryId).append(")\n");
            report.append("-".repeat(40)).append("\n");
            
            items.sort(Comparator.comparing(Equipment::getName));
            
            for (Equipment equipment : items) {
                report.append(String.format("  • %-25s $%.2f/day\n", 
                            equipment.getName(), equipment.getDailyRentCost()));
                if (equipment.getDesc() != null && !equipment.getDesc().isEmpty()) {
                    report.append("    ").append(truncateString(equipment.getDesc(), 50)).append("\n");
                }
            }
            
            report.append(String.format("\nTotal items in category: %d\n\n", items.size()));
            totalItems += items.size();
        }
        
        report.append("=".repeat(50)).append("\n");
        report.append("TOTAL EQUIPMENT ITEMS: ").append(totalItems).append("\n");
        
        return report.toString();
    }
    
    /**
     * Retrieves a customer's full name by their account ID.
     *
     * @param customerList the list of all customers to search
     * @param customerId the customer ID to look up
     * @return the customer's first and last name, or an unknown customer message if not found
     */
    private String getCustomerNameById(ArrayList<Customer> customerList, int customerId) {
        for (Customer customer : customerList) {
            if (customer.getAccountId() == customerId) {
                return customer.getFirstName() + " " + customer.getLastName();
            }
        }
        return "Unknown Customer (ID: " + customerId + ")";
    }
    
    /**
     * Retrieves an equipment name by its equipment ID.
     *
     * @param equipmentList the list of all equipment to search
     * @param equipmentId the equipment ID to look up
     * @return the equipment name, or an unknown equipment message if not found
     */
    private String getEquipmentNameById(ArrayList<Equipment> equipmentList, int equipmentId) {
        for (Equipment equipment : equipmentList) {
            if (equipment.getEquipmentId() == equipmentId) {
                return equipment.getName();
            }
        }
        return "Unknown Equipment (ID: " + equipmentId + ")";
    }
    
    /**
     * Converts a category ID to its display name.
     *
     * @param categoryId the numeric category identifier
     * @return the human-readable category name
     */
    private String getCategoryName(int categoryId) {
        switch (categoryId) {
            case 10: return "Power Tools";
            case 20: return "Yard Equipment";
            case 30: return "Compressors";
            case 40: return "Generators";
            case 50: return "Air Tools";
            default: return "Category " + categoryId;
        }
    }
    
    /**
     * Truncates a string to a specified maximum length, adding ellipsis if truncated.
     *
     * @param str the string to truncate
     * @param maxLength the maximum allowed length
     * @return the truncated string with ellipsis, or the original string if within limits
     */
    private String truncateString(String str, int maxLength) {
        if (str == null) return "";
        if (str.length() <= maxLength) return str;
        return str.substring(0, maxLength - 3) + "...";
    }
}