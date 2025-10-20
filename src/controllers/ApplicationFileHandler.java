package controllers;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import models.User;

public class ApplicationFileHandler extends FileHandler{
	private String filePath = "assets/testcases/Application_list.csv";
	private String line;
	private String[] rowData;
	private ArrayList<Application> ApplicationList = new ArrayList<>();
	
	public ArrayList<Application> readFromFile() {
		try(Scanner sc = new Scanner(new File(filePath))){
			while(sc.hasNextLine()) {
				line = sc.nextLine();
				rowData = line.split(",");
				Application application = new Application(//get relevant data);
				ApplicationList.add(application);
			}
			
		}
		catch (FileNotFoundException e) {
			e.getMessage();
		}
		return ApplicationList;
	}
	
	public void writeToFile(ArrayList<Application> ApplicationList) {
		this.clearRowData();
		try(PrintWriter pw = new PrintWriter(filePath)){
			pw.println("ApplicationID,Role,NameOfStudent");
			
			for(Application a: ApplicationList) {
				pw.println(a.getID()+"," + a.Role() + "," + a.getName());
			}
		}
		catch (FileNotFoundException e) {
			e.getMessage();
		}
	}
	
	public void AddToFile(Application a) {
		try (PrintWriter pw = new PrintWriter(new FileWriter(filePath, true))){
			pw.println(a.getID()+"," + a.Role() + "," + a.getName());
			
		}
		catch (FileNotFoundException e) {
			e.getMessage();
		}
	}
	
	public void RemoveFromFile(String id) {
		ApplicationList = this.readFromFile();
		for(int i = 0; i < ApplicationList.size(); i++}{
			if(ApplicationList[i].getID().equals(id)) {
				ApplicationList.remove(i);
			}
		}
		this.writeToFile(ApplicationList);
}
	
	private void clearRowData() {
		for(int i = 0; i<rowData.length; i++) {
			rowData[i] = null;
		}
	}
}
