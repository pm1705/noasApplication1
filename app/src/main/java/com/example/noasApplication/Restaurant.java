package com.example.noasApplication;

import java.util.ArrayList;

public class Restaurant {
    String name;
    String link;
    String[] cities;
    boolean kosher;
    ArrayList<Integer> products;

    public Restaurant(String name, String link, String[] cities, boolean kosher, ArrayList<Integer> products){
        this.name = name;
        this.link = link;
        this.cities = cities;
        this.kosher = kosher;
        this.products = products;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String[] getCities() {
        return cities;
    }

    public void setCities(String[] cities) {
        this.cities = cities;
    }

    public boolean isKosher() {
        return kosher;
    }

    public void setKosher(boolean kosher) {
        this.kosher = kosher;
    }

    public ArrayList<Integer> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Integer> products) {
        this.products = products;
    }
}
