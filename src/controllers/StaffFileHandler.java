package controllers;
import models.User;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.File;
import java.util.ArrayList;
import java.io.PrintWriter;

public class StaffFileHandler extends FileHandler{
	private String filePath = "assets/testcases/sample_staff_list.csv";
	private String line;
	private String[] rowData;
	private ArrayList<User> userList = new ArrayList<>();
	
	public ArrayList<User> readFromFile() {
		try(Scanner sc = new Scanner(new File(filePath))){
			while(sc.hasNextLine()) {
				line = sc.nextLine();
				rowData = line.split(",");
				User user = new User(rowData[0], rowData[1]);
				userList.add(user);
				System.out.println(rowData[0] + " " + rowData[1]);
			};
			
		}
		catch (FileNotFoundException e) {
			e.getMessage();
		}
		return userList;
	}
	
	public void writeToFile(ArrayList<User> userList) {
		this.clearRowData();
		try(PrintWriter pw = new PrintWriter(filePath)){
			pw.println("StaffID,Name,Password");
			
			for(User u: userList) {
				pw.println(u.getID()+"," + u.getUserName() + "," + u.getPassword());
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