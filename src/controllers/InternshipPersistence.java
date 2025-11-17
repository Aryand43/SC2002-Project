package controllers;

import models.Internship;
import java.util.List;

/**
 * Abstraction for internship persistence layer.
 * Decouples InternshipManager from FileHandler implementation.
 * Allows for multiple persistence strategies (file, database, cloud, etc.)
 */
public interface InternshipPersistence {
    /**
     * Load all internships from persistent storage
     * @return List of all internships
     */
    List<Internship> load();

    /**
     * Save internships to persistent storage
     * @param internships List of internships to persist
     */
    void save(List<Internship> internships);
}
