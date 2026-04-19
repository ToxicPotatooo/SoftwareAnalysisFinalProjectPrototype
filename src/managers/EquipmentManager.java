package managers;

import models.Equipment;
import utils.CsvHandler;
import utils.DataArrays;
import java.util.ArrayList;

public class EquipmentManager {
    
    private ArrayList<Equipment> listOfEquipment;
    private DataArrays data;
    
    public EquipmentManager() {
        data = CsvHandler.csvReader();
        listOfEquipment = data.getEquipData();
    }
    
    private void saveData() {
        data.setEquipData(listOfEquipment);
        CsvHandler.csvWriter(data);
    }
    
    public boolean createNewEquipment(Equipment newEquipment) {
        for (Equipment e : listOfEquipment) {
            if (e.getEquipmentId() == newEquipment.getEquipmentId()) {
                return false;
            }
        }
        listOfEquipment.add(newEquipment);
        saveData();
        return true;
    }
    
    public boolean updateEquipment(int targetEquipmentID, Equipment updatedEquipment) {
        for (int i = 0; i < listOfEquipment.size(); i++) {
            if (listOfEquipment.get(i).getEquipmentId() == targetEquipmentID) {
                listOfEquipment.set(i, updatedEquipment);
                saveData();
                return true;
            }
        }
        return false;
    }
    
    public boolean deleteEquipment(int equipmentId) {
        for (int i = 0; i < listOfEquipment.size(); i++) {
            if (listOfEquipment.get(i).getEquipmentId() == equipmentId) {
                listOfEquipment.remove(i);
                saveData();
                return true;
            }
        }
        return false;
    }
    
    public boolean reportEquipmentAsDamaged(int equipmentId) {
        for (Equipment e : listOfEquipment) {
            if (e.getEquipmentId() == equipmentId) {
                return deleteEquipment(equipmentId);
            }
        }
        return false;
    }
    
    public ArrayList<Equipment> getListOfEquipment() {
        return listOfEquipment;
    }
    
    public Equipment getEquipmentById(int id) {
        for (Equipment e : listOfEquipment) {
            if (e.getEquipmentId() == id) {
                return e;
            }
        }
        return null;
    }
    
    public ArrayList<Equipment> getEquipmentByCategory(int categoryId) {
        ArrayList<Equipment> result = new ArrayList<>();
        for (Equipment e : listOfEquipment) {
            if (e.getCategoryId() == categoryId) {
                result.add(e);
            }
        }
        return result;
    }
    
    public int getNextId() {
        int maxId = 0;
        for (Equipment e : listOfEquipment) {
            if (e.getEquipmentId() > maxId) {
                maxId = e.getEquipmentId();
            }
        }
        return maxId + 1;
    }
}
