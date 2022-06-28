package com.example.noasApplication;

import static com.example.noasApplication.FBRefs.refProducts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ingredient_amount extends AppCompatActivity {

    TextView name, errors;
    EditText amount;
    String str_amount, str_errors;
    String[] products, products_ids;
    ArrayList<Integer> grams, cals;
    int index;
    double cal = 0;

    Intent recipe_conclusion, recived_intent;

    ValueEventListener stuListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_amount);

        name = (TextView)findViewById(R.id.product_name_amount);
        errors = (TextView)findViewById(R.id.errors_amount);
        amount = (EditText)findViewById(R.id.grams_amount);

        recipe_conclusion = new Intent(this, AddRecipeInstructions.class);
        recived_intent = getIntent();

        products = recived_intent.getStringArrayExtra("products_names");
        products_ids = recived_intent.getStringArrayExtra("products_ids");

        index = 0;

        cals = new ArrayList<>();
        grams = new ArrayList<>();

        stuListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dS) {

                cals.clear();

                for(DataSnapshot data : dS.getChildren()) {
                    cals.add(Integer.parseInt(data.child("cal").getValue().toString()));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        };
        refProducts.addValueEventListener(stuListener);
        show();
    }

    private void show(){
        name.setText(products[index]);
        amount.setText("0");
    }

    private double calculate_calories(){
        double sum = 0;
        for (int i = 0; i < grams.size(); i++){
            double cal, gram;
            cal = cals.get(i);
            gram = grams.get(i);
            sum += cal * (gram/100);
        }
        return sum;
    }

    private boolean valid_grams(){
        str_amount = amount.getText().toString();
        if (Integer.parseInt(str_amount) > 0){
            return true;
        }
        return false;
    }

    public void pre(View view) {
        if (index > 0) {
            index--;
            show();
        }
    }

    public void next(View view) {
        str_errors = "";
        if (!valid_grams()) str_errors = "Invalid number of grams.\n";
        errors.setText(str_errors);
        if (str_errors == "") {
            grams.add(Integer.parseInt(str_amount));
            if (index < products.length-1) {
                index++;
                show();
            }
            else {
                recipe_conclusion.putExtra("name", recived_intent.getStringExtra("name"));
                recipe_conclusion.putExtra("cal", calculate_calories());
                recipe_conclusion.putExtra("time", recived_intent.getIntExtra("time", 0));
                recipe_conclusion.putExtra("kosher", recived_intent.getBooleanExtra("kosher", false));
                recipe_conclusion.putExtra("description", recived_intent.getStringExtra("description"));
                recipe_conclusion.putExtra("products_names", products);
                recipe_conclusion.putExtra("products_ids", products_ids);
                double[] grams_array = new double[grams.size()];

                for (int i=0; i<grams.size();i++){
                    grams_array[i] = grams.get(i);
                }

                recipe_conclusion.putExtra("grams", grams_array);
                startActivity(recipe_conclusion);
            }
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