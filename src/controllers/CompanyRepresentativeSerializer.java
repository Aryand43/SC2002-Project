package controllers;

import models.CompanyRepresentative;
/**
 * Implements the serializing and deserializing of CompanyRepresentative entity and the relevant metadata for its file
 * <p>
 * 
 * Responsible for converting CompanyRepresentative objects to and from their string representation
 * to the correct format to be written back into the file, as well as providing file related metadata such as 
 * filepath and header
 * @param CompanyRepresentative the type of entity to be serialized and deserialized
 * 
 */
public class CompanyRepresentativeSerializer implements Serializer<CompanyRepresentative>{
	
	/**
	 * Deserializes the given string into the CompanyRepresentative by creating a new instance of CompanyRepresentative
	 * by separting the line by commas into CompanyRepresentativeID, Name, CompanyName, Department, Position,
	 * Email and application status 
	 * 
	 * @param line the line to deserialize
	 * @return the CompanyRepresentative Object reconstructed from the line of text
	 */
	public CompanyRepresentative deserialize(String line) {
		String[] rowData = line.split(",");
		CompanyRepresentative cr = new CompanyRepresentative(rowData[0],rowData[1],rowData[2],rowData[3],rowData[4],rowData[5]);
		return cr;
	}
	

	/**
	 * Serializes the CompanyRepresentative into a string of information to be stored into the corresponding file
	 * by retrieving the CompanyRepresentativeID, Name, CompanyName, Department, Position,Email and application status 
	 * of the CompanyRepresentative
	 * 
	 * @param cr the CompanyRepresentative to serialize
	 * @return a string that is comma delimited for csv file
	 */
	public String serialize(CompanyRepresentative cr) {
		String line = cr.getID() + "," + cr.getName() + "," + cr.getCompanyName() + "," + cr.getDepartment() + "," + cr.getPosition() + ", cr.getEmail() + "," + cr.getStatus();
		return line;
	}
	
	
	/**
	 * Obtains the header for the CSV file of CompanyRepresentative
	 * 
	 * @return the string for Header
	 */
	public String getHeader() {
		return "CompanyRepID,Name,CompanyName,Department,Position,Email,Status";
	}
	
	
	/**
	 * Obtains the filepath for the CompanyRepresentative
	 * 
	 * @return the string for filepath
	 */
	public String getFilePath() {
		return  "assets/testcases/sample_company_representative_list.csv";
	}
}
