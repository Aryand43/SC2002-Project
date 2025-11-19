package models;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Application {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    /*
     * Defines valid states for an application.
     */
    public enum ApplicationStatus {ACCEPTED, PENDING, SUCCESSFUL, UNSUCCESSFUL, WITHDRAWN, WITHDRAW_REQUESTED}
    
    /**
     * Defines valid state transitions for an application.
     * Used by FSM to enforce allowed transitions.
     */
    public enum StateTransition {
        // Acceptance flow
        ACCEPT(ApplicationStatus.PENDING, ApplicationStatus.SUCCESSFUL),
        REJECT_APPLICATION(ApplicationStatus.PENDING, ApplicationStatus.UNSUCCESSFUL),
        
        // Withdrawal flow
        REQUEST_WITHDRAWAL(ApplicationStatus.SUCCESSFUL, ApplicationStatus.WITHDRAW_REQUESTED),
        APPROVE_WITHDRAWAL(ApplicationStatus.WITHDRAW_REQUESTED, ApplicationStatus.WITHDRAWN),
        REJECT_WITHDRAWAL(ApplicationStatus.WITHDRAW_REQUESTED, ApplicationStatus.SUCCESSFUL);
        
        private final ApplicationStatus from;
        private final ApplicationStatus to;
        
        StateTransition(ApplicationStatus from, ApplicationStatus to) {
            this.from = from;
            this.to = to;
        }
        
        public ApplicationStatus getFromState() { return from; }
        public ApplicationStatus getToState() { return to; }
    }

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

    /**
     * Convenience constructor used by some serializers (id, studentId, internshipId, statusAsString)
     * @param id Application ID
     * @param studentID Student ID
     * @param internshipID Internship ID
     * @param status Current status of application
     * @param date Date of application
     */
    public Application(String id, String studentID, String internshipID, String status, String date) {
        this.id = id;
        this.studentID = studentID;
        this.internshipID = internshipID;
        try {
            this.status = ApplicationStatus.valueOf(status.toUpperCase());
        } catch (Exception e) {
            this.status = ApplicationStatus.PENDING;
        }
        this.appliedDate = date;
    }
    
    
    /**
     * Simple constructor for apply() when student and internship objects are available
     * @param studentID Student ID
     * @param internshipID Internship ID
     */
    public Application(String studentID, String internshipID) {
        this.id = "APP" + System.currentTimeMillis();
        this.studentID = studentID;
        this.internshipID = internshipID;
        this.status = ApplicationStatus.PENDING;
        LocalDate localDate = LocalDate.now();
        this.appliedDate = localDate.format(FORMATTER);
        this.studentRef = null; // Explicitly set to null
        this.internshipRef = null; // Explicitly set to null
    }

    public String getID() { return id; }
    public String getStudentID() { return studentID; }
    public String getInternshipID() { return internshipID; }
    public ApplicationStatus getStatus() { return status; }
    public String getAppliedDate() { return appliedDate; }

    public void setStatus(ApplicationStatus status) { this.status = status; }
    public void setStudentID(String s) { this.studentID = s; }
    public void setInternshipID(String i) { this.internshipID = i; }

    public Student getStudent(){return this.studentRef;}

    /**
     * Setter method for studentRef
     * @param studentRef The student reference ID to set
     */
    public void setStudent(Student studentRef) {
        this.studentRef = studentRef;
    }

    public Internship getInternship(){return this.internshipRef;}
    public void setInternship(Internship internshipRef) {
        this.internshipRef = internshipRef;
    }

    /**
     * Validates if the application state transition is allowed according to FSM rules.
     * @param transition The desired transition
     * @return true if the transition is valid from the current state
     */
    private boolean isTransitionAllowed(StateTransition transition) {
        return this.status == transition.getFromState();
    }
    
    /**
     * Performs the application state transition if allowed by FSM.
     * @param transition The desired transition
     * @return true if transition succeeded, false if transition is not allowed
     */
    private boolean performTransition(StateTransition transition) {
        if (!isTransitionAllowed(transition)) {
            return false;
        }
        this.status = transition.getToState();
        return true;
    }

    /**
     * Accept this application (state machine transition: PENDING to SUCCESSFUL)
     * @return true if transition succeeded
     */
    public boolean accept() {
        return performTransition(StateTransition.ACCEPT);
    }

    /**
     * Reject this application (state machine transition: PENDING to UNSUCCESSFUL)
     * @return true if transition succeeded
     */
    public boolean reject() {
        return performTransition(StateTransition.REJECT_APPLICATION);
    }

    /**
     * Student requests withdrawal (state machine transition: SUCCESSFUL to WITHDRAW_REQUESTED)
     * @return true if transition succeeded
     */
    public boolean requestWithdrawal() {
        return performTransition(StateTransition.REQUEST_WITHDRAWAL);
    }

    /**
     * Approve a pending withdrawal (state machine transition: WITHDRAW_REQUESTED to WITHDRAWN)
     * @return true if transition succeeded
     */
    public boolean approveWithdrawal() {
        return performTransition(StateTransition.APPROVE_WITHDRAWAL);
    }

    /**
     * Reject a withdrawal request (state machine transition: WITHDRAW_REQUESTED to SUCCESSFUL)
     * @return true if transition succeeded
     */
    public boolean rejectWithdrawal() {
        return performTransition(StateTransition.REJECT_WITHDRAWAL);
    }

    public void setStudentRef(Student studentRef) {
        this.studentRef = studentRef;
    }
    
    @Override
    public String toString() {
        String internInfo = (internshipID == null) ? "<unknown internship>" : internshipID;
        String studentInfo = (studentID == null) ? "<unknown student>" : studentID;
        return String.format("Application[id=%s, student=%s, internship=%s, status=%s, date=%s]",
                id, studentInfo, internInfo, status, appliedDate);
    }
}
