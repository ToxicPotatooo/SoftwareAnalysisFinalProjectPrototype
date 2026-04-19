package models;

public class Equipment {

    private int equipmentId;
    private String category;
    private String name;
    private String description;
    private double dailyRentCost;

    public Equipment(int equipmentId, String category, String name, String description, double dailyRentCost) {
        this.equipmentId = equipmentId;
        this.category = category;
        this.name = name;
        this.description = description;
        this.dailyRentCost = dailyRentCost;
    }

    // Getters and Setters
    public int getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(int equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getDailyRentCost() {
        return dailyRentCost;
    }

    public void setDailyRentCost(double dailyRentCost) {
        this.dailyRentCost = dailyRentCost;
    }
}