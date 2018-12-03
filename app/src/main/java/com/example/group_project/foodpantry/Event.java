package com.example.group_project.foodpantry;

public class Event extends Registration {

    private String eventDate;

    public Event(String name, String address, String phoneNumber, String
        emailAddress, String website, String eventDate, String timeOpen, String timeClosed) {
        super(name, address, phoneNumber, emailAddress, website, timeOpen, timeClosed);
        this.eventDate = eventDate;
    }

    public Event() {}

    public String getEventDate() {
        return eventDate;
    }

    public String getEventDateForDisplay() {
        return "Event's date is: " + eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public boolean isPantry() {
        return false;
    }

    public String toString() {
        return super.toString() + ", " + eventDate;
    }

}
