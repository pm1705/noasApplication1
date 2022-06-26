package com.example.noasApplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
    Intent product_page, ingredient_amount, recived_intent;
    String str_name;

    String search_text, current_id, current_name, current_description, recieved_option;
    boolean current_kosher;

    ArrayList<String> product_list;
    ArrayList<Integer> products;

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

        str_products = "";
        products = new ArrayList<Integer>();
        product_list = new ArrayList<>();

        product_page = new Intent(this, AddProduct.class);
        ingredient_amount = new Intent(this, ingredient_amount.class);
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
        if (product_list.size() > 0) {
            str_products = product_list.get(0);
            for (int i = 1; i < product_list.size(); i++) {
                str_products += ", " + product_list.get(i);
            }
            search.setText(str_products);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (matching_results.get(i) != "-1") {
            if (products.contains(i)) products.remove(i);
            else products.add(i);
        }
        else {
            show_products();
            System.out.println(i);
            startActivityForResult(product_page, 1);
        }
    }

    @Override
    protected void onActivityResult(int source, int good, @Nullable Intent data_back) {
        super.onActivityResult(source, good, data_back);
        if (data_back != null) {
            products.add(data_back.getIntExtra("n", 0));
            show_products();
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
            product_list.add("-1");
            matching_results.add("-1");
            matching_results_info.add("Add new Product");
        }
        ArrayAdapter<String> adp = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, matching_results_info);
        result.setAdapter(adp);
    }

    public void Next(View view) {
        ingredient_amount.putExtra("products_names", product_list);
        ingredient_amount.putExtra("products_ids", products);
        ingredient_amount.putExtra("name", recived_intent.getStringExtra("name"));
        ingredient_amount.putExtra("description", recived_intent.getStringExtra("description"));
        ingredient_amount.putExtra("kosher", recived_intent.getBooleanExtra("kosher", false));
        ingredient_amount.putExtra("time", recived_intent.getIntExtra("time", 0));
        com.example.noasApplication.FBRefs.refRecipes.removeEventListener(stuListener);
        startActivity(ingredient_amount);
    }
}