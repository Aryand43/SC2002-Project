package controllers;

import java.util.List;

/**
 * Abstraction for persistence layer.
 * Decouples Manager from FileHandler implementation.
 */
public interface DataPersistence<T>{
    /**
     * Load data into program from persistent storage
     * @return List of all internships
     */
    List<T> load();

    /**
     * Save data to persistent storage
     * @param internships List of internships to persist
     */
    void save(List<T> internships);
}
