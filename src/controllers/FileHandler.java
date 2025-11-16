package controllers;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles reading and writing serialized entities to a file. 
 * <p>
 * This class uses a {@link Serializer} to convert entities
 * to and from string representations
 * 
 * @param <T> the type of entity handled by this FileHandler
 * @author KaiQiang
 */
public class FileHandler<T>{
	private Serializer<T> serializer;
	private String filePath;
	
	
	/**
	 * Creates a new instance of FileHandler using the given serializer that is specific to the entity
	 * 
	 * @param serializer the serializer used for converting entities
	 */
	public FileHandler(Serializer<T> serializer){
		this.serializer = serializer;
		this.filePath = serializer.getFilePath();
	}
	
	
	/**
	 * Read line by line from a file, ignoring the header line.
	 * 
	 * @return an array list of entities after it has been deserialised from the file
	 * @throws FileNotFoundException if the file does not exist
	 */
	public ArrayList<T> readFromFile() {
		ArrayList<T> arrayList = new ArrayList<>();
		try(Scanner sc = new Scanner(new File(filePath))){
			// Skip header line if present
			if(sc.hasNextLine()) {
				sc.nextLine();
			}
			while(sc.hasNextLine()) {
				String line = sc.nextLine();
				// skip blank/empty lines to avoid malformed deserialization
				if (line == null || line.trim().isEmpty()) continue;
				T returnedEntity = serializer.deserialize(line);
				if (returnedEntity != null) arrayList.add(returnedEntity);
			}
		}
		catch (FileNotFoundException e) {
			e.getMessage();
		}
		return arrayList;
	}
	
	
	/**
	 * Writes line by line to the same filepath including the header
	 * 
	 * 
	 * @param arrayList takes in an array list of the entity to be written back to the file including
	 * @throws FileNotFoundException if the file does not exist
	 */
	public void writeToFile(ArrayList<T> arrayList) {
		try(PrintWriter pw = new PrintWriter(filePath)){
			pw.println(serializer.getHeader());
			
			for(T u: arrayList) {
				pw.println(serializer.serialize(u));
			}
		}
		catch (FileNotFoundException e) {
			e.getMessage();
		}
	}

	
}