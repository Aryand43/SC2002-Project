package controllers;
import  models.User;
import java.util.ArrayList;


public abstract class FileHandler<T> {
	public abstract ArrayList<T> readFromFile();
	public abstract void writeToFile(ArrayList<T> dataList);
}
