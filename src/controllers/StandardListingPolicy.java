package controllers;

/**
 * Standard implementation of ListingPolicy with the required limits by assignment briefing.<br>
 * Can be extended or replaced for different implementations.
 */
public class StandardListingPolicy implements ListingPolicy {
    private static final int MAX_LISTINGS_PER_REP = 5;
    private static final int MIN_SLOTS = 1;
    private static final int MAX_SLOTS = 10;

    @Override
    public boolean canCreateListing(String companyRepId, int currentListingCount) {
        return currentListingCount < MAX_LISTINGS_PER_REP;
    }

    @Override
    public boolean isValidSlotCount(int slots) {
        return slots >= MIN_SLOTS && slots <= MAX_SLOTS;
    }

    @Override
    public int getMaxListingsPerRep() {
        return MAX_LISTINGS_PER_REP;
    }

    @Override
    public int getMinSlots() {
        return MIN_SLOTS;
    }

    @Override
    public int getMaxSlots() {
        return MAX_SLOTS;
    }
}
