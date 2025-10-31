package controllers;

import models.CareerCenterStaff;
/**
 * Implements the serializing and deserializing of staff entity and the relevant metadata for its file
 * <p>
 * 
 * Responsible for converting CareerCenterStaff objects to and from their string representation
 * to the correct format to be written back into the file, as well as providing file related metadata such as 
 * filepath and header
 * @param CareerCenterStaff the type of entity to be serialized and deserialized
 * 
 */
public class StaffSerializer implements Serializer<CareerCenterStaff>{
	
	
	/**
	 * Deserializes the given string into the CareerCenterStaff by creating a new instance of CareerCenterStaff 
	 * by separting the line by commas into StaffID, Name, Role, Department & Email
	 * 
	 * @param line the line to deserialize
	 * @return the CareerCenterStaff reconstructed from the line of text
	 */
	public CareerCenterStaff deserialize(String line) {
		String[] rowData = line.split(",");
		CareerCenterStaff staff = new CareerCenterStaff(rowData[0],rowData[1],rowData[2],rowData[3],rowData[4]);
		return staff;
	}
	
	
	/**
	 * Serializes the CareerCenterStaff into a string of information to be stored into the corresponding file
	 * by retrieving the ID, Name, Role, Department and Email of the CareerCenterStaff
	 * 
	 * @param staff the CareerCenterStaff to serialize
	 * @return a string that is comma delimited for csv file
	 */
	public String serialize(CareerCenterStaff staff) {
		String line = staff.getID() + "," + staff.getName() + "," + staff.getRole() +"," + staff.getDepartment() +"," + staff.getEmail();
		return line;
	}
	
	
	/**
	 * Obtains the header for the CSV file of CareerCenterStaff
	 * 
	 * @return the string for Header
	 */
	public String getHeader() {
		return "StaffID,Name,Role,Department,Email";
	}
	
	
	/**
	 * Obtains the filepath for the CareerCenterStaff
	 * 
	 * @return the string for filepath
	 */
	public String getFilePath() {
		return  "assets/testcases/sample_staff_list.csv";
	}
}
