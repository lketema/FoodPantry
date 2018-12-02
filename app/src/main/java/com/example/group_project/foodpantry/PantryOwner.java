package com.example.group_project.foodpantry;

import java.util.ArrayList;
import java.util.List;

public class PantryOwner extends User {
    private List<String> registrations = new ArrayList<>();

    public PantryOwner(String name, String email, String phoneNumber, String userType) {
        super(name, email, phoneNumber, userType);
    }

    public PantryOwner() {}

    public List<String> getRegistrations() {
        return registrations;
    }

    public void setRegistrations(List<String> registrations) {
        this.registrations = registrations;
    }

    public void addRegistration(String registrationID) {
        this.registrations.add(registrationID);
    }

    public void removeRegistration(String registrationID) {
        this.registrations.remove(registrationID);
    }
}
