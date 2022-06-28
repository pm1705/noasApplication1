package com.example.noasApplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.noasApplication.main_screen_activity.current_user;

public class AddRecipe extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    EditText name, description; // cal, ingredients, instructions, topping;
    Switch kosher;
    String str_name, str_description; // str_cal, str_ingredients, str_instructions, str_topping;
    TextView errors;
    Spinner time_pick;
    String[] time_lst = {"all", "breakfast", "lunch", "dinner"};
    int time_int;
    Intent choose_products;

    ArrayList Recipes;

    int next_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        name = (EditText)findViewById(R.id.name_recipe);
        description = (EditText)findViewById(R.id.description_recipe);
        kosher = (Switch)findViewById(R.id.kosher_recipe);
        errors = (TextView)findViewById(R.id.errors_recipe);
        time_pick = (Spinner) findViewById(R.id.time_pick);

        time_pick.setOnItemSelectedListener(this);
        time_int = 0;
        ArrayAdapter<String> gender_adp = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item,time_lst);
        time_pick.setAdapter(gender_adp);

        choose_products = new Intent(this, ChooseProduct.class);

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


    public void submit_recipe(View view) {
        String str_errors = "";
        if (!valid_name()){str_errors += "Name is too short.\n";}
        if (!valid_description()){str_errors += "\n";}
        errors.setText(str_errors);
        if (str_errors == ""){
            // next page
            choose_products.putExtra("name", str_name);
            choose_products.putExtra("description", str_description);
            choose_products.putExtra("kosher", kosher.isChecked());
            choose_products.putExtra("time", time_int);
            startActivity(choose_products);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        time_int = i;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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