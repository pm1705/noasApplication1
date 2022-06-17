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
    private int meal_time;

    public Recipe(String name, String description, double cal, boolean kosher,
                  ArrayList<String> instructions, ArrayList<String> ingredients,
                  ArrayList<String> toppings, String key, int meal_time) {
        super(name, description, cal, kosher, key);
        this.instructions = instructions;
        this.ingredients = ingredients;
        this.toppings = toppings;
        this.meal_time = meal_time;
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

    public int getMeal_time() {
        return meal_time;
    }

    public void setMeal_time(int meal_time) {
        this.meal_time = meal_time;
    }

}
