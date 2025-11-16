package controllers;

import models.Student;

/**
 * Implements the serializing and deserializing of student entity and the relevant metadata for its file
 * <p>
 * 
 * Responsible for converting Student objects to and from their string representation
 * to the correct format to be written back into the file, as well as providing file related metadata such as 
 * filepath and header
 * @param Student the type of entity to be serialized and deserialized
 * 
 */
public class StudentSerializer implements Serializer<Student> {
	
	/**
	 * Deserializes the given string into the Student Object by creating a new instance of Student
	 * by separting the line by commas into StudentID, Name , Major, Year and Email
	 * 
	 * @param line the line to deserialize
	 * @return the Student reconstructed from the line of text
	 */
	@Override
	public Student deserialize(String line) {
    String[] rowData = line.split(",");
    if (rowData.length < 5) {
        throw new IllegalArgumentException("Invalid student line (expected 5 columns): " + line);
    }

    String id    = rowData[0].trim();
    String name  = rowData[1].trim();
    String major = rowData[2].trim();
    int year;
    try {
        year = Integer.parseInt(rowData[3].trim());
    } catch (NumberFormatException e) {
        throw new IllegalArgumentException("Invalid year value for student: " + rowData[3], e);
    }
    String email = rowData[4].trim();
    String password = rowData[5].trim();
	System.out.println("current data: " + name);
    return new Student(id, name, major, year, email, password);
}

	/**
	 * Serializes the Student into a string of information to be stored into the corresponding file
	 * by retrieving the StudentID, Name , Major, Year and Email of the Student
	 * 
	 * @param Student the student object to serialize
	 * @return a string that is comma delimited for csv file
	 */
	@Override
	public String serialize(Student student) {
		String line = student.getID() + "," + student.getUserName() + "," + student.getMajor() + "," + student.getYearOfStudy() +"," + student.getEmail() + "," +student.getPassword();
		return line;
	}
	
	
	/**
	 * Obtains the header for the CSV file of Student
	 * 
	 * @return the string for Header
	 */
	@Override
	public String getHeader() {
		return "StudentID,Name,Major,Year,Email";
	}
	
	
	/**
	 * Obtains the filepath for the Student
	 * 
	 * @return the string for filepath
	 */
	@Override
	public String getFilePath() {
		return  "assets/testcases/sample_student_list.csv";
	}

}

