package com.example.noasApplication;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


class FBRefs {
    public static FirebaseDatabase FBDB = FirebaseDatabase.getInstance();

    public static DatabaseReference refUsers = FBDB.getReference("Users");
    public static DatabaseReference refRecipes = FBDB.getReference("Recipes");
    public static DatabaseReference refRestaurants = FBDB.getReference("Restaurants");
}
