package managers;

import models.Equipment;
import utils.CsvHandler;
import utils.DataArrays;
import java.util.ArrayList;

public class EquipmentManager {
    
    private ArrayList<Equipment> listOfEquipment;
    private DataArrays data;
    
    public EquipmentManager(DataArrays data) {
        this.data = data;
        this.listOfEquipment = data.getEquipData();
        if (this.listOfEquipment == null) {
            this.listOfEquipment = new ArrayList<>();
        }
    }
    
    private void saveData() {
        data.setEquipData(listOfEquipment);
        CsvHandler.csvWriter(data);
    }
    
    public void addEquipment(Equipment equipment) {
        for (Equipment e : listOfEquipment) {
            if (e.getEquipmentId() == equipment.getEquipmentId()) {
                throw new IllegalArgumentException("Equipment ID already exists.");
            }
        }
        listOfEquipment.add(equipment);
        saveData();
    }
    
    public void createNewEquipment(Equipment equipment) {
        addEquipment(equipment);
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
    
    public ArrayList<Equipment> getListOfEquipment() {
        return listOfEquipment;
    }
    
    public ArrayList<Equipment> getAllEquipment() {
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
    
    public Equipment findEquipmentById(int id) {
        return getEquipmentById(id);
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
    
    public int getNextEquipmentId() {
        return getNextId();
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
    
    public boolean reportEquipmentAsDamaged(int equipmentId) {
        return deleteEquipment(equipmentId);
    }
}