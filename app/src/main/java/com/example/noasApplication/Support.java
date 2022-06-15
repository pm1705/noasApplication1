package com.example.noasApplication;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Support {

    public static int generate_recipe_key() {
        ArrayList Recipes;

        int next_key;

        Recipes = new ArrayList();

        FBRefs.refRecipes.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dS) {
                Recipes.clear();
                for (DataSnapshot data : dS.getChildren()) {
                    Recipes.add("" + data.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        next_key = Recipes.size();
        return next_key;
    }

    public static int generate_restaurant_key() {
        ArrayList Restaurnts;

        int next_key;

        Restaurnts = new ArrayList();

        FBRefs.refRestaurants.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dS) {
                Restaurnts.clear();
                for (DataSnapshot data : dS.getChildren()) {
                    Restaurnts.add("" + data.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        next_key = Restaurnts.size();
        return next_key;
    }
}
