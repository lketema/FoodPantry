package com.example.group_project.foodpantry;

public class Event extends Registration {
    public Event(String name, String address, String phoneNumber, String
        emailAddress, String website, String timeOpen, String timeClosed) {
        super(name, address, phoneNumber, emailAddress, website, timeOpen, timeClosed);
    }

    public boolean isPantry() {
        return false;
    }

}
