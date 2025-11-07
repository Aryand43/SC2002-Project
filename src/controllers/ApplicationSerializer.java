package controllers;

import models.Application;
import controllers.UserManager;
import controllers.InternshipManager;
/**
 * Implements the serializing and deserializing of Application entity and the relevant metadata for its file
 * <p>
 * 
 * Responsible for converting Application objects to and from their string representation
 * to the correct format to be written back into the file, as well as providing file related metadata such as 
 * filepath and header
 * @param Application the type of entity to be serialized and deserialized
 * 
 */
public class ApplicationSerializer implements Serializer<Application>{

    private UserManager userManager;
    private InternshipManager internshipManager;

    public ApplicationSerializer() {}

    public ApplicationSerializer(UserManager userManager, InternshipManager internshipManager) {
        this.userManager = userManager;
        this.internshipManager = internshipManager;
    }

	/**
	 * Deserializes the given string into the Application by creating a new instance of Application
	 * by separting the line by commas into 
	 * 
	 * @param line the line to deserialize
	 * @return the Application Object reconstructed from the line of text
	 */
	public Application deserialize(String line) {
		String[] rowData = line.split(",");
		Application application = new Application(rowData[0],rowData[1],rowData[2],rowData[3]);
		if (userManager != null) {
		    application.setStudentRef(userManager.getStudentByID(application.getStudentID()));
		}
		if (internshipManager != null) {
		    application.setInternshipRef(internshipManager.findInternshipByID(application.getInternshipID()));
		}
		return application;
	}
	
	
	/**
	 * Serializes the Application into a string of information to be stored into the corresponding file
	 * by retrieving the 
	 * 
	 * @param application the Application to serialize
	 * @return a string that is comma delimited for csv file
	 */
	public String serialize(Application application) {
		String line = application.getID();
		return line;
	}
	
	
	
	/**
	 * Obtains the header for the CSV file of Application
	 * 
	 * @return the string for Header
	 */
	public String getHeader() {
		return "";
	}
	
	
	/**
	 * Obtains the filepath for the Application
	 * 
	 * @return the string for filepath
	 */
	public String getFilePath() {
		return  "assets/testcases/application_list.csv";
	}

}