package com.example.group_project.foodpantry;

public class Pantry extends Registration {
    
    private static final int SUNDAY = 0, MONDAY = 1, TUESDAY = 2, WEDNESDAY = 3,
            THURSDAY = 4, FRIDAY = 5, SATURDAY = 6;
  
    private boolean[] daysOpen;

    public Pantry(String name, String address, String phoneNumber, String
        emailAddress, String website, String timeOpen, String timeClosed,
        boolean[] daysOpen) {
        super(name, address, phoneNumber, emailAddress, website, timeOpen, timeClosed);
        this.daysOpen = daysOpen;
    }

    public boolean isPantry() {
        return true;
    }

    public boolean isOpenOn(int day) {
        return day < 0 || day > 6 ? false : daysOpen[day];
    }

    public void setDayOperational(int day, boolean open) {
        daysOpen[day] = open;
    }
}
