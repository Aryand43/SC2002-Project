package controllers;

import models.Application;

public class ApplicationSerializer implements Serialzer<Application>{
	public Application serialize(String line) {
		String[] rowData = line.split(",");
		Application application = new Application(rowData[0],rowData[1],rowData[2],rowData[3]);
		return application;
	}
	
	public String deserialize(Application application) {
		String line = application.getID();
		return line;
	}
	
	public String getHeader() {
		return "";
	}
	
	public String getFilePath() {
		return  "assets/testcases/application_list.csv";
	}

}
