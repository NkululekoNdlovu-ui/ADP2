package za.ac.cput.timetableproject.domain;

public class Venue {
    private int venueId;
    private String description;

    public Venue() {
    }

    public Venue(int venueId, String description) {
        this.venueId = venueId;
        this.description = description;
    }

    public int getVenueId() {
        return venueId;
    }

    public String getDescription() {
        return description;
    }

    public void setVenueId(int venueId) {
        this.venueId = venueId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return  venueId + " " + description ;
    }
}
