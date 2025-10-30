package controllers;

import models.Student;

public class StudentSerializer implements Serializer<Student> {
	public Student serialize(String line) {
		String[] rowData = line.split(",");
		Student student = new Student(rowData[0],rowData[1],rowData[2],rowData[3]);
		return student;
	}
	
	public String deserialize(Student student) {
		String line = student.getID() + "," + student.getUserName() + "," + student.getMajor() + "," + student.getYearOfStudy() +"," + student.getEmail();
		return line;
	}
	
	public String getHeader() {
		return "StudentID,Name,Major,Year,Email";
	}
	
	public String getFilePath() {
		return  "assets/testcases/sample_student_list.csv";
	}

}
