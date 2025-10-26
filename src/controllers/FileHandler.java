package controllers;
import  java.util.ArrayList;
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
