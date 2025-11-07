package models;

import java.time.LocalDate;

public class Application {
    public enum ApplicationStatus {PENDING, SUCCESSFUL, UNSUCCESSFUL, WITHDRAWN, WITHDRAW_REQUESTED}

    private String id;
    private String studentID; 
    private String internshipID; 
    private ApplicationStatus status;
    private final String appliedDate; 

    private Student studentRef;
    private Internship internshipRef;

    /**
     * Class Constructor
     * @param studentID ID of student
     * @param internshipID ID of internship
     * @param appliedDate Date of application
     * @param studentRef Student object
     * @param internshipRef Internship object
     * @param status Current status of application
     */
    public Application(String studentID, String internshipID, String appliedDate, Student studentRef, Internship internshipRef, ApplicationStatus status) {
        this.id = "APP" + System.currentTimeMillis();
        this.studentID = studentID;
        this.internshipID = internshipID;
        this.appliedDate = appliedDate;
        this.studentRef = studentRef;
        this.internshipRef = internshipRef;
        this.status = status;
    }

    // Convenience constructor used by some serializers (id, studentId, internshipId, statusAsString)
    public Application(String id, String studentID, String internshipID, String status) {
        this.id = id;
        this.studentID = studentID;
        this.internshipID = internshipID;
        try {
            this.status = ApplicationStatus.valueOf(status.toUpperCase());
        } catch (Exception e) {
            this.status = ApplicationStatus.PENDING;
        }
        this.appliedDate = LocalDate.now().toString();
    }

    // Simple constructor for apply() when student and internship objects are available
    public Application(String studentID, String internshipID) {
        this.id = "APP" + System.currentTimeMillis();
        this.studentID = studentID;
        this.internshipID = internshipID;
        this.status = ApplicationStatus.PENDING;
        this.appliedDate = LocalDate.now().toString();
        this.studentRef = null; // Explicitly set to null
        this.internshipRef = null; // Explicitly set to null
    }

    public String getID() { return id; }
    public String getStudentID() { return studentID; }
    public String getInternshipID() { return internshipID; }
    public Student getStudent() { return this.studentRef;}
    public ApplicationStatus getStatus() { return status; }
    public String getAppliedDate() { return appliedDate; }

    public void setStatus(ApplicationStatus status) { this.status = status; }
    public void setStudentID(String s) { this.studentID = s; }
    public void setInternshipID(String i) { this.internshipID = i; }

    public Student getStudentRef(){return this.studentRef;}

    /**
     * Setter method for studentRef
     * @param studentRef The student reference ID to set
     */
    public void setStudentRef(Student studentRef) {
        this.studentRef = studentRef;
    }

    public Internship getInternshipRef(){return this.internshipRef;}
    public void setInternshipRef(Internship internshipRef) {
        this.internshipRef = internshipRef;
    }

    @Override
    public String toString() {
        String internInfo = (internshipID == null) ? "<unknown internship>" : internshipID;
        String studentInfo = (studentID == null) ? "<unknown student>" : studentID;
        return String.format("Application[id=%s, student=%s, internship=%s, status=%s, date=%s]",
                id, studentInfo, internInfo, status, appliedDate);
    }
}
