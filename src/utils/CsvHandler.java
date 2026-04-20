package utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.ArrayList;
import models.*;

/**
 * Enumeration representing the different data sections within the CSV file.
 * Used during parsing to determine which type of data is currently being read.
 */
enum Section {
    NONE,           // No section detected yet
    CATEGORIES,     // Currently reading category data
    EQUIPMENT,      // Currently reading equipment data
    CUSTOMERS,      // Currently reading customer data
    RENTALS         // Currently reading rental data
}

/**
 * Handles CSV file read and write operations for the Village Rentals application.
 * Provides static methods to parse data from a CSV file into DataArrays objects
 * and to write DataArrays content back to the CSV file.
 * 
 * The CSV file follows a section-based format with headers and data rows.
 * Each section is clearly marked and contains related data.
 */
public class CsvHandler {   

    /** The file path to the CSV data file relative to the project root */
    static String FILEPATH = "res/data-samples.csv";
    
    /**
     * Reads data from the CSV file and populates a DataArrays object.
     * Parses sections sequentially, detecting section headers and processing
     * data rows according to the current section context.
     * 
     * The method handles:
     * - Section detection (CATEGORY LIST, RENTAL EQUIPMENT, etc.)
     * - Header row skipping
     * - Empty line filtering
     * - Data validation and error handling
     *
     * @return a DataArrays object containing all parsed equipment, customer, and rental data
     */
    public static DataArrays csvReader() {
        // Initialize data container and section tracker
        DataArrays data = new DataArrays();
        Section section = Section.NONE;
        
        try (BufferedReader br = new BufferedReader(new FileReader(FILEPATH))) {
            String line;
            
            // Read the file line by line
            while ((line = br.readLine()) != null) {
                
                // Skip empty lines or lines that contain only commas (e.g., ",,,,,,")
                if (line.trim().isEmpty() || line.replace(",", "").trim().isEmpty()) {
                    continue;
                }
                
                // SECTION DETECTION - Identify which data block we're entering
                // Check for category section header
                if (line.toUpperCase().contains("CATEGORY LIST")) {
                    section = Section.CATEGORIES;
                    continue;
                }
                // Check for equipment section header
                if (line.toUpperCase().contains("RENTAL EQUIPMENT")) {
                    section = Section.EQUIPMENT;
                    continue;
                }
                // Check for customer section header
                if (line.toUpperCase().contains("CUSTOMER INFORMATION")) {
                    section = Section.CUSTOMERS;
                    continue;
                }
                // Check for rental section header
                if (line.toUpperCase().contains("RENTAL INFORMATION")) {
                    section = Section.RENTALS;
                    continue;
                }
                
                // SKIP HEADER ROWS - These contain column names, not actual data
                if (line.contains("equipment_id") ||
                    line.contains("customer_id") ||
                    line.contains("rental_id") ||
                    line.contains("category id")) {
                    continue;
                }
                
                // DATA CLEANING - Remove empty values caused by trailing commas
                // Split the line by commas
                String[] rawLine = line.split(",");
                ArrayList<String> cleanValues = new ArrayList<>();
                
                // Filter out empty strings and trim whitespace
                for (String value : rawLine) {
                    String trimmed = value.trim();
                    if (!trimmed.isEmpty()) {
                        cleanValues.add(trimmed);
                    }
                }
                
                // Convert cleaned values to array for easier access
                String[] pLine = cleanValues.toArray(new String[0]);
                
                // DATA PARSING - Process based on current section
                switch (section) {
                    
                    case EQUIPMENT:
                        // Equipment data requires at least 5 fields: ID, Category ID, Name, Description, Daily Rate
                        if (pLine.length >= 5) {
                            try {
                                int equipId = Integer.valueOf(pLine[0]);
                                int catId = Integer.valueOf(pLine[1]);
                                String name = pLine[2];
                                String desc = pLine[3];
                                double dRate = Double.valueOf(pLine[4]);
                                data.getEquipData().add(new Equipment(equipId, catId, name, desc, dRate));
                            } catch (NumberFormatException e) {
                                System.err.println("Error parsing equipment: " + line);
                            }
                        }
                        break;
                        
                    case CUSTOMERS:
                        // Customer data requires at least 5 fields: ID, Last Name, First Name, Phone, Email
                        if (pLine.length >= 5) {
                            try {
                                int cusId = Integer.valueOf(pLine[0]);
                                String lName = pLine[1];
                                String fName = pLine[2];
                                String phone = pLine[3];
                                String eMail = pLine[4];
                                data.getAccountData().add(new Account(cusId, lName, fName, phone, eMail));
                            } catch (NumberFormatException e) {
                                System.err.println("Error parsing customer: " + line);
                            }
                        }
                        break;
                    
                    case RENTALS:
                        // Rental data requires at least 7 fields: ID, Current Date, Customer ID, Equipment ID, Rental Date, Return Date, Cost
                        if (pLine.length >= 7) {
                            try {
                                int rentId = Integer.valueOf(pLine[0]);
                                Date curDate = Date.valueOf(pLine[1]);
                                int cusId2 = Integer.valueOf(pLine[2]);
                                int equipId2 = Integer.valueOf(pLine[3]);
                                Date rentDate = Date.valueOf(pLine[4]);
                                Date returnDate = Date.valueOf(pLine[5]);
                                double cost = Double.valueOf(pLine[6]);
                                data.getRentalData().add(new Rental(rentId, curDate, cusId2, equipId2, rentDate, returnDate, cost));
                            } catch (Exception e) {
                                System.err.println("Error parsing rental: " + line);
                            }
                        }
                        break;
                    
                    default:
                        // No valid section detected - skip this line
                        break;
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("CSV file not found: " + FILEPATH);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error reading CSV file");
            e.printStackTrace();
        }
        
        return data;
    }
    
    /**
     * Writes data from a DataArrays object to the CSV file.
     * This method COMPLETELY REWRITES the file with current data from memory.
     * 
     * The file is written in a structured format with the following sections:
     * 1. Category List - Hardcoded categories
     * 2. Rental Equipment - All equipment from data.getEquipData()
     * 3. Customer Information - All customers from data.getAccountData()
     * 4. Rental Information - All rentals from data.getRentalData()
     * 
     * Each section includes appropriate headers and blank line separators
     * for readability and consistent parsing.
     *
     * @param data the DataArrays object containing equipment, customer, and rental data
     * @return {@code true} if the write operation was successful, {@code false} otherwise
     */
    public static boolean csvWriter(DataArrays data) {
        try (PrintWriter pw = new PrintWriter(FILEPATH)) {
            StringBuilder sb = new StringBuilder();
            
            // ===== CATEGORIES SECTION =====
            // Write category section header
            sb.append("Category List\n");
            // Write category column headers
            sb.append("category id,name\n");
            // Write hardcoded category data
            sb.append("10,Power Tools\n");
            sb.append("20,Yard Equipment\n");
            sb.append("30,Compressors\n");
            sb.append("40,Generators\n");
            sb.append("50,Air Tools\n");
            sb.append("\n");  // Blank line separator for readability
            
            // ===== EQUIPMENT SECTION =====
            // Write equipment section header
            sb.append("Rental Equipment\n");
            // Write equipment column headers
            sb.append("equipment_id,category_id,name,description,daily_rate\n");
            // Write all equipment data from memory
            for (Equipment e : data.getEquipData()) {
                sb.append(e.getEquipmentId()).append(",")
                  .append(e.getCategoryId()).append(",")
                  .append(e.getName()).append(",")
                  .append(e.getDesc()).append(",")
                  .append(e.getDailyRentCost()).append("\n");
            }
            sb.append("\n");  // Blank line separator for readability
            
            // ===== CUSTOMER SECTION =====
            // Write customer section header
            sb.append("Customer Information\n");
            // Write customer column headers
            sb.append("customer_id,last_name,first_name,contact_phone,e-mail\n");
            // Write all customer data from memory
            for (Account a : data.getAccountData()) {
                sb.append(a.getAccountId()).append(",")
                  .append(a.getLastName()).append(",")
                  .append(a.getFirstName()).append(",")
                  .append(a.getPhoneNumber()).append(",")
                  .append(a.getEmail()).append("\n");
            }
            sb.append("\n");  // Blank line separator for readability
            
            // ===== RENTALS SECTION =====
            // Write rental section header
            sb.append("Rental Information\n");
            // Write rental column headers
            sb.append("rental_id,date,customer_id,equipment_id,rental_date,return_date,cost\n");
            // Write all rental data from memory
            for (Rental r : data.getRentalData()) {
                sb.append(r.getId()).append(",")
                  .append(r.getCurDate()).append(",")
                  .append(r.getCusId()).append(",")
                  .append(r.getEquipId()).append(",")
                  .append(r.getRentalDate()).append(",")
                  .append(r.getReturnDate()).append(",")
                  .append(r.getRentalCost()).append("\n");
            }
            
            // Write the complete file content
            pw.write(sb.toString());
            pw.flush();  // Ensure all data is physically written to disk
            return true;
            
        } catch (FileNotFoundException e) {
            System.err.println("Cannot write to CSV file: " + FILEPATH);
            e.printStackTrace();
            return false;
        }
    }
}