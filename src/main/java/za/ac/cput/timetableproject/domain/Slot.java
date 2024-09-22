package za.ac.cput.timetableproject.domain;

public class Slot {
    private int slotId;
    private String startTime;
    private String endTime;
    private String dayOfWeek;

    // Default constructor
    public Slot() {
    }

    // Parameterized constructor
    public Slot(String startTime, String endTime, String dayOfWeek) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.dayOfWeek = dayOfWeek;
    }

    // Getters and Setters
    public int getSlotId() {
        return slotId;
    }

    public void setSlotId(int slotId) {
        this.slotId = slotId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
}
