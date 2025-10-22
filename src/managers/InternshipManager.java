package managers;
import controllers.FileHandler;
import java.util.List;
import models.Internship;
import models.Internship.InternshipStatus;


public class InternshipManager {
    private List<Internship> internshipList;
    private FileHandler fileHandler;
    private static final String INTERNSHIP_FILE = "assets/testcases/internships.csv";
    private static final int MAX_INTERNSHIPS_PER_REP = 5;


    /**
     * Find internship by ID
     */
    public Internship findInternshipByID(String internshipId) {
        return internshipList.stream()
            .filter(i -> i.getInternshipID().equals(internshipId))
            .findFirst()
            .orElse(null);
    }
    /**
     * Approve the internship (Career Center Staff action)
     */
    public void updateInternshipStatus(String internshipID, String update) {
        Internship i = findInternshipByID(internshipID);
        if (i.getStatus() == InternshipStatus.PENDING && update.equalsIgnoreCase("APPROVED")) {
            i.setStatus(InternshipStatus.APPROVED);
        }
    }

}