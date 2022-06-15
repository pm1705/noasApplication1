package com.example.noasApplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

import static com.example.noasApplication.main_screen_activity.current_user;

public class browse_recipes extends AppCompatActivity implements AdapterView.OnItemClickListener {

    EditText search_input;
    ListView results;

    String search_text, current_id, current_name, current_description, recieved_option, allowed_ids;

    ArrayList<String> recipes_ids, recipes_names, recipes_descriptions, matching_results, matching_results_info;

    Intent recieved_intent, show_recipe;

    ValueEventListener stuListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_recipes);

        search_input = (EditText) findViewById(R.id.search_input);
        results = (ListView) findViewById(R.id.results);

        results.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        results.setOnItemClickListener(this);

        search_text = search_input.getText().toString();

        recieved_intent = getIntent();
        recieved_option = recieved_intent.getStringExtra("option");
        allowed_ids = "";

        show_recipe = new Intent(this, RecipePage.class);

        if (recieved_option.equals("favorites")){
            allowed_ids = current_user.getFavorites();
        }
        else if (recieved_option.equals("mine")){
            allowed_ids = current_user.getRecipes();
        }

        String[] allowed_ids_list = allowed_ids.split("\\,",-1);
        ArrayList allowed_ids_list2 = new ArrayList<String>(Arrays.asList(allowed_ids_list));

        recipes_ids = new ArrayList<String>();
        recipes_names = new ArrayList<String>();
        recipes_descriptions = new ArrayList<String>();
        matching_results = new ArrayList<String>();
        matching_results_info = new ArrayList<String>();


        stuListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dS) {

                recipes_ids.clear();
                recipes_names.clear();
                recipes_descriptions.clear();

                for(DataSnapshot data : dS.getChildren()) {

                    if (recieved_option.equals("regular") || allowed_ids_list2.contains(data.getKey())) {
                        recipes_ids.add("" + data.getKey());
                        recipes_names.add("" + data.child("name").getValue().toString());
                        recipes_descriptions.add("" + data.child("description").getValue().toString());
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        };
        com.example.noasApplication.FBRefs.refRecipes.addValueEventListener(stuListener);
    }


    public void update_results(View view) {

        matching_results.clear();
        matching_results_info.clear();

        search_text = search_input.getText().toString();

        for (int i = 0; i< recipes_ids.size(); i++){

            current_id = recipes_ids.get(i);
            current_name = recipes_names.get(i);
            current_description = recipes_descriptions.get(i);

            if (current_name.contains(search_text) || current_description.contains(search_text)){
                matching_results.add(current_id);
                matching_results_info.add(current_name + ", " + current_description);
            }
        }

        ArrayAdapter<String> adp = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, matching_results_info);
        results.setAdapter(adp);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        show_recipe.putExtra("id",matching_results.get(i));
        startActivity(show_recipe);
    }

    public void onPause() {
        if (stuListener!=null) {
            com.example.noasApplication.FBRefs.refRecipes.removeEventListener(stuListener);
        }
        super.onPause();
    }
}