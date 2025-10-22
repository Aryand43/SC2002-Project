package controllers;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import models.Internship;
import models.User;
import java.io.FileWriter;

public class InternshipFileHandler extends FileHandler{
	private String filePath = "assets/testcases/internship_list.csv";
	private String line;
	private String[] rowData;
	private ArrayList<Internship> InternshipList = new ArrayList<>();
	
	public ArrayList<Internship> readFromFile() {
		try(Scanner sc = new Scanner(new File(filePath))){
			while(sc.hasNextLine()) {
				line = sc.nextLine();
				rowData = line.split(",");
				Internship Internship = new Internship(//GetRelevantDataHere);
				InternshipList.add(Internship);
			}
			
		}
		catch (FileNotFoundException e) {
			e.getMessage();
		}
		return InternshipList;
	}
	
	public void writeToFile(ArrayList<Internship> InternshipList) {
		this.clearRowData();
		try(PrintWriter pw = new PrintWriter(filePath)){
			pw.println("InternshipID,Company,Role");
			
			for(Internship i: InternshipList) {
				pw.println(i.getID()+"," + i.getCompany() + "," + i.getRole());
			}
		}
		catch (FileNotFoundException e) {
			e.getMessage();
		}
	}
	
	public void AddToFile(Internship i) {
		try (PrintWriter pw = new PrintWriter(new FileWriter(filePath, true))){
			pw.println(i.getID()+"," + i.getCompany() + "," + i.getRole())
			
		}
		catch (FileNotFoundException e) {
			e.getMessage();
		}
	}
	
	public void RemoveFromFile(String id) {
		InternshipList = this.readFromFile();
		for(int i = 0; i < InternshipList.size(); i++}{
			if(InternshipList[i].getID().equals(id)) {
				InternshipList.remove(i);
			}
		this.writeToFile(InternshipList);
	}
	
	private void clearRowData() {
		for(int i = 0; i<rowData.length; i++) {
			rowData[i] = null;
		}
	}
}
