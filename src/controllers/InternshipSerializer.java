package controllers;

import models.Internship;

public class InternshipSerializer implements Serialzer<Internship>{
	public Internship serialize(String line) {
		String[] rowData = line.split(",");
		Internship internship = new Internship(rowData[0],rowData[1],rowData[2],rowData[3],rowData[4]);
		return internship;
	}
	
	public String deserialize(Internship internship) {
		String line =
		return line;
	}
	
	public String getHeader() {
		return "";
	}
	
	public String getFilePath() {
		return  "assets/testcases/Internship_list.csv";
	}
}
