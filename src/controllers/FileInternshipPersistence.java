package controllers;

import java.util.ArrayList;
import java.util.List;
import models.Internship;

/**
 * Concrete implementation from DataPersistence. @see DataPersistence for more information on methods
 * Decouples Manager from FileHandler implementation.
 */
public class FileInternshipPersistence implements DataPersistence<Internship> {
    private final FileHandler<Internship> fileHandler;

    public FileInternshipPersistence() {
        InternshipSerializer serializer = new InternshipSerializer();
        this.fileHandler = new FileHandler<>(serializer);
    }

    @Override
    public List<Internship> load() {
        return fileHandler.readFromFile();
    }

    @Override
    public void save(List<Internship> internships) {
        fileHandler.writeToFile(new ArrayList<>(internships));
    }
}
