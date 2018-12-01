package com.example.group_project.foodpantry;

import java.util.ArrayList;

public class PantryOwner extends User {

    private ArrayList<Pantry> pantries;

    public PantryOwner(String name, String email, String password) {
        super(name, email, password);
        this.pantries = new ArrayList<Pantry>();
    }

    public ArrayList<Pantry> getPantries() {
        return pantries;
    }

    public void addPantry(Pantry pantry) {
        this.pantries.add(pantry);
    }

    public void removePantry(Pantry pantry) {
        this.pantries.remove(pantry);
    }

}
