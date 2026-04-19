package ui;

import utils.DataArrays;
import utils.XlsxReader;

import models.*;

public class MainTest {

    public static void main(String[] args) {
	
	// 📦 Read Excel file
        DataArrays data = XlsxReader.xlsxReadFile();

        // 🧾 Print each category
        for (Equipment e : data.getEquipData()) {
            System.out.println(e);
        }
    }

}
