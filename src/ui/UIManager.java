package ui;

import utils.DataArrays;
import utils.XlsxReader;

public class UIManager {

    public String filePath;
    
    public UIManager() {
	DataArrays xlsxData = XlsxReader.xlsxReadFile();
    }
}
