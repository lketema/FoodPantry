package com.example.group_project.foodpantry;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String name, email, phoneNumber, userType;
    private List<String> favorites = new ArrayList<>();

    public User(String name, String email, String phoneNumber, String userType) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userType = userType;
//        this.favorites.add("ABC_Pantry");
//        this.favorites.add("UMD PANTRY");
    }

    public User() {}

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getUserType() {
        return userType;
    }

    public List<String> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<String> favorites) {
        this.favorites = favorites;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void addFavorite(String registrationID) {
        this.favorites.add(registrationID);
    }

    public void removeFavorite(String registrationID) {
        this.favorites.remove(registrationID);
    }

}
