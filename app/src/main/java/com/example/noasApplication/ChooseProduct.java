package com.example.noasApplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChooseProduct extends AppCompatActivity implements AdapterView.OnItemClickListener {

    EditText name;
    ListView result;
    TextView search;
    Intent product_page, ingredient_amount, recived_intent, bar_code_intent;
    String str_name, id_back;

    String search_text, current_id, current_name, current_description, recieved_option;
    boolean current_kosher;

    ArrayList<String> chosen_products_names;
    ArrayList<String> chosen_products_ids;

    ArrayList<String> product_ids, product_names, product_description, product_cal, matching_results, matching_results_info;
    ArrayList<Boolean> product_kosher;

    String str_products;

    ValueEventListener stuListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_product);

        name = (EditText)findViewById(R.id.search_choose_product);
        result = (ListView)findViewById(R.id.results);
        search = (TextView)findViewById(R.id.products_choose_product);

        result.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        result.setOnItemClickListener(this);

        product_ids = new ArrayList<String>();
        product_names = new ArrayList<String>();
        matching_results = new ArrayList<String>();
        matching_results_info = new ArrayList<String>();
        product_cal = new ArrayList<String>();

        chosen_products_ids = new ArrayList<String>();
        chosen_products_names = new ArrayList<String>();

        str_products = "";

        product_page = new Intent(this, AddProduct.class);
        ingredient_amount = new Intent(this, ingredient_amount.class);
        bar_code_intent = new Intent(this, BarQrScan.class);
        recived_intent = getIntent();

        stuListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dS) {

                product_ids.clear();
                product_names.clear();
                product_cal.clear();

                for(DataSnapshot data : dS.getChildren()) {
                    product_ids.add(data.getKey());
                    System.out.println(data.getKey());
                    product_names.add(data.child("name").getValue().toString());
                    product_cal.add(data.child("cal").getValue().toString());

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        };
        FBRefs.refProducts.addValueEventListener(stuListener);

    }

    public void show_products(){
        if (chosen_products_names.size() > 0) {
            str_products = chosen_products_names.get(0);
            for (int i = 1; i < chosen_products_names.size(); i++) {
                str_products += "\n " + chosen_products_names.get(i);
            }
            search.setText(str_products);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (matching_results.get(i) != "-1") {
            if (chosen_products_ids.contains(matching_results.get(i))){
                chosen_products_ids.remove(matching_results.get(i));
                chosen_products_names.remove(matching_results_info.get(i));
                show_products();
            }
            else {
                chosen_products_ids.add(matching_results.get(i));
                chosen_products_names.add(matching_results_info.get(i));
                show_products();
            }
        }
        else {
            show_products();
            System.out.println(i);
            startActivityForResult(product_page, 1);
            if (stuListener!=null) {
                FBRefs.refProducts.removeEventListener(stuListener);
            }
        }
    }

    @Override
    protected void onActivityResult(int source, int good, @Nullable Intent data_back) {
        super.onActivityResult(source, good, data_back);
        if (data_back != null) {
            id_back = data_back.getStringExtra("n");
            if (!id_back.equals("-1")){
                chosen_products_ids.add(id_back);
                show_products();
                FBRefs.refProducts.addValueEventListener(stuListener);
            }
        }
    }

    public void update_results(View view) {
        matching_results.clear();
        matching_results_info.clear();

        search_text = name.getText().toString();

        for (int i = 0; i< product_ids.size(); i++){

            current_id = product_ids.get(i);
            current_name = product_names.get(i);

            if (current_name.contains(search_text)){
                    matching_results.add(current_id);
                    matching_results_info.add(current_name+ " (" + product_cal.get(i) + ")");
            }
        }

        if (matching_results_info.size() < 3){
            matching_results.add("-1");
            matching_results_info.add("Add new Product");
        }
        ArrayAdapter<String> adp = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, matching_results_info);
        result.setAdapter(adp);
    }

    public void Next(View view) {
        String[] chosen_names_array = new String[chosen_products_names.size()];
        String[] chosen_ids_array = new String[chosen_products_names.size()];
        for (int i=0; i<chosen_products_names.size();i++){
            chosen_names_array[i] = chosen_products_names.get(i);
            chosen_ids_array[i] = chosen_products_ids.get(i);
        }
        System.out.println(chosen_products_names.size());
        ingredient_amount.putExtra("products_names", chosen_names_array);
        ingredient_amount.putExtra("products_ids", chosen_ids_array);
        ingredient_amount.putExtra("name", recived_intent.getStringExtra("name"));
        ingredient_amount.putExtra("description", recived_intent.getStringExtra("description"));
        ingredient_amount.putExtra("kosher", recived_intent.getBooleanExtra("kosher", false));
        ingredient_amount.putExtra("time", recived_intent.getIntExtra("time", 0));
        com.example.noasApplication.FBRefs.refRecipes.removeEventListener(stuListener);
        startActivity(ingredient_amount);
    }

    public void scan_now(View view) {
        startActivityForResult(bar_code_intent, 1);
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