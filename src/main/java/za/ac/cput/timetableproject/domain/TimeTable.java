package za.ac.cput.timetableproject.domain;

public class TimeTable {
    private int timeTableId; // Auto-generated primary key
    private int lectureId;
    private int venueId;
    private int slotId;
    private int groupId;
    private int subjectCode; // Subject code
    private String type;

    public TimeTable() {
    }

    // Constructor without timeTableId
    public TimeTable(int lectureId, int venueId, int slotId, int groupId, int subjectCode, String type) {
        this.lectureId = lectureId;
        this.venueId = venueId;
        this.slotId = slotId;
        this.groupId = groupId;
        this.subjectCode = subjectCode;
        this.type = type;
    }

    // Getters and Setters
    public int getTimeTableId() {
        return timeTableId; // This will be set after insertion
    }

    public void setTimeTableId(int timeTableId) {
        this.timeTableId = timeTableId; // Can be used after retrieval from the database
    }

    public int getLectureId() {
        return lectureId;
    }

    public void setLectureId(int lectureId) {
        this.lectureId = lectureId;
    }

    public int getVenueId() {
        return venueId;
    }

    public void setVenueId(int venueId) {
        this.venueId = venueId;
    }

    public int getSlotId() {
        return slotId;
    }

    public void setSlotId(int slotId) {
        this.slotId = slotId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(int subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
