package controllers;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import models.User;

/**
 * <b>FileHandler CLASS</b><br>
 * In charge of the logic behind interaction with CSV file .
 * <br><br>
 * Responsibilities include:
 * <ul>
 * <li>Reading from a file</li>
 * <li>Writing To a file</li>
 * </ul>
 * 
 * Abstract class and requires to be inherited by relevant classes for more specific functions.
 * 
 * @author KaiQiang
 */
public abstract class FileHandler {
	public abstract ArrayList<User> readFromFile();
	public abstract void writeToFile(ArrayList<User> userList);
}


public class FileHandler<T>{
	private Serializer<T> serializer;
	private String filePath;
	
	public FileHandler(Serializer<T> serializer){
		this.serializer = serializer;
		this.filePath = serializer.getFilePath();
	}
	
	public ArrayList<T> readFromFile() {
		try(Scanner sc = new Scanner(new File(filePath))){
			if(sc.hasNextLine()) {
				sc.nextLine();
			}
			while(sc.hasNextLine()) {
				String line = sc.nextLine();
				T returnedEntity = serializer.deserialize();
				ArrayList<T> arrayList = new ArrayList<>();
				arrayList.add(returnedEntity);
			};
			
		}
		catch (FileNotFoundException e) {
			e.getMessage();
		}
		return arrayList;
	}
	
	public void writeToFile(ArrayList<T> arrayList) {
		try(PrintWriter pw = new PrintWriter(filePath)){
			pw.println(serializer.getHeader());
			
			for(entities u: arrayList) {
				pw.println(serializer.serialize(u));
			}
		}
		catch (FileNotFoundException e) {
			e.getMessage();
		}
	}
}