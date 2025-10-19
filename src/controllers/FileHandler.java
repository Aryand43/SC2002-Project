package controllers;
import  models.User;
import java.util.ArrayList;


public abstract class FileHandler {
	public abstract ArrayList<User> readFromFile();
	public abstract void writeToFile(ArrayList<User> userList);
}
