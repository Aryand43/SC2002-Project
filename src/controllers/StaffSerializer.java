package controllers;

import models.CareerCenterStaff;

public class StaffSerializer {
	public CareerCenterStaff serialize(String line) {
		String[] rowData = line.split(",");
		CareerCenterStaff staff = new CareerCenterStaff(rowData[0],rowData[1],rowData[2],rowData[3],rowData[4]);
		return staff;
	}
	
	public String deserialize(CareerCenterStaff staff) {
		String line = staff.getID() + "," + staff.getName() + "," + staff.getRole() +"," + staff.getDepartment() +"," + staff.getEmail();
		return line;
	}
	
	public String getHeader() {
		return "StaffID,Name,Role,Department,Email";
	}
	
	public String getFilePath() {
		return  "assets/testcases/sample_staff_list.csv";
	}
}
