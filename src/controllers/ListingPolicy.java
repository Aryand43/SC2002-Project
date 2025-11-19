package controllers;

/**
 * Encapsulates requirements for creating internship listings.<br>
 * Open/Closed Principle: New policies can be added without modifying existing code.
 */
public interface ListingPolicy {
    /**
     * Validate if a company representative can create a new listing
     * @param companyRepId The representative ID
     * @param currentListingCount Current number of listings for this rep
     * @return true if listing can be created, false otherwise
     */
    boolean canCreateListing(String companyRepId, int currentListingCount);

    /**
     * Validate the number of available slots for an internship
     * @param slots Number of slots requested
     * @return true if slot count is valid, false otherwise
     */
    boolean isValidSlotCount(int slots);

    /**
     * Get the max allowed listings per representative
     * @return maximum number of listings allowed
     */
    int getMaxListingsPerRep();

    /**
     * Get the minimum allowed slots per listing
     * @return minimum number of slots
     */
    int getMinSlots();

    /**
     * Get the maximum allowed slots per listing
     * @return maximum number of slots
     */
    int getMaxSlots();
}
