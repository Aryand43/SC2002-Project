package managers;

import controllers.ApplicationSerializer;
import controllers.FileHandler;
import java.util.ArrayList;
import java.util.List;
import models.Application;
import models.Internship;
import models.Student;

//Minimal ApplicationManager implementation to satisfy ReportGenerator usage.
public class ApplicationManager {
    private List<Application> applicationList;
    private InternshipManager internshipManager;
    private FileHandler<Application> fileHandler;

    public ApplicationManager() {
        this.applicationList = new ArrayList<>();
    }

    public ApplicationManager(InternshipManager internshipManager) {
        this();
        this.internshipManager = internshipManager;
        ApplicationSerializer serializer = new ApplicationSerializer();
        this.fileHandler = new FileHandler<>(serializer);
        this.applicationList = fileHandler.readFromFile();
    }

    public void saveApplicationsToFile() {
        fileHandler.writeToFile(new ArrayList<>(applicationList));
    }

    // Return current applications
    public List<Application> getApplicationList() {
        return new ArrayList<>(applicationList);
    }

    // Apply for an internship
    public Application apply(Student student, Internship opportunity) {
        Application app = new Application(student, opportunity);
        applicationList.add(app);
        return app;
    }

    // Accept a placement for a student and application
    public boolean acceptPlacement(Student student, Application application) {
        if (application == null) return false;
        application.setStatus(Application.ApplicationStatus.SUCCESSFUL);
        // If internship manager available, update listing confirmation
        if (internshipManager != null && application.getInternship() != null) {
            internshipManager.updateListingOnConfirmation(application.getInternship().getInternshipID());
        }
        return true;
    }

    // Student requests withdrawal (mark requested)
    public boolean requestWithdrawal(Application application) {
        if (application == null) return false;
        application.setStatus(Application.ApplicationStatus.WITHDRAW_REQUESTED);
        return true;
    }

    // Approve withdrawal (finalize)
    public boolean approveWithdrawal(Application application) {
        if (application == null) return false;
        application.setStatus(Application.ApplicationStatus.WITHDRAWN);
        if (internshipManager != null && application.getInternship() != null) {
            internshipManager.updateListingOnWithdrawal(application.getInternship().getInternshipID());
        }
        return true;
    }

    // Reject withdrawal request
    public boolean rejectWithdrawal(Application application) {
        if (application == null) return false;
        application.setStatus(Application.ApplicationStatus.SUCCESSFUL);
        // TODO: Ensure we track original state if more states are added later
        return true;
    }

    // Helpers used elsewhere in project: accept/reject by application ID
    public boolean approveStudentWithdrawal(String applicationID) {
        Application app = findByID(applicationID);
        return approveWithdrawal(app);
    }

    public boolean rejectStudentWithdrawal(String applicationID) {
        Application app = findByID(applicationID);
        return rejectWithdrawal(app);
    }

    public Application findByID(String id) {
        for (Application a : applicationList) {
            if (a.getID().equals(id)) return a;
        }
        return null;
    }
}
