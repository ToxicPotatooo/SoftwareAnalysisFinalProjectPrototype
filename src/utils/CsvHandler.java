package utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Date;

import models.*;

/**
 * Enumeration representing the different data sections within the CSV file.
 * Used during parsing to determine which type of data is currently being read.
 */
enum Section {
    NONE,
    CATEGORIES,
    EQUIPMENT,
    CUSTOMERS,
    RENTALS
}

/**
 * Handles CSV file read and write operations for the Village Rentals application.
 * Provides static methods to parse data from a CSV file into DataArrays objects
 * and to write DataArrays content back to the CSV file.
 */
public class CsvHandler {   

    static String FILEPATH = "res/data-samples.csv";
    
    /**
     * Reads data from the CSV file and populates a DataArrays object.
     * Parses sections sequentially, detecting section headers and processing
     * data rows according to the current section context.
     *
     * @return a DataArrays object containing all parsed equipment, customer, and rental data
     */
    public static DataArrays csvReader() {
	
	DataArrays data = new DataArrays();
	Section section = Section.NONE;
	
	try (BufferedReader br = new BufferedReader(new FileReader(FILEPATH))) {
	    
	    String line;
	    
		while ((line = br.readLine()) != null) {

		//System.out.println("Section: " + section + " | Line: " + line);
		
		if (line.trim().isEmpty() || line.startsWith(",")) {
		    continue;
		}
		
		//section detection
		if (line.toUpperCase().contains("CATEGORY LIST")) {
		    section = Section.CATEGORIES;
		    continue;
		}
		if (line.toUpperCase().contains("RENTAL EQUIPMENT")) {
		    section = Section.EQUIPMENT;
		    continue;
		}
		if (line.toUpperCase().contains("CUSTOMER INFORMATION")) {
		    section = Section.CUSTOMERS;
		    continue;
		}
		if (line.toUpperCase().contains("RENTAL INFORMATION")) {
		    section = Section.RENTALS;
		    continue;
		}
		
		
		// Skip header rows that contain column names
		if (line.contains("equipment_id") ||
			line.contains("customer_id") ||
			line.contains("rental_id")) {
		    continue;
		}
		
		String[] pLine = line.split(",");
		
		// Data parsing based on current section
		switch (section) {
			
			case EQUIPMENT:
			    
			    int equipId = Integer.valueOf(pLine[0]);
			    int catId = Integer.valueOf(pLine[1]);
			    String name = pLine[2];
			    String desc = pLine[3];
			    double dRate = Double.valueOf(pLine[4]);
			    
			    data.getEquipData().add(new Equipment(equipId, catId, name, desc, dRate));
			    
			    break;
			    
			case CUSTOMERS:
			    
			    int cusId = Integer.valueOf(pLine[0]);
			    String lName = pLine[1];
			    String fName = pLine[2];
			    String phone = pLine[3];
			    String eMail = pLine[4];
			    
			    data.getAccountData().add(new Account(cusId, lName, fName, phone, eMail));
			    
			    break;
			
			case RENTALS:
			    
			    int rentId = Integer.valueOf(pLine[0]);
			    Date curDate = Date.valueOf(pLine[1]);
			    int cusId2 = Integer.valueOf(pLine[2]);
			    int equipId2 = Integer.valueOf(pLine[3]);
			    Date rentDate = Date.valueOf(pLine[4]);
			    Date returnDate = Date.valueOf(pLine[5]);
			    double cost = Double.valueOf(pLine[6]);
			    
			    data.getRentalData().add(new Rental(rentId, curDate, cusId2, equipId2, rentDate, returnDate, cost));
			    
			    break;
			
			default:
			    break;
		}
	    }
	    
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	
	return data;
    }
    
    /**
     * Writes data from a DataArrays object to the CSV file.
     * Formats the output with section headers, column headers, and data rows
     * in the expected structure for later reading by csvReader().
     * 
     * Note: Categories are currently hardcoded and not read from the DataArrays object.
     *
     * @param data the DataArrays object containing equipment, customer, and rental data
     * @return {@code false} always (return value appears to be unused in current implementation)
     */
    public static boolean csvWriter(DataArrays data) {
	
	try (PrintWriter pw = new PrintWriter(FILEPATH)) {
	    
	    StringBuilder sb = new StringBuilder();
	    
	    // Write categories section header
	    sb.append("Category List\n");
	    
	    // Write categories data (hardcoded values)
	    sb.append("category id,name\n");
	    sb.append("10,Power Tools\n");
	    sb.append("20,Yard Equipment\n");
	    sb.append("30,Compressors\n");
	    sb.append("40,Generators\n");
	    sb.append("50,AirTools\n");
	    
	    // Write equipment section
	    sb.append("Rental Equipment\n");
	    sb.append("equipment_id,category_id,name,description,daily_rate\n");
	    
	    for (Equipment e : data.getEquipData()) {
		int equipId = e.getEquipmentId();
		int catId = e.getCategoryId();
		String name = e.getName();
		String desc = e.getDesc();
		double cost = e.getDailyRentCost();
		
		sb.append(equipId + "," + catId + "," + name + "," + desc + "," + cost + "\n");
	    }
	    
	    // Write customer section
	    sb.append("Customer Information\n");
	    sb.append("customer_id,last_name,first_name,contact_phone,e-mail\n");
	    
	    for (Account a : data.getAccountData()) {
		int accountId = a.getAccountId();
		String lName = a.getLastName();
		String fName = a.getFirstName();
		String phone = a.getPhoneNumber();
		String email = a.getEmail();
		
		sb.append(accountId + "," + lName + "," + fName + "," + phone + "," + email + "\n");
	    }
	    
	    // Write rentals section
	    sb.append("Rental Information\n");
	    sb.append("rental_id,date,customer_id,equipment_id,rental_date,return_date,cost\n");
	    
	    for (Rental r : data.getRentalData()) {
		int rentId = r.getId();
		Date curDate = (Date) r.getCurDate();
		int cusId = r.getCusId();
		int equipId = r.getEquipId();
		Date rentDate = (Date) r.getRentalDate();
		Date returnDate = (Date) r.getReturnDate();
		double cost = r.getRentalCost();
		
		sb.append(rentId + "," + curDate + "," + cusId + "," + equipId + "," + rentDate + "," + returnDate + "," + cost + "\n");
	    }
	    
	    pw.write(sb.toString());
	    
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	    return false;
	}
	
	return false;
    }
}
