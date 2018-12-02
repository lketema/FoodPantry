package com.example.group_project.foodpantry;

import java.util.ArrayList;

public class PantryOwner extends User {
    private ArrayList<String> registrations = new ArrayList<>();

    public PantryOwner(String name, String email, String phoneNumber, String userType) {
        super(name, email, phoneNumber, userType);
    }

    public ArrayList<String> getRegistrations() {
        return registrations;
    }

    public void addRegistration(String registrationID) {
        this.registrations.add(registrationID);
    }

    public void removeRegistration(String registrationID) {
        this.registrations.remove(registrationID);
    }
}
