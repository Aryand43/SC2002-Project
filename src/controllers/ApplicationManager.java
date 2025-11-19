package controllers;

import java.util.List;
import models.*;

/**
 * ApplicationManager manages application workflows using FSM.
 * The Application model enforces valid state transitions via StateTransition enum.
 * <br><br>
 * Valid state transitions (defined in Application.StateTransition):
 * - ACCEPT: PENDING to SUCCESSFUL <br>
 * - REJECT_APPLICATION: PENDING to UNSUCCESSFUL <br>
 * - REQUEST_WITHDRAWAL: SUCCESSFUL to WITHDRAW_REQUESTED <br>
 * - APPROVE_WITHDRAWAL: WITHDRAW_REQUESTED to WITHDRAWN <br>
 * - REJECT_WITHDRAWAL: WITHDRAW_REQUESTED to SUCCESSFUL <br>
 */
public class ApplicationManager {
    private ApplicationRepository repository;
    private InternshipManager internshipManager;
    private UserManager userManager;

    public ApplicationManager() {
        // empty manager for testing or manual wiring
    }

    public ApplicationManager(InternshipManager internshipManager) {
        this.internshipManager = internshipManager;
        DataPersistence<Application> persistence = new FileApplicationPersistence();
        this.repository = new ApplicationRepository(persistence);
    }

    public ApplicationManager(InternshipManager internshipManager, UserManager userManager) {
        this.internshipManager = internshipManager;
        this.userManager = userManager;
        DataPersistence<Application> persistence = new FileApplicationPersistence();
        this.repository = new ApplicationRepository(persistence);
        // After loading applications, resolve references to Student and Internship objects
        resolveObjectReferences();
    }
    
    /**
     * Returns the list of all applications.
     * @return List of all applications, or empty list if repository is null
     */
    public List<Application> getApplicationList() {
        return repository != null ? repository.getAll() : List.of();
    }

    /**
     * Creates a new application for a student to apply for an internship.
     * @param student The student applying
     * @param opportunity The internship opportunity
     * @return The newly created Application
     */
    public Application apply(Student student, Internship opportunity) {
        Application app = new Application(student.getID(), opportunity.getInternshipID());
        app.setStudent(student);
        app.setInternship(opportunity);
        if (repository != null) repository.add(app);
        return app;
    }

    /**
     * Accepts a placement for a student and application. <br>
     * The necessary details in internship listing is then updated with InternshipManager.
     * @param student The student accepting placement
     * @param application The application being accepted
     * @return true if transition succeeded, false otherwise
     */
    public boolean acceptPlacement(Student student, Application application) {
        if (application == null) return false;
        boolean transitioned = application.accept();
        if (!transitioned) return false;
        if (repository != null) repository.update(application);
        // If internship manager available, update listing confirmation
        if (internshipManager != null && application.getInternshipID() != null) {
            internshipManager.updateListingOnConfirmation(application.getInternshipID());
        }
        return true;
    }

    /**
     * Rejects an application (state transition: PENDING to UNSUCCESSFUL).
     * @param application The application to reject
     * @return true if transition succeeded, false otherwise
     */
    public boolean rejectApplication(Application application) {
        if (application == null) return false;
        boolean transitioned = application.reject();
        if (!transitioned) return false;
        if (repository != null) repository.update(application);
        return true;
    }

    /**
     * Student requests withdrawal from a successful application.<br>
     * State transition: SUCCESSFUL to WITHDRAW_REQUESTED.
     * @param application The application to request withdrawal for
     * @return true if transition succeeded, false otherwise
     */
    public boolean requestWithdrawal(Application application) {
        if (application == null) return false;
        boolean transitioned = application.requestWithdrawal();
        if (!transitioned) return false;
        if (repository != null) repository.update(application);
        return true;
    }

    /**
     * Approves a withdrawal request.<br>
     * State transition: WITHDRAW_REQUESTED to WITHDRAWN.<br>
     * Updates internship slot availability.
     * @param application The application with withdrawal request to approve
     * @return true if transition succeeded, false otherwise
     */
    public boolean approveWithdrawal(Application application) {
        if (application == null) return false;
        boolean transitioned = application.approveWithdrawal();
        if (!transitioned) return false;
        if (repository != null) repository.update(application);
        if (internshipManager != null && application.getInternshipID() != null) {
            internshipManager.updateListingOnWithdrawal(application.getInternshipID());
        }
        return true;
    }

    /**
     * Rejects a withdrawal request.<br>
     * State transition: WITHDRAW_REQUESTED back to SUCCESSFUL.
     * @param application The application with withdrawal request to reject
     * @return true if transition succeeded, false otherwise
     */
    public boolean rejectWithdrawal(Application application) {
        if (application == null) return false;
        boolean transitioned = application.rejectWithdrawal();
        if (!transitioned) return false;
        if (repository != null) repository.update(application);
        return true;
    }

    /**
     * Approves a student withdrawal request by application ID.
     * @param applicationID The ID of the application
     * @return true if withdrawal was approved, false otherwise
     */
    public boolean approveStudentWithdrawal(String applicationID) {
        Application app = findByID(applicationID);
        if (app == null) {
            System.out.println("Error: Application with ID '" + applicationID + "' not found.");
            return false;
        }
        return approveWithdrawal(app);
    }

    /**
     * Rejects a student withdrawal request by application ID.
     * @param applicationID The ID of the application
     * @return true if rejection was successful, false otherwise
     */
    public boolean rejectStudentWithdrawal(String applicationID) {
        Application app = findByID(applicationID);
        if (app == null) {
            System.out.println("Error: Application with ID '" + applicationID + "' not found.");
            return false;
        }
        return rejectWithdrawal(app);
    }

    public Application findByID(String id) {
        return repository != null ? repository.findById(id) : null;
    }

    private void resolveObjectReferences() {
        if (userManager == null || repository == null) {
            return;
        }
        for (Application app : repository.getAll()) {
            app.setStudentRef((Student) userManager.getStudentByID(app.getStudentID()));
            app.setInternship(internshipManager.findInternshipByID(app.getInternshipID()));
        }
    }
    
    public void saveTofile() {
        if (repository != null) repository.save();
    }
}
