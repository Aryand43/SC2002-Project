package controllers;

import java.util.List;
import models.Application;


/**
 * Concrete implementation from DataPersistence. @see DataPersistence @see DataPersistence for more information on methods<br>
 * Decouples Manager from FileHandler implementation.
 */
public class FileApplicationPersistence implements DataPersistence<Application> {
    private FileHandler<Application> fileHandler;

    public FileApplicationPersistence() {
        ApplicationSerializer serializer = new ApplicationSerializer();
        this.fileHandler = new FileHandler<>(serializer);
    }

    @Override
    public List<Application> load() {
        return fileHandler.readFromFile();
    }

    @Override
    public void save(List<Application> applications) {
        fileHandler.writeToFile(new java.util.ArrayList<>(applications));
    }
}
