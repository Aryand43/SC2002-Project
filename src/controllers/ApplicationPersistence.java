package controllers;

import java.util.List;
import models.Application;

/*
 * Function for persisting Application data
 */
public interface ApplicationPersistence {
    List<Application> load();
    void save(List<Application> applications);
}
