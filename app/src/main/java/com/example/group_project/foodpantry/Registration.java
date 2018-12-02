package com.example.group_project.foodpantry;

public abstract class Registration {
    private String name, address, phoneNumber, emailAddress, website;

    protected Registration(String name, String address, String phoneNumber,
        String emailAddress, String website) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.website = website;
    }

    public String getName() {
        return name;
    }
    
    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getWebsite() {
        return website;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
    
    public void setWebsite(String website) {
        this.website = website;
    }
}
