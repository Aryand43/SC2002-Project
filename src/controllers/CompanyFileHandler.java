package controllers;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import models.User;

public class CompanyFileHandler extends FileHandler{
	private String filePath = "assets/testcases/sample_company_representative_list.csv";
	private String pendingRepsFile = "assets/testcases/pending_rep_account.csv";
	private String line;
	private String[] rowData;
	private ArrayList<User> userList = new ArrayList<>();
	private ArrayList<User> pendingList = new ArrayList<>();

	
	public ArrayList<User> readFromFile() {
		try(Scanner sc = new Scanner(new File(filePath))){
			while(sc.hasNextLine()) {
				line = sc.nextLine();
				rowData = line.split(",");
				User user = new User(rowData[0], rowData[1], rowData[5]);
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
			pw.println("CompanyRepID,Company Rep Name,Password");
			
			for(User u: userList) {
				pw.println(u.getID()+"," + u.getUserName() + "," + u.getPassword());
			}
		}
		catch (FileNotFoundException e) {
			e.getMessage();
		}
	}
	//public void writeToPendingRepsFile(ArrayList<User> pendingList) {
		//this.clearRowData();
		//try(PrintWriter pw = new PrintWriter(pendingRepsFile)){
			//pw.println("Name,Company,Position,Email");
			
			//for(User u: pendingList) {
				//pw.println(u.getUserName()+"," + u.getCompany() + "," + u.getPosition() + "," + u.getEmail());
			//}
		//}
		//catch (FileNotFoundException e) {
			//e.getMessage();
		//}
	//}
	
	private void clearRowData() {
		for(int i = 0; i<rowData.length; i++) {
			rowData[i] = null;
		}
	}
}