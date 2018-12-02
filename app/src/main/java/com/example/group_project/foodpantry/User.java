package com.example.group_project.foodpantry;

public class User {
//    enum UserType{
//        OWNER, VOLUNTEER, RECIPIENT
//    }

    public String fullname, email, phoneNumber,userType;
//    UserType userType;

    public User(String fullname, String email, String phoneNumber, String userType){
        this.fullname = fullname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userType = userType;

    }


}
