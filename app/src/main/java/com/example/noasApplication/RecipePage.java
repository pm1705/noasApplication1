package com.example.noasApplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import static com.example.noasApplication.FBRefs.refRecipes;

public class RecipePage extends AppCompatActivity {

    TextView name, description, ingredients, instructions, topping;
    String str_name, str_cal, str_description, str_ingredients, str_instructions, str_topping;
    String key;

    Intent recieved_intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_page);

        recieved_intent = getIntent();
        key = recieved_intent.getStringExtra("id");

        name = (TextView)findViewById(R.id.name_recpg);
        description = (TextView)findViewById(R.id.description_recpg);
        ingredients = (TextView)findViewById(R.id.ingredients_recpg);
        instructions = (TextView)findViewById(R.id.instructions_recpg);
        topping = (TextView)findViewById(R.id.topping_recpg);

        ValueEventListener stuListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dS) {

                for(DataSnapshot data : dS.getChildren()) {
                    System.out.println(data.getKey() + "," + key);
                    if (data.getKey().equals(key)){
                        str_name = data.child("name").getValue().toString();
                        str_cal = data.child("cal").getValue().toString();
                        str_description = data.child("description").getValue().toString();
                        str_ingredients = data.child("ingredients").getValue().toString();
                        str_instructions = data.child("instructions").getValue().toString();
                        str_topping = data.child("topping").getValue().toString();
                    }
                }

                name.setText("Name: " + str_name + "(" + str_cal + "):");
                description.setText("Description: " + str_description);
                ingredients.setText("Ingredients: " + str_ingredients);
                instructions.setText("Instructions: " + str_instructions);
                topping.setText("Topping: " + str_topping);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        };
        refRecipes.addValueEventListener(stuListener);


    }

    public void back(View view) {
        finish();
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        String itm = item.getTitle().toString();

        if (itm.equals("Recipes")){
            Intent browse_recipes_intent = new Intent(this, browse_recipes.class);
            browse_recipes_intent.putExtra("option", "regular");
            startActivity(browse_recipes_intent);
        }

        return true;
    }
}