package controllers;

import java.util.ArrayList;

public class ReportGenerator {

	public void generateReportByStatus(String status) {
		private ArrayList<InternshipOpportunity> InternshipList = new ArrayList<>();
		InternshipList = InternshipFileHandler.readFromFile();
		System.out.println("InternshipID Status Company");
		for(Internship i: InternshipList) {
			if(i.getStatus().equals(status)) {
				System.println(i.getId() + i.getStatus() + i.getCompany());
			}
		}
	}
}
