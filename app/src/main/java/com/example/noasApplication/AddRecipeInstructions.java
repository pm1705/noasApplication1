package com.example.noasApplication;

import static com.example.noasApplication.FBRefs.refRecipes;
import static com.example.noasApplication.FBRefs.refUsers;
import static com.example.noasApplication.main_screen_activity.current_user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddRecipeInstructions extends AppCompatActivity {

    TextView name, description, ingredients, errors;
    ImageView img;
    EditText instructions, topping;
    String str_name, str_description, str_ingredients;
    double double_cal;
    int int_time, next_key;
    boolean bool_kosher;
    String[] ingredients_list, ingredients_ids_list;
    double[] grams;
    String str_instructions, str_topping, str_errors;
    Intent recieved_intent, main;
    ArrayList<String> Recipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe_instructions);

        name = (TextView)findViewById(R.id.name_recipe_conclusion);
        description = (TextView)findViewById(R.id.description_recipe_conclusion);
        ingredients = (TextView)findViewById(R.id.ingredients_recipe_conclusion);
        errors = (TextView)findViewById(R.id.errors_recipe_conclusion);
        img = (ImageView)findViewById(R.id.image_recipe_conclusion);
        instructions = (EditText)findViewById(R.id.instruction_recipe_conclusion);
        topping = (EditText)findViewById(R.id.topping_recipe_conclusion);

        recieved_intent = getIntent();
        main = new Intent(this, main_screen_activity.class);

        Recipes = new ArrayList();

        refRecipes.addListenerForSingleValueEvent(new ValueEventListener() { // לקחת מתכונים
            @Override
            public void onDataChange(@NonNull DataSnapshot dS) {
                Recipes.clear();
                for(DataSnapshot data : dS.getChildren()) {
                    Recipes.add("" + data.getKey());
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        receive_info();
        ingredients_to_string();
        show_info();

    }

    private void receive_info(){
        str_name = recieved_intent.getStringExtra("name");
        double_cal = recieved_intent.getDoubleExtra("cal", 0);
        int_time = recieved_intent.getIntExtra("time", 0);
        bool_kosher = recieved_intent.getBooleanExtra("kosher", false);
        str_description = recieved_intent.getStringExtra("description");
        ingredients_list = recieved_intent.getStringArrayExtra("products_names");
        ingredients_ids_list = recieved_intent.getStringArrayExtra("products_ids");
        grams = recieved_intent.getDoubleArrayExtra("grams");
    }

    private void ingredients_to_string(){
        str_ingredients = ingredients_list[0];
        for (int i = 1; i < ingredients_list.length; i++){
            str_ingredients += ingredients_list[i];
        }
    }

    private void show_info(){
        name.setText(str_name + " (" + double_cal + "Calories)- " + int_time +"\nIs kosher: " + bool_kosher);
        description.setText(str_description);
        ingredients.setText(str_ingredients);
    }

    private boolean valid_instructions(){
        str_instructions = instructions.getText().toString();
        if (str_instructions.length() > 0){
            return true;
        }
        return false;
    }

    private boolean valid_topping(){
        str_topping = topping.getText().toString();
        return true;
    }


    public void submit_recipe(View view) {
        str_errors = "";
        if (!valid_instructions()) str_errors += "There must be at least one instruction.\n";
        if (!valid_topping()) str_topping += "\n";
        errors.setText(str_errors);
        if (str_errors == ""){
            next_key = Recipes.size();
            String key = String.valueOf(next_key);
            refRecipes.child(key).setValue("");
            refRecipes.child(key).child("name").setValue(str_name);
            refRecipes.child(key).child("cal").setValue(double_cal);
            refRecipes.child(key).child("time").setValue(int_time);
            refRecipes.child(key).child("kosher").setValue(bool_kosher);
            refRecipes.child(key).child("description").setValue(str_description);
            refRecipes.child(key).child("ingredients").setValue("");
            for (int i = 0; i < ingredients_ids_list.length; i++){
                refRecipes.child(key).child("ingredients").child(String.valueOf(i)).setValue(ingredients_ids_list[i] + " (" + grams[i] + " g)");
            }
            refRecipes.child(key).child("instructions").setValue(str_instructions);
            refRecipes.child(key).child("topping").setValue(str_topping);
            refUsers.child(String.valueOf(current_user.getId())).child("recipes").child(key).setValue(" ");
            startActivity(main);
        }
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