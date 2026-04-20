package managers;

import java.util.ArrayList;

import models.Customer;
import models.Equipment;
import models.Rental;

public class ReportManager {
	
	public ReportManager() {}
	
	public String buildEquipmentReport(ArrayList<Equipment> equipmentList) {
		String string = "";
		for(int i = 0; i < equipmentList.size(); i++) {
			Equipment equipment = equipmentList.get(i);
			string = string + equipment.getName();
			if(i < equipmentList.size()) {
				string = string + "\n";
			}
		}
		return string;
	}
	
	public String buildAccountReport(ArrayList<Customer> customerList) {
		String string = "";
		for(int i = 0; i < customerList.size(); i++) {
			Customer customer = customerList.get(i);
			string = string + customer.getFirstName() + " " + customer.getLastName();
			if(i < customerList.size()) {
				string = string + "\n";
			}
		}
		return string;
	}
	
	public String buildRentalReport(ArrayList<Rental> rentalList) {
		String string = "";
		for(int i = 0; i < rentalList.size(); i++) {
			Rental rental = rentalList.get(i);
			string = string + rental.getEquipId();
			if(i < rentalList.size()) {
				string = string + "\n";
			}
		}
		return string;
	}
}
