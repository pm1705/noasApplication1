package com.example.noasApplication;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


class FBRefs {
    public static FirebaseDatabase FBDB = FirebaseDatabase.getInstance();
    public static FirebaseStorage FBST = FirebaseStorage.getInstance();

    public static StorageReference storageRef = FBST.getReference();
    public static DatabaseReference refUsers = FBDB.getReference("Users");
    public static DatabaseReference refRecipes = FBDB.getReference("Recipes");
    public static DatabaseReference refRestaurants = FBDB.getReference("Restaurants");
    public static DatabaseReference refProducts = FBDB.getReference("Products");
}
