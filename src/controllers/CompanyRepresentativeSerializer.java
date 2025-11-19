package controllers;

import models.CompanyRepresentative;
/**
 * Implements the serializing and deserializing of CompanyRepresentative entity and the relevant metadata for its file
 * <p>
 * 
 * Responsible for converting CompanyRepresentative objects to and from their string representation<br>
 * to the correct format to be written back into the file, as well as providing file related metadata such as filepath and header.<br><br>
 * CompanyRepresentative is the type of entity to be serialized and deserialized
 * 
 */
public class CompanyRepresentativeSerializer implements Serializer<CompanyRepresentative>{
	
	/**
	 * Deserializes the given string into the CompanyRepresentative by creating <br>
	 * a new instance of CompanyRepresentative by separting the line by commas into:<br>
	 * <ol>
	 * <li>CompanyRepresentativeID</li>
	 * <li>Name</li>
	 * <li>CompanyName</li>
	 * <li>Department</li>
	 * <li>Position</li>
	 * <li>Email</li>
	 * <li>Application status</li>
	 * </ol>
	 * @param line the line to deserialize
	 * @return the CompanyRepresentative Object reconstructed from the line of text
	 */
	public CompanyRepresentative deserialize(String line) {
		String[] rowData = line.split(",");
		Boolean status;
		if (rowData[6].trim().equalsIgnoreCase("null") || rowData[6].trim().isEmpty()) {
			status = null;
		} else {
			status = Boolean.valueOf(rowData[6]);
		}
		CompanyRepresentative cr;
		if (rowData.length > 7 && rowData[7] != null && !rowData[7].isEmpty()) {
		    // rowData[7] exists and has a value
		    cr = new CompanyRepresentative(
		            rowData[0], rowData[1], rowData[5], rowData[2], rowData[3],
		            rowData[4], status, rowData[7]
		    );
		} else {
		    // rowData[7] is missing or empty → use another function
			 cr = new CompanyRepresentative(
			            rowData[0], rowData[1], rowData[5], rowData[2], rowData[3],
			            rowData[4], status);
		}
		return cr;
	}
	

	/**
	 * Serializes the CompanyRepresentative into a string of information to be stored into the corresponding file<br>
	 * by retrieving the CompanyRepresentativeID, Name, CompanyName, Department, Position,Email and application status<br>
	 * of the CompanyRepresentative<br>
	 * 
	 * @param cr the CompanyRepresentative to serialize
	 * @return a string that is comma delimited for csv file
	 */
	public String serialize(CompanyRepresentative cr) {
		String line = cr.getID() + "," + cr.getUserName() + "," + cr.getCompanyName() + "," + cr.getDepartment() + "," + cr.getPosition() + "," + cr.getEmail() + "," + String.valueOf(cr.isApproved()) + "," + cr.getPassword();
		return line;
	}
	
	
	/**
	 * Obtains the header for the CSV file of CompanyRepresentative
	 * 
	 * @return the string for Header
	 */
	public String getHeader() {
		return "CompanyRepID,Name,CompanyName,Department,Position,Email,Status,Password";
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
