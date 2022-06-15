package com.example.noasApplication;

import java.util.ArrayList;

public class Recipe extends Product {
    private String name;
    private String description;
    private double cal;
    private boolean kosher;
    private ArrayList<String> instructions;
    private ArrayList<String> ingredients;
    private ArrayList<String> toppings;
    private String key;

    public Recipe(String name, String description, double cal, boolean kosher,
                  ArrayList<String> instructions, ArrayList<String> ingredients,
                  ArrayList<String> toppings, String key) {
        super(name, description, cal, kosher, key);
        this.instructions = instructions;
        this.ingredients = ingredients;
        this.toppings = toppings;
    }

    public ArrayList<String> getInstructions() {
        return instructions;
    }

    public void setInstructions(ArrayList<String> instructions) {
        this.instructions = instructions;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<String> getToppings() {
        return toppings;
    }

    public void setToppings(ArrayList<String> toppings) {
        this.toppings = toppings;
    }

}
