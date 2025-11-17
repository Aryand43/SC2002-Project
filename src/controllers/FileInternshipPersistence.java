package controllers;

import models.Internship;
import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper around FileHandler to implement InternshipPersistence interface.
 * Decouples InternshipManager from FileHandler specifics.
 */
public class FileInternshipPersistence implements InternshipPersistence {
    private FileHandler<Internship> fileHandler;

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
