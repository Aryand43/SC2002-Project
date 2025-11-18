package models; 
import java.util.ArrayList;
import models.Internship;
import models.Application.ApplicationStatus; 

public class CompanyRepresentative extends User {
    private String companyName;
    private String department; 
    private String position;
    private Boolean approved; 
    private ArrayList<Internship> listings;

    /*
    * Constructor 
    */

    public CompanyRepresentative(String ID, String name, String email, String companyName, String department, String position){
        super(ID, name, email);
        this.companyName = companyName;
        this.department = department;
        this.position = position;
        this.userType = TypesOfUser.CompanyRepresentative;
        this.approved = null;
        this.listings = new ArrayList<>();        
    }
    
    /**
     * Constructor for Serializer where it takes extra parameter of status & password
     * @param ID Company Rep ID
     * @param name Company Rep Name
     * @param email Company Rep Email (To Be used as loginID)
     * @param companyName Company Name
     * @param department Which department the Company Rep belogns to
     * @param position Which position the Company Representative holds
     * @param status Status of the account creation
     * @param password Password of the Company Representative
     */
    public CompanyRepresentative(String ID, String name, String email, String companyName, String department, String position, Boolean status, String password){
        super(ID, name, email,password);
        this.companyName = companyName;
        this.department = department;
        this.position = position;
        this.userType = TypesOfUser.CompanyRepresentative;
        this.approved = status;
        this.listings = new ArrayList<>();        
    }
    
    /**
     * Alternate version of Serializer for company representative where the password is non existent yet
     * @param ID Company Rep ID
     * @param name Company Rep Name
     * @param email Company Rep Email (To Be used as loginID)
     * @param companyName Company Name
     * @param department Which department the Company Rep belogns to
     * @param position Which position the Company Representative holds
     * @param status Status of the account creatio
     */
    public CompanyRepresentative(String ID, String name, String email, String companyName, String department, String position, Boolean status){
        super(ID, name, email);
        this.companyName = companyName;
        this.department = department;
        this.position = position;
        this.userType = TypesOfUser.CompanyRepresentative;
        this.approved = status;
        this.listings = new ArrayList<>();        
    }

    /*
    * Getters & Setters
    */
    public String getCompanyName() { return companyName; }
    public String getDepartment() { return department; }
    public String getPosition() { return position; }
    public Boolean isApproved() { return approved; }
    public void setApproved(Boolean status) { this.approved = status; }
    public ArrayList<Internship> getListings() { return listings; }
    public boolean createInternshipListing(Internship io) {
        if (listings.size() >= 5) {
            System.out.println("You can only post up to 5 internships.");
            return false;
        }
        listings.add(io);
        System.out.println("Internship created: " + io.getTitle());
        return true;
    }
    public static CompanyRepresentative registerRep(String userName, String companyName, String department, String position, String email) {
        String ID = "CR" + Math.abs((userName + System.currentTimeMillis()).hashCode());
        CompanyRepresentative newRep = new CompanyRepresentative(ID, userName, email, companyName, department, position);
        System.out.println("Registration request created for: " + userName);
        return newRep;
    }
    public void approveApplication(Application app) {
        app.setStatus(ApplicationStatus.SUCCESSFUL);
        System.out.println("Application " + app.getID() + " approved.");
    }
    
    public void rejectApplication(Application app) {
        app.setStatus(ApplicationStatus.UNSUCCESSFUL);
        System.out.println("Application " + app.getID() + " rejected.");
    }
}