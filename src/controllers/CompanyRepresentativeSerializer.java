package controllers;

import models.CompanyRepresentative;

public class CompanyRepresentativeSerializer implements Serialzer<CompanyReprsentative>{
	public CompanyRepresentative serialize(String line) {
		String[] rowData = line.split(",");
		CompanyRepresentative cr = new CompanyRepresentative(rowData[0],rowData[1],rowData[2],rowData[3],rowData[4],rowData[5]);
		return cr;
	}
	
	public String deserialize(CompanyRepresentative cr) {
		String line = cr.getID() + "," + cr.getName() + "," + cr.getCompanyName() + "," + cr.getDepartment() + "," + cr.getPosition() + ", cr.getEmail() + "," + cr.getStatus();
		return line;
	}
	
	public String getHeader() {
		return "CompanyRepID,Name,CompanyName,Department,Position,Email,Status";
	}
	
	public String getFilePath() {
		return  "assets/testcases/sample_company_representative_list.csv";
	}
}
