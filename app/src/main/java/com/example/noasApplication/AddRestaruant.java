package com.example.noasApplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
import java.util.Arrays;

import static com.example.noasApplication.FBRefs.refRecipes;
import static com.example.noasApplication.FBRefs.refRestaurants;

public class AddRestaruant extends AppCompatActivity {

    EditText res_name, res_link, res_location, pro_name, pro_description, pro_calories;
    Switch kosher;
    String str_res_name, str_res_link, str_res_location, str_pro_name, str_pro_description, str_pro_calories;
    TextView res_errors, pro_errors;

    Intent main_screen_intent;

    ArrayList<String> Recipes, Restaurnts;
    ArrayList<com.example.noasApplication.Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_restaruant);

        res_name = (EditText)findViewById(R.id.res_name_add_res);
        res_link = (EditText)findViewById(R.id.res_link_add_res);
        res_location = (EditText)findViewById(R.id.res_location_add_res);
        pro_name = (EditText)findViewById(R.id.pro_name_add_res);
        pro_description = (EditText)findViewById(R.id.pro_description_add_res);
        pro_calories = (EditText)findViewById(R.id.pro_calories_add_res);
        kosher = (Switch)findViewById(R.id.res_kosher_add_res);
        res_errors = (TextView)findViewById(R.id.res_errors_add_res);
        pro_errors = (TextView)findViewById(R.id.pro_errors_add_res);

        Recipes = new ArrayList<>();
        Restaurnts = new ArrayList<>();

        products = new ArrayList<com.example.noasApplication.Product>();

        main_screen_intent = new Intent(this, com.example.noasApplication.main_screen_activity.class);


        refRecipes.addListenerForSingleValueEvent(new ValueEventListener() {
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
        refRestaurants.addListenerForSingleValueEvent(new ValueEventListener() {
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

    }

    private boolean valid_res_name(){
        str_res_name = res_name.getText().toString();
        if (str_res_name.length() > 0){
            return true;
        }
        return false;
    }

    private boolean valid_res_link(){
        str_res_link = res_link.getText().toString();
        if (str_res_link.length() > 0){
            return true;
        }
        return false;
    }

    private boolean valid_res_location(){
        str_res_location = res_location.getText().toString();
        if (str_res_location.length() > 0){
            return true;
        }
        return false;
    }

    private boolean valid_pro_name(){
        str_pro_name = pro_name.getText().toString();
        if (str_pro_name.length() > 0){
            return true;
        }
        return false;
    }

    private boolean valid_pro_description(){
        str_pro_description = pro_description.getText().toString();
        return true;
    }

    private boolean valid_pro_calories(){
        str_pro_calories = pro_calories.getText().toString();
        if (Double.parseDouble(str_pro_calories) > 0){
            return true;
        }
        return false;
    }

    private boolean valid_products(){
        if (products.size() > 0){
            return true;
        }
        return false;
    }

    private ArrayList<String> locations(){
        ArrayList<String> list = new ArrayList<String>(Arrays.asList(str_res_location.split("\n")));
        return list;
    }


    public void add_pro_add_res(View view) {
        String errors = "";
        if (!valid_pro_name()){errors += "Invalid name of product.\n";}
        if (!valid_pro_description()){errors += "Invalid description.\n";}
        if (!valid_pro_calories()){errors += "Invalid number of calories.\n";}
        pro_errors.setText(errors);
        if (errors == ""){
            // add to product list
            String key = String.valueOf(Recipes.size());
            products.add(new com.example.noasApplication.Product(str_pro_name, str_pro_description,
                            Double.parseDouble(str_pro_calories), kosher.isChecked(), key)
                        );
            // add to firebase
            refRecipes.child(key).setValue("");
            refRecipes.child(key).child("name").setValue(str_pro_name);
            refRecipes.child(key).child("description").setValue(str_pro_description);
            refRecipes.child(key).child("cal").setValue(Integer.parseInt(str_pro_calories));
            refRecipes.child(key).child("kosher").setValue(kosher.isChecked());
            refRecipes.child(key).child("time").setValue(0);
            refRecipes.child(key).child("instructions").setValue("none");
            refRecipes.child(key).child("ingredients").setValue("none");
            refRecipes.child(key).child("topping").setValue("none");
            // clean fields
            pro_name.setText("");
            pro_description.setText("");
            pro_calories.setText("");
        }
    }

    public void submit(View view) {
        String errors = "";
        if (!valid_res_name()){errors += "Invalid name of restaurant.\n";}
        if (!valid_res_link()){errors += "Invalid link.\n";}
        if (!valid_res_location()){errors += "There must be at least one location.\n";}
        if (!valid_products()){errors += " There must be at least one product.\n";}
        res_errors.setText(errors);
        if (errors == ""){
            String key = String.valueOf(Restaurnts.size());
            refRestaurants.child(key).setValue("");
            refRestaurants.child(key).child("name").setValue(str_res_name);
            refRestaurants.child(key).child("link").setValue(str_res_link);
            refRestaurants.child(key).child("locations").setValue("");
            ArrayList<String> location_list = locations();
            for (int i = 0; i < location_list.size(); i++){
                refRestaurants.child(key).child("locations").child("" + i).setValue(location_list.get(i));
            }
            refRestaurants.child(key).child("products").setValue("");
            for (int i = 0; i < products.size(); i++){
                System.out.println(products.get(i).toString());
                refRestaurants.child(key).child("products").child("" + i).setValue(products.get(i));
            }
            startActivity(main_screen_intent);

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