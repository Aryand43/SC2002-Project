package controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import models.Internship;
import models.Internship.InternshipLevel;
import models.Internship.InternshipStatus;

/**
 * Implements the serializing and deserializing of Internship  entity and the relevant metadata for its file
 * <p>
 * 
 * Responsible for converting Internship objects to and from their string representation
 * to the correct format to be written back into the file, as well as providing file related metadata such as 
 * filepath and header.<br><br>
 * Internship is the type of entity to be serialized and deserialized
 * 
 */
public class InternshipSerializer implements Serializer<Internship>{

	// Use dd/MM/yyyy to match CSV date format
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	/**
	 * Deserializes the given string into the Internship by creating a <br>
	 * new instance of Internship by separating the line by commas into:<br>
	 * <ol>
	 * <li>ListingID</li>
	 * <li>Title</li>
	 * <li>Description</li>
	 * <li>Level</li>
	 * <li>PreferredMajor</li>
	 * <li>OpeningDate</li>
	 * <li>ClosingDate</li>
	 * <li>Status</li>
	 * <li>CompanyName</li>
	 * <li>CompanyRepId</li>
	 * <li>TotalSlots</li>
	 * <li>AvailableSlots</li>
	 * <li>ConfirmedSlots</li>
	 * <li>Visible</li>
	 * </ol>
	 * 
	 * @param line the line to deserialize
	 * @return the Internship Object reconstructed from the line of text
	 */
	@Override
	public Internship deserialize(String line) {
		String[] rowData = line.split(",");
		Internship internship = new Internship(rowData[0],rowData[1],rowData[2],InternshipLevel.valueOf(rowData[3]),rowData[4],LocalDate.parse(rowData[5], FORMATTER),LocalDate.parse(rowData[6], FORMATTER),InternshipStatus.valueOf(rowData[7]),rowData[8], rowData[9], Integer.parseInt(rowData[10]), Integer.parseInt(rowData[11]), Integer.parseInt(rowData[12]), Boolean.parseBoolean(rowData[13]) );
		return internship;
	}
	
	/**
	 * Serializes the Internship Object into a string of information to be stored into the corresponding file
	 * by retrieving the 
	 * 
	 * @param internship the Internship to serialize
	 * @return a string that is comma delimited for csv file
	 */
	@Override
	public String serialize(Internship internship) {
		String line = internship.getInternshipID() + "," + internship.getTitle() + "," + internship.getDescription() + "," + internship.getLevel().toString() + "," + internship.getPreferredMajor() + "," + FORMATTER.format(internship.getOpeningDate()) + "," + FORMATTER.format(internship.getClosingDate())
				+ "," + internship.getStatus().toString() + "," + internship.getCompanyName() + "," + internship.getCompanyRepId() + "," + String.valueOf(internship.getTotalSlots()) + "," + String.valueOf(internship.getAvailableSlots()) + "," + String.valueOf(internship.getConfirmedSlots()) + ","
				+ String.valueOf(internship.isVisible());
		return line;
	}
	
	
	/**
	 * Obtains the header for the CSV file of Internship
	 * 
	 * @return the string for Header
	 */
	@Override
	public String getHeader() {
		return "";
	}
	
	
	/**
	 * Obtains the filepath for the Internship
	 * 
	 * @return the string for filepath
	 */
	@Override
	public String getFilePath() {
		return  "assets/testcases/internship_list.csv";
	}
}
