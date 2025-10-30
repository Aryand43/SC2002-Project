package controllers;

public interface Serializer<T> {
	public String serialize(T entity);
	public T deserialize(String line);
	public String getFilePath();
	public String getHeader();
}
