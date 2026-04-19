package models;

public class Equipment {

    private int equipmentId;
    private int categoryId;
    private String name;
    private String desc;
    private double dailyRentCost;
    
    
    public Equipment(int equipmentId, int categoryId, String name, String desc, double dailyRentCost) {
	
	this.equipmentId = equipmentId;
	this.categoryId = categoryId;
	this.name = name;
	this.desc = desc;
	this.dailyRentCost = dailyRentCost;
    }
    
    @Override
    public String toString() {
	return "Equipment [equipmentId=" + equipmentId + ", category=" + categoryId + ", name=" + name + ", desc=" + desc
		+ ", dailyRentCost=" + dailyRentCost + "]";
    }

    public int getEquipmentId() {
        return equipmentId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public double getDailyRentCost() {
        return dailyRentCost;
    }

    public void setEquipmentId(int equipmentId) {
        this.equipmentId = equipmentId;
    }

    public void setCategory(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setDailyRentCost(double dailyRentCost) {
        this.dailyRentCost = dailyRentCost;
    }
    
    
}
