package utils;

import java.io.*;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import models.*;

enum Section {
    NONE,
    CATEGORIES,
    EQUIPMENT,
    CUSTOMERS,
    RENTALS
}

public class XlsxReader {


    static String FILEPATH = "res/data-samples.xlsx";

    // =========================
    // ===== READ METHOD =======
    // =========================
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
                if (firstCell == null) continue;

                String cellContents = getString(firstCell);

                // ===== SECTION DETECTION =====
                if (cellContents.equalsIgnoreCase("CATEGORY LIST")) {
                    section = Section.CATEGORIES;
                    continue;
                } else if (cellContents.equalsIgnoreCase("RENTAL EQUIPMENT")) {
                    section = Section.EQUIPMENT;
                    continue;
                } else if (cellContents.equalsIgnoreCase("CUSTOMER INFORMATION")) {
                    section = Section.CUSTOMERS;
                    continue;
                } else if (cellContents.equalsIgnoreCase("RENTAL INFORMATION")) {
                    section = Section.RENTALS;
                    continue;
                }

                // skip headers
                if (cellContents.equalsIgnoreCase("id")) continue;

                // ===== DATA PARSING =====
                switch (section) {

                    case EQUIPMENT:
                        int equipId = getInt(row.getCell(0));
                        int cateId = getInt(row.getCell(1));
                        String name = getString(row.getCell(2));
                        String desc = getString(row.getCell(3));
                        double rentRate = getDouble(row.getCell(4));

                        data.getEquipData().add(
                                new Equipment(equipId, cateId, name, desc, rentRate)
                        );
                        break;

                    case CUSTOMERS:
                        int cusId = getInt(row.getCell(0));
                        String lName = getString(row.getCell(1));
                        String fName = getString(row.getCell(2));
                        String phone = getString(row.getCell(3));
                        String email = getString(row.getCell(4));

                        data.getAccountData().add(
                                new Account(cusId, lName, fName, phone, email)
                        );
                        break;

                    case RENTALS:
                        int rentId = getInt(row.getCell(0));
                        Date date = getDate(row.getCell(1));
                        int cusId2 = getInt(row.getCell(2));
                        int equipId2 = getInt(row.getCell(3));
                        Date rentDate = getDate(row.getCell(4));
                        Date returnDate = getDate(row.getCell(5));
                        double cost = getDouble(row.getCell(6));

                        data.getRentalData().add(
                                new Rental(rentId, date, cusId2, equipId2, rentDate, returnDate, cost)
                        );
                        break;

                    default:
                        break;
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("Could not locate file: " + FILEPATH);
        } catch (IOException e) {
            System.out.println("IO error while reading file");
        }

        return data;
    }

    // =========================
    // ===== WRITE METHOD ======
    // =========================
    public static boolean xlsxWriteToFile(DataArrays data) {

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {

            XSSFSheet sheet = workbook.createSheet("Details");

            Map<String, Object[]> lines = new TreeMap<>();

            // ===== CATEGORY SECTION =====
            lines.put("1", new Object[]{"CATEGORY LIST"});
            lines.put("2", new Object[]{"category_id", "name"});
            lines.put("3", new Object[]{10, "Power Tools"});
            lines.put("4", new Object[]{20, "Yard Equipment"});
            lines.put("5", new Object[]{30, "Compressors"});
            lines.put("6", new Object[]{40, "Generators"});
            lines.put("7", new Object[]{50, "Air Tools"});

            int rowIndex = 10;

            // ===== EQUIPMENT =====
            lines.put(String.valueOf(rowIndex++), new Object[]{"RENTAL EQUIPMENT"});
            lines.put(String.valueOf(rowIndex++),
                    new Object[]{"equipment_id", "category_id", "name", "description", "daily_rate"});

            for (Equipment e : data.getEquipData()) {
                lines.put(String.valueOf(rowIndex++),
                        new Object[]{
                                e.getEquipmentId(),
                                e.getCategoryId(),
                                e.getName(),
                                e.getDesc(),
                                e.getDailyRentCost()
                        });
            }

            // ===== CUSTOMERS =====
            lines.put(String.valueOf(rowIndex++), new Object[]{"CUSTOMER INFORMATION"});
            lines.put(String.valueOf(rowIndex++),
                    new Object[]{"customer_id", "last_name", "first_name", "phone", "email"});

            for (Account a : data.getAccountData()) {
                lines.put(String.valueOf(rowIndex++),
                        new Object[]{
                                a.getAccountId(),
                                a.getLastName(),
                                a.getFirstName(),
                                a.getPhoneNumber(),
                                a.getEmail()
                        });
            }

            // ===== RENTALS =====
            lines.put(String.valueOf(rowIndex++), new Object[]{"RENTAL INFORMATION"});
            lines.put(String.valueOf(rowIndex++),
                    new Object[]{"rental_id", "date", "customer_id", "equipment_id", "rent_date", "return_date", "cost"});

            for (Rental r : data.getRentalData()) {
                lines.put(String.valueOf(rowIndex++),
                        new Object[]{
                                r.getId(),
                                r.getCurDate(),
                                r.getCusId(),
                                r.getEquipId(),
                                r.getRentalDate(),
                                r.getReturnDate(),
                                r.getRentalCost()
                        });
            }

            // ===== WRITE TO SHEET =====
            int rownum = 0;
            for (String key : lines.keySet()) {
                Row row = sheet.createRow(rownum++);
                Object[] objArr = lines.get(key);

                int cellnum = 0;
                for (Object obj : objArr) {
                    Cell cell = row.createCell(cellnum++);

                    if (obj instanceof String)
                        cell.setCellValue((String) obj);
                    else if (obj instanceof Integer)
                        cell.setCellValue((Integer) obj);
                    else if (obj instanceof Double)
                        cell.setCellValue((Double) obj);
                    else if (obj instanceof Date)
                        cell.setCellValue((Date) obj);
                }
            }

            // ✅ WRITE FILE
            try (FileOutputStream out = new FileOutputStream("res/output.xlsx")) {
                workbook.write(out);
            }

            return true;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    // =========================
    // ===== HELPER METHODS ====
    // =========================
    private static String getString(Cell cell) {
        if (cell == null) return "";
	return cell.toString().trim();
    }

    private static int getInt(Cell cell) {
        if (cell == null) return 0;
        return (int) cell.getNumericCellValue();
    }

    private static double getDouble(Cell cell) {
        if (cell == null) return 0.0;
        return cell.getNumericCellValue();
    }

    private static Date getDate(Cell cell) {
        if (cell == null) return null;
        return cell.getDateCellValue();
    }
}
