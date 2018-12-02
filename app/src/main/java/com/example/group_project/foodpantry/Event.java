package com.example.group_project.foodpantry;

public class Event extends Registration {
    private String eventDate;

    public Event(String name, String address, String phoneNumber, String
        emailAddress, String website, String eventDate) {
        super(name, address, phoneNumber, emailAddress, website);
        this.eventDate = eventDate;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }
}
