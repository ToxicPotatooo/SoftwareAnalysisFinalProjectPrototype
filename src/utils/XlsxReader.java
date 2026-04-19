package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.Date;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import models.*;

enum Section {
    NONE,
    EQUIPMENT,
    CUSTOMERS,
    RENTALS
}

public class XlsxReader {

    static String FILEPATH = "res/data-sample.xlsx";
    
    public static DataArrays xlsxReadFile() {
	
	DataArrays data = new DataArrays();
	
	Section section = Section.NONE;
	
	try (FileInputStream file = new FileInputStream(FILEPATH);
		XSSFWorkbook workbook = new XSSFWorkbook(file)) {
	    
	    
	    XSSFSheet sheet = workbook.getSheetAt(0);
	    
	    
	    for (int i = 0; i <= sheet.getLastRowNum(); i++) {
		
		Row row = sheet.getRow(i);
		if (row == null) continue;
		
		Cell firstCell = row.getCell(0);
		if(firstCell == null) continue;
		
		String cellContents = firstCell.toString();
		
		//section check/update
		if (cellContents.equalsIgnoreCase("RENTAL EQUIPMENT")) {
		    section = Section.EQUIPMENT;
		    continue;
		}
		if (cellContents.equalsIgnoreCase("CUSTOMER INFORMATION")) {
		    section = Section.CUSTOMERS;
		    continue;
		}
		if (cellContents.equalsIgnoreCase("RENTAL INFORMATION")) {
		    section = Section.RENTALS;
		    continue;
		}
		
		//header check
		if (cellContents.equalsIgnoreCase("id")) continue;
		
		switch (section) {
			
			case EQUIPMENT:
			    
			    int equipId = (int) row.getCell(0).getNumericCellValue();
			    int cateId = (int) row.getCell(1).getNumericCellValue();
			    String name = row.getCell(2).getStringCellValue();
			    String desc = row.getCell(3).getStringCellValue();
			    double rentRate = row.getCell(4).getNumericCellValue();
			    
			    data.getEquipData().add(new Equipment(equipId, cateId, name, desc, rentRate));
			    
			    break;
			    
			case CUSTOMERS:
			    
			    int cusId = (int) row.getCell(0).getNumericCellValue();
			    String lName = row.getCell(1).getStringCellValue();
			    String fName = row.getCell(2).getStringCellValue();
			    String phone = row.getCell(3).getStringCellValue();
			    String email = row.getCell(4).getStringCellValue();
			    
			    data.getAccountData().add(new Account(cusId, lName, fName, phone, email));
			    
			    break;
			    
			case RENTALS:
			    
			    int rentId = (int) row.getCell(0).getNumericCellValue();
			    Date date = row.getCell(1).getDateCellValue();
			    int cusId2 = (int) row.getCell(2).getNumericCellValue();
			    int equipId2 = (int) row.getCell(3).getNumericCellValue();
			    Date rentDate = row.getCell(4).getDateCellValue();
			    Date returnDate = row.getCell(5).getDateCellValue();
			    double cost = row.getCell(6).getNumericCellValue();
			    
			    data.getRentalData().add(new Rental(rentId, date, cusId2, equipId2, rentDate, returnDate, cost));
				    
			    break;
			    
			default:
			    break;
		}
	    }
	    
	} catch (FileNotFoundException e) {
	    System.out.println("xlsx file not found");
	} catch (IOException e) {
	    System.out.println("Cannot read file due to IO error");
	}
	
	return data;
    }
    
    public boolean xlsxWriteToFile() {
	
	try (FileInputStream file = new FileInputStream("data-samples.xlsx")) {
	    
	    //create new workbook
	    XSSFWorkbook workbook = new XSSFWorkbook(file);
	    
	    //create a sheet
	    XSSFSheet sheet = workbook.createSheet("Details");
	    
	    //data to write
	    
	    
	} catch (FileNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	
	return false;
    }
    
    
}
