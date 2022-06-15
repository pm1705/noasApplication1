package com.example.noasApplication;

public class Product {
    private String name;
    private String description;
    private double cal;
    private boolean kosher;
    private String key;

    public Product(String name, String description, double cal, boolean kosher, String key){
        this.name = name;
        this.description = description;
        this.cal = cal;
        this.kosher = kosher;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getCal() {
        return cal;
    }

    public void setCal(double cal) {
        this.cal = cal;
    }

    public boolean isKosher() {
        return kosher;
    }

    public void setKosher(boolean kosher) {
        this.kosher = kosher;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

