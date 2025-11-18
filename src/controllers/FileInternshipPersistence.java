package controllers;

import java.util.ArrayList;
import java.util.List;
import models.Internship;

/**
 * Wrapper around FileHandler to implement InternshipPersistence interface.
 * Decouples InternshipManager from FileHandler specifics.
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
