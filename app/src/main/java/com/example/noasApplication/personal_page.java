package com.example.noasApplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.noasApplication.FBRefs.refUsers;
import static com.example.noasApplication.main_screen_activity.current_user;

public class personal_page extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    EditText uname, email, pass, age, weight, height, location;
    Spinner gender, activity_level;
    ImageView avatar;
    String str_uname, str_email, str_pass, str_age, str_weight, str_height, str_location, currentMail;
    TextView errors;
    ArrayList usersEmails;
    int next_id, int_activity_level, int_gender;
    String[] sex_options = {"Male", "Female", "Other"};
    String[] activity_options = {"1", "2", "3", "4", "5"};
    ImageView pfpimg;
    Uri selectedImageUri;
    Intent recieved_intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_page);

        uname = (EditText)findViewById(R.id.uname_register);
        email = (EditText)findViewById(R.id.email_register);
        pass = (EditText)findViewById(R.id.pass_register);
        age = (EditText)findViewById(R.id.age_register);
        weight = (EditText)findViewById(R.id.weight_register);
        height = (EditText)findViewById(R.id.height_register);
        gender = (Spinner)findViewById(R.id.gender_register);
        activity_level = (Spinner)findViewById(R.id.activity_level_register);
        location = (EditText)findViewById(R.id.location_register);
        errors = (TextView)findViewById(R.id.error);

        pfpimg = (ImageView) findViewById(R.id.profile_image);
        recieved_intent = getIntent();
        selectedImageUri = Uri.parse(recieved_intent.getStringExtra("uri"));
        if (null != selectedImageUri && selectedImageUri.toString() != "") {
            try {
                Picasso.get().load(selectedImageUri.toString()).into(pfpimg);
            }
            catch (Exception e){
            }
        }

        uname.setText(current_user.getName());
        email.setText(current_user.getEmail());
        pass.setText(current_user.getPassword());
        age.setText("" + current_user.getAge());
        weight.setText("" + current_user.getWeight());
        height.setText("" + current_user.getHeight());
        gender.setSelection(current_user.getGender());
        activity_level.setSelection(current_user.getActivity_level());
        location.setText(current_user.getLocation());


        gender.setOnItemSelectedListener(this);
        ArrayAdapter<String> gender_adp = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item,sex_options);
        gender.setAdapter(gender_adp);

        activity_level.setOnItemSelectedListener(this);
        ArrayAdapter<String> activity_adp = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item,activity_options);
        activity_level.setAdapter(activity_adp);

        usersEmails = new ArrayList();

        refUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dS) {
                usersEmails.clear();
                for(DataSnapshot data : dS.getChildren()) {
                    currentMail = "" + data.child("email").getValue();
                    usersEmails.add(currentMail);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


    }

    private boolean valid_uname(){
        str_uname = uname.getText().toString();
        if (str_uname.length() > 4) return true;
        return false;
    }

    private boolean valid_email(){
        str_email = email.getText().toString();
        if (str_email.contains("@")) return true;
        return false;
    }

    private boolean valid_pass(){
        str_pass = pass.getText().toString();
        if (str_pass.length() < 8) return false;
        return true;
    }

    private boolean valid_age(){
        str_age = age.getText().toString();
        if (Integer.parseInt(str_age) <= 0) return false;
        return true;
    }

    private boolean valid_weight(){
        str_weight = weight.getText().toString();
        if (Integer.parseInt(str_weight) <= 0) return false;
        return true;
    }

    private boolean valid_height(){
        str_height = height.getText().toString();
        if (Integer.parseInt(str_height) <= 0) return false;
        return true;
    }

    private boolean valid_location(){
        str_location = location.getText().toString();

        return true;
    }

    private boolean valid_gender(){
        if (int_gender == -1) return false;
        return true;
    }

    private boolean valid_activity_level(){
        if (int_activity_level == -1) return false;
        return true;
    }

    public boolean free_email(){
        str_email = email.getText().toString();
        System.out.println(str_email + "," + current_user.getEmail());
        if  (usersEmails.contains(str_email) && !str_email.equals(current_user.getEmail())){
            return false;
        }
        return true;
    }

    public void submitChanges(View view) {
        String error = "";
        System.out.println(current_user.getId());
        if (!valid_uname()) error += "Username is too short.\n";
        if (!valid_email()) error += "Invalid email.\n";
        if (!free_email()) error += "Email in use.\n";
        if (!valid_pass()) error += "Invalid password.\n";
        if (!valid_age()) error += "Invalid age.\n";
        if (!valid_weight()) error += "Invalid weight.\n";
        if (!valid_height()) error += "Invalid height.\n";
        if (!valid_location()) error += "Invalid location.\n";
        if (!valid_gender()) error += "Must choose gender.\n";
        if (!valid_activity_level()) error += "Must choose activity level.\n";
        errors.setText(error);
        if (error == ""){
            refUsers.child(Integer.toString(current_user.getId())).setValue("");
            refUsers.child(Integer.toString(current_user.getId())).child("username").setValue(str_uname);
            refUsers.child(Integer.toString(current_user.getId())).child("email").setValue(str_email);
            refUsers.child(Integer.toString(current_user.getId())).child("pass").setValue(str_pass);
            refUsers.child(Integer.toString(current_user.getId())).child("age").setValue(str_age);
            refUsers.child(Integer.toString(current_user.getId())).child("weight").setValue(str_weight);
            refUsers.child(Integer.toString(current_user.getId())).child("height").setValue(str_height);
            refUsers.child(Integer.toString(current_user.getId())).child("gender").setValue(int_gender);
            refUsers.child(Integer.toString(current_user.getId())).child("activityLevel").setValue(int_activity_level);
            refUsers.child(Integer.toString(current_user.getId())).child("location").setValue(str_location);
            if (null != selectedImageUri) {
                FBRefs.storageRef.child(str_email).putFile(selectedImageUri);
            }

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getResources().getResourceEntryName(adapterView.getId()).equals("gender_register")) {
            int_gender = i;
        }
        if (adapterView.getResources().getResourceEntryName(adapterView.getId()).equals("activity_level_register")) {
            int_activity_level = i;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public void upload_img(View view) {
        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), 200);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == 200) {
                // Get the url of the image from data
                selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    pfpimg.setImageURI(selectedImageUri);
                }
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