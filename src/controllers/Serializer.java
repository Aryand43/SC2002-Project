package controllers;

/**
 * Interface that defines the  contract for serializing and deserialzing entities of type {@code T}
 * <p>
 * 
 * A {@code Serializer} is responsible for converting entity objects to and from their string representation
 * to the correct format to be written back into the file, as well as providing file related metadata such as 
 * filepath and header
 * @param <T> the type of entity to be serialized and deserialized
 */

public interface Serializer<T> {
	
	
	/**
	 * Serializes the given entity into a string of information to be stored into the corresponding file
	 * 
	 * @param entity the entity to serialize
	 * @return a string that is comma delimited for csv file
	 */
	public String serialize(T entity);
	
	
	/**
	 * Deserializes the given string into the corresponding entity by creating a new instance of the entity using
	 * the given information from the string that was read from the file
	 * 
	 * @param line the line to deserialize
	 * @return the entity reconstructed from the line of text
	 */
	public T deserialize(String line);
	
	
	/**
	 * Obtains the filepath for the corresponding entity class
	 * 
	 * @return the string for filepath
	 */
	public String getFilePath();
	
	/**
	 * Obtains the header for the corresponding entity class
	 * 
	 * @return the string that is comma delimited for the corresponding file
	 */
	public String getHeader();
}
