package controllers;

import java.util.ArrayList;
import java.util.List;
import models.Application;

/**
 * Repository for applications.
 * Handles actions like delete/edit/add and queries for application.
 * Single Responsibility: Data access and retrieval only.
 */
public class ApplicationRepository {
    private ApplicationPersistence persistence;
    private List<Application> applications;

    public ApplicationRepository(ApplicationPersistence persistence) {
        this.persistence = persistence;
        List<Application> loaded = persistence.load();
        this.applications = (loaded != null) ? new ArrayList<>(loaded) : new ArrayList<>();
    }

    public List<Application> getAll() {
        return new ArrayList<>(applications);
    }

    public Application findById(String id) {
        if (id == null) return null;
        for (Application a : applications) {
            if (id.equals(a.getID())) return a;
        }
        return null;
    }

    public void add(Application app) {
        if (app == null) return;
        applications.add(app);
    }

    public boolean update(Application app) {
        if (app == null || app.getID() == null) return false;
        for (int i = 0; i < applications.size(); i++) {
            if (applications.get(i).getID().equals(app.getID())) {
                applications.set(i, app);
                return true;
            }
        }
        return false;
    }

    public boolean delete(String id) {
        Application a = findById(id);
        if (a == null) return false;
        return applications.remove(a);
    }

    public void save() {
        persistence.save(new ArrayList<>(applications));
    }
}
