package managers;

import models.Equipment;
import utils.CsvHandler;
import utils.DataArrays;
import java.util.ArrayList;

/**
 * Manages equipment inventory, including creation, deletion, retrieval, and category-based filtering.
 * This class handles all equipment-related business logic and persistence operations.
 */
public class EquipmentManager {
    
    private ArrayList<Equipment> listOfEquipment;
    private DataArrays data;
    
    /**
     * Constructs an EquipmentManager with the given data container.
     * Initializes the internal equipment list from the data source.
     *
     * @param data the data container holding equipment information
     */
    public EquipmentManager(DataArrays data) {
        this.data = data;
        this.listOfEquipment = data.getEquipData();
        if (this.listOfEquipment == null) {
            this.listOfEquipment = new ArrayList<>();
        }
    }
    
    /**
     * Persists the current equipment list to the CSV data source.
     */
    private void saveData() {
        data.setEquipData(listOfEquipment);
        CsvHandler.csvWriter(data);
    }
    
    /**
     * Adds a new piece of equipment to the inventory and persists the change.
     *
     * @param equipment the equipment object to add
     * @throws IllegalArgumentException if equipment with the same ID already exists
     */
    public void addEquipment(Equipment equipment) {
        for (Equipment e : listOfEquipment) {
            if (e.getEquipmentId() == equipment.getEquipmentId()) {
                throw new IllegalArgumentException("Equipment ID already exists.");
            }
        }
        listOfEquipment.add(equipment);
        saveData();
    }
    
    /**
     * Creates a new equipment entry. Synonym for {@link #addEquipment(Equipment)}.
     *
     * @param equipment the equipment object to create
     */
    public void createNewEquipment(Equipment equipment) {
        addEquipment(equipment);
    }
    
    /**
     * Deletes equipment from the inventory by its ID.
     *
     * @param equipmentId the ID of the equipment to delete
     * @return {@code true} if the equipment was found and deleted, {@code false} otherwise
     */
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
    
    /**
     * Returns the complete list of equipment.
     *
     * @return an ArrayList of all equipment
     */
    public ArrayList<Equipment> getListOfEquipment() {
        return listOfEquipment;
    }
    
    /**
     * Returns the complete list of equipment. Synonym for {@link #getListOfEquipment()}.
     *
     * @return an ArrayList of all equipment
     */
    public ArrayList<Equipment> getAllEquipment() {
        return listOfEquipment;
    }
    
    /**
     * Retrieves equipment by its unique ID.
     *
     * @param id the equipment ID to search for
     * @return the matching Equipment object, or {@code null} if not found
     */
    public Equipment getEquipmentById(int id) {
        for (Equipment e : listOfEquipment) {
            if (e.getEquipmentId() == id) {
                return e;
            }
        }
        return null;
    }
    
    /**
     * Finds equipment by its ID. Synonym for {@link #getEquipmentById(int)}.
     *
     * @param id the equipment ID to search for
     * @return the matching Equipment object, or {@code null} if not found
     */
    public Equipment findEquipmentById(int id) {
        return getEquipmentById(id);
    }
    
    /**
     * Calculates the next available equipment ID based on the maximum existing ID plus one.
     *
     * @return the next available equipment ID
     */
    public int getNextId() {
        int maxId = 0;
        for (Equipment e : listOfEquipment) {
            if (e.getEquipmentId() > maxId) {
                maxId = e.getEquipmentId();
            }
        }
        return maxId + 1;
    }
    
    /**
     * Calculates the next available equipment ID. Synonym for {@link #getNextId()}.
     *
     * @return the next available equipment ID
     */
    public int getNextEquipmentId() {
        return getNextId();
    }
    
    /**
     * Retrieves all equipment belonging to a specific category.
     *
     * @param categoryId the category ID to filter by
     * @return an ArrayList of equipment in the specified category (may be empty)
     */
    public ArrayList<Equipment> getEquipmentByCategory(int categoryId) {
        ArrayList<Equipment> result = new ArrayList<>();
        for (Equipment e : listOfEquipment) {
            if (e.getCategoryId() == categoryId) {
                result.add(e);
            }
        }
        return result;
    }
    
    /**
     * Reports equipment as damaged, which removes it from the active inventory.
     * Currently delegates to {@link #deleteEquipment(int)}.
     *
     * @param equipmentId the ID of the damaged equipment
     * @return {@code true} if the equipment was found and removed, {@code false} otherwise
     */
    public boolean reportEquipmentAsDamaged(int equipmentId) {
        return deleteEquipment(equipmentId);
    }
}
