package models; 
import java.util.ArrayList; 

public class CompanyRepresentative extends User {
    private String companyName;
    private String department; 
    private String position;
    private boolean approved; 
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
        this.approved = false;
        this.listings = new ArrayList<>();        
    }

    /*
    * Getters & Setters
    */
    public String getCompanyName() { return companyName; }
    public String getDepartment() { return department; }
    public String getPosition() { return position; }
    public boolean isApproved() { return approved; }
    public void setApproved(boolean status) { this.approved = status; }
    public ArrayList<> getListings() { return listings; }
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
        app.setStatus("Successful");
        System.out.println("Application " + app.getID() + " approved.");
    }
    
    public void rejectApplication(Application app) {
        app.setStatus("Unsuccessful");
        System.out.println("Application " + app.getID() + " rejected.");
    }
}