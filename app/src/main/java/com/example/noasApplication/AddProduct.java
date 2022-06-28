package com.example.noasApplication;

import androidx.annotation.NonNull;
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

public class AddProduct extends AppCompatActivity {

    EditText name, cal, car, fats, prot;
    TextView errors;
    Intent choose_product;
    String str_name, str_cal, str_car, str_fats, str_prot, error;

    ArrayList products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        name = (EditText)findViewById(R.id.name_add_product);
        cal = (EditText)findViewById(R.id.cal_add_product);
        car = (EditText)findViewById(R.id.car_add_product);
        fats = (EditText)findViewById(R.id.fats_add_product);
        prot = (EditText)findViewById(R.id.prot_add_product);

        errors = (TextView) findViewById(R.id.errors_text) ;

        choose_product = getIntent();

        products = new ArrayList();

        com.example.noasApplication.FBRefs.refProducts.addListenerForSingleValueEvent(new ValueEventListener() { // לקחת מתכונים
            @Override
            public void onDataChange(@NonNull DataSnapshot dS) {
                products.clear();
                for(DataSnapshot data : dS.getChildren()) {
                    products.add("" + data.getKey());
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    private boolean valid_name(){
        str_name = name.getText().toString();
        if (str_name.length() > 0){
            return true;
        }
        if (!products.contains(str_name)){
            return true;
        }
        return false;
    }

    private boolean valid_cal(){
        str_cal = cal.getText().toString();
        if (Integer.parseInt(str_cal) >= 0){
            return true;
        }
        return false;
    }

    private boolean valid_car(){
        str_car = car.getText().toString();
        if (Integer.parseInt(str_car) >= 0){
            return true;
        }
        return false;
    }

    private boolean valid_fats(){
        str_fats = fats.getText().toString();
        if (Integer.parseInt(str_fats) >= 0){
            return true;
        }
        return false;
    }

    private boolean valid_prot(){
        str_prot = prot.getText().toString();
        if (Integer.parseInt(str_prot) >= 0){
            return true;
        }
        return false;
    }

    public void submit(View view) {
        error = "";
        if (!valid_name()) error += "Invalid name.\n";
        if (!valid_cal()) error += "Invalid number of calories.\n";
        if (!valid_car()) error += "Invalid number of carbohydrates.\n";
        if (!valid_fats()) error += "Invalid number of fats.\n";
        if (!valid_prot()) error += "Invalid number of proteins.\n";
        errors.setText(error);
        if (error == ""){
            String key = String.valueOf(products.size());
            // add product to fb
            FBRefs.refProducts.child(key).setValue("");
            System.out.println("----HII");
            FBRefs.refProducts.child(key).child("name").setValue(str_name);
            FBRefs.refProducts.child(key).child("cal").setValue(str_cal);
            FBRefs.refProducts.child(key).child("car").setValue(str_car);
            FBRefs.refProducts.child(key).child("fats").setValue(str_fats);
            FBRefs.refProducts.child(key).child("prot").setValue(str_prot);
            // add product to recipe
            choose_product.putExtra("n", key);
            setResult(RESULT_OK, choose_product);
            // back
            finish();
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