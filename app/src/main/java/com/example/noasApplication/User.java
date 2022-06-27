package com.example.noasApplication;

public class User {
    String name, email, password, location;
    int weight, height, age, activity_level, gender, id;
    //String  recipes, favorites;

    public User(String name, String email, String password, String location, int gender, int weight, int height, int age, int activity_level, int id) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.weight = weight;
        this.height = height;
        this.age = age;
        this.activity_level = activity_level;
        this.location = location;
        // this.recipes = recipes;
        // this.favorites = favorites;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getActivity_level() {
        return activity_level;
    }

    //public String getRecipes() {
    //    return recipes;
    //}
//
    //public void setRecipes(String recipes) {
    //    this.recipes = recipes;
    //}
//
    //public String getFavorites() {
    //    return favorites;
    //}
//
    //public void setFavorites(String favorites) {
    //    this.favorites = favorites;
    //}

    public void setActivity_level(int activity_level) {
        this.activity_level = activity_level;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
