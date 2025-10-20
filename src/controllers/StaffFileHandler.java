package controllers;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import models.CareerCenterStaff;

public class StaffFileHandler extends FileHandler<CareerCenterStaff>{
	/**
     * Reads staff list from CSV and automatically registers them
     * as CareerCenterStaff objects.
     */
    @Override
	private String filePath = "assets/testcases/sample_staff_list.csv";
	private String line;
	private String[] rowData;
	private ArrayList<CareerCenterStaff> staffList = new ArrayList<>();
	
	public ArrayList<CareerCenterStaff> readFromFile() {
		try(Scanner sc = new Scanner(new File(filePath))){
			while(sc.hasNextLine()) {
				line = sc.nextLine();
				rowData = line.split(",");
				CareerCenterStaff staff = new CareerCenterStaff(rowData[0], rowData[1], rowData[4], rowData[3]);
				staffList.add(staff);
				System.out.println(rowData[0] + " " + rowData[1]);
			}
			
		}
		catch (FileNotFoundException e) {
			e.getMessage();
		}
		return staffList;
	}
	
	public void writeToFile(ArrayList<CareerCenterStaff> staffList) {
		this.clearRowData();
		try(PrintWriter pw = new PrintWriter(filePath)){
			pw.println("StaffID,Name,Password");
			
			for(CareerCenterStaff staff : staffList) {
				pw.println(staff.getID()+"," + staff.getUserName() + "," + staff.getPassword());
			}
		}
		catch (FileNotFoundException e) {
			e.getMessage();
		}
	}
	
	private void clearRowData() {
		for(int i = 0; i<rowData.length; i++) {
			rowData[i] = null;
		}
	}
}
