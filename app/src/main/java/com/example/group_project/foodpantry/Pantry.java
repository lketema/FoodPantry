package com.example.group_project.foodpantry;

import java.time.format.DateTimeFormatter;

public class Pantry {
    private String name;
    private PantryOwner pantryOwner;
    //not sure if we'll need this class
    //maybe add events to a given pantry
    //or add time of operation
    private String timeOpen, timeClosed;

    public Pantry(String name, PantryOwner pantryOwner, String timeOpen, String timeClosed) {
        this.name = name;
        this.pantryOwner = pantryOwner;
        this.timeOpen = timeOpen;
        this.timeClosed = timeClosed;
    }

    public String getName() {
        return name;
    }

    public PantryOwner getPantryOwner() {
        return pantryOwner;
    }

    public String getTimeOpen() {
        return timeOpen;
    }

    public String getTimeClosed() {
        return timeClosed;
    }
}
