package com.example.group_project.foodpantry;

import java.util.List;
import java.util.ArrayList;

public class Pantry extends Registration {
    
    private static final int SUNDAY = 0, MONDAY = 1, TUESDAY = 2, WEDNESDAY = 3,
            THURSDAY = 4, FRIDAY = 5, SATURDAY = 6;

    private List<Boolean> daysOpen;

    public Pantry(String name, String address, String phoneNumber, String
        emailAddress, String website, String timeOpen, String timeClosed,
        List<Boolean> daysOpen) {
        super(name, address, phoneNumber, emailAddress, website, timeOpen, timeClosed);
        this.daysOpen = daysOpen;
    }

    public Pantry() {}

    public List<Boolean> getDaysOpen() {
        return daysOpen;
    }

    public void setDaysOpen(List<Boolean> daysOpen) {
        this.daysOpen = daysOpen;
    }

    public Boolean isOpenOn(int day) {
        return day < 0 || day > 6 ? false : daysOpen.get(day);
    }

    public boolean isPantry() {
        return true;
    }

    public void setDayOperational(int day, boolean open) {
        daysOpen.set(day,open);
    }

    @Override
    public String toString() {
        return super.toString() + ", " + daysOpen.toString();
    }

}
