package com.example.noasApplication;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.noasApplication.main_screen_activity.current_user;

public class AddRecipe extends AppCompatActivity {

    EditText name, description, cal, ingredients, instructions, topping;
    Switch kosher;
    String str_name, str_description, str_cal, str_ingredients, str_instructions, str_topping;
    TextView errors;

    ArrayList Recipes;

    int next_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        name = (EditText)findViewById(R.id.name_recipe);
        description = (EditText)findViewById(R.id.description_recipe);
        cal = (EditText)findViewById(R.id.calories_recipe);
        ingredients = (EditText)findViewById(R.id.ingredients_recipe);
        instructions = (EditText)findViewById(R.id.instructions_recipe);
        topping = (EditText)findViewById(R.id.topping_recipe);
        kosher = (Switch)findViewById(R.id.kosher_recipe);
        errors = (TextView)findViewById(R.id.errors_recipe) ;

        Recipes = new ArrayList();

        com.example.noasApplication.FBRefs.refRecipes.addListenerForSingleValueEvent(new ValueEventListener() { // לקחת מתכונים
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
    }

    private boolean valid_name(){
        str_name = name.getText().toString();
        if (str_name.length() >= 3){
            return true;
        }
        return false;
    }

    private boolean valid_description(){
        str_description = description.getText().toString();
        return true;
    }

    private boolean valid_cal(){
        str_cal = cal.getText().toString();
        if (Double.parseDouble(str_cal) > 0){
            return true;
        }
        return false;
    }

    private boolean valid_ingredients(){
        str_ingredients = ingredients.getText().toString();
        if (str_ingredients.length() > 0){
            return true;
        }
        return false;
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
        String error = "";
        if (!valid_name()){error += "Name is too short.\n";}
        if (!valid_description()){error += "\n";}
        if (!valid_cal()){error += "Invalid number of calories.\n";}
        if (!valid_ingredients()){error += "must be at least one ingredient.\n";}
        if (!valid_instructions()){error += "must be at least one instruction.\n";}
        if (!valid_topping()){error += "\n";}
        errors.setText(error);
        if (error == ""){
            next_key = Recipes.size();
            String key = String.valueOf(next_key);
            com.example.noasApplication.FBRefs.refRecipes.child(key).setValue("");
            com.example.noasApplication.FBRefs.refRecipes.child(key).child("name").setValue(str_name);
            com.example.noasApplication.FBRefs.refRecipes.child(key).child("description").setValue(str_description);
            com.example.noasApplication.FBRefs.refRecipes.child(key).child("cal").setValue(str_cal);
            com.example.noasApplication.FBRefs.refRecipes.child(key).child("ingredients").setValue(str_ingredients);
            com.example.noasApplication.FBRefs.refRecipes.child(key).child("instructions").setValue(str_instructions);
            com.example.noasApplication.FBRefs.refRecipes.child(key).child("topping").setValue(str_topping);
            com.example.noasApplication.FBRefs.refRecipes.child(key).child("kosher").setValue(kosher.isChecked());
            com.example.noasApplication.FBRefs.refUsers.child(String.valueOf(current_user.getId())).child("recipes").child(key).setValue(" ");
            finish();
        }
    }
}