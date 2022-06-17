package com.example.noasApplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChooseProduct extends AppCompatActivity implements AdapterView.OnItemClickListener {

    EditText name;
    ListView result;
    TextView search;
    Intent product_page;
    String str_name;

    ArrayList<String> product_list;
    ArrayList<Integer> products;
    String str_products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_product);

        name = (EditText)findViewById(R.id.search_choose_product);
        result = (ListView)findViewById(R.id.results);
        search = (TextView)findViewById(R.id.search_choose_product);

        result.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        result.setOnItemClickListener(this);

        product_page = new Intent(this, AddProduct.class);

    }

    public void show_products(){
        str_products = product_list.get(0);
        for (int i = 1; i < product_list.size(); i++){
            str_products += ", " + product_list.get(i);
        }
        search.setText(str_products);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (products.contains(i)) products.remove(i);
        else products.add(i);
        show_products();
        if (product_list.get(i) == "-1"){
            startActivityForResult(product_page,1);
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
}