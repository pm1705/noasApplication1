package com.example.noasApplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
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

import java.util.ArrayList;

public class register_activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText uname, email, pass, age, weight, height, location;
    Spinner gender, activity_level;
    TextView errors;

    String str_uname, str_email, str_pass, str_age, str_weight, str_height, str_location, currentMail;
    int next_id, int_activity_level, int_gender;

    ArrayList usersEmails;
    String[] sex_options = {"Male", "Female", "Other"};
    String[] activity_options = {"1", "2", "3", "4", "5"};

    Intent main_screen;

    SharedPreferences.Editor editor; // זיכרון פנימי

    ImageView pfpimg;
    Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_activity);

        main_screen = new Intent(this, com.example.noasApplication.main_screen_activity.class);

        SharedPreferences logged_information = getSharedPreferences("INFO",MODE_PRIVATE);
        editor = logged_information.edit(); // זיכרון פנימי


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

        pfpimg = (ImageView) findViewById(R.id.pfpimg);

        gender.setOnItemSelectedListener(this);
        int_gender = -1;
        ArrayAdapter<String> gender_adp = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item,sex_options);
        gender.setAdapter(gender_adp);

        activity_level.setOnItemSelectedListener(this);
        int_activity_level = -1;
        ArrayAdapter<String> activity_adp = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item,activity_options);
        activity_level.setAdapter(activity_adp);

        usersEmails = new ArrayList();

        com.example.noasApplication.FBRefs.refUsers.addListenerForSingleValueEvent(new ValueEventListener() { // במטרה לא לדרוס אימייל קיים
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
        if (str_pass.length() <= 8) return false;
        return true;
    }

    private boolean valid_age(){
        str_age = age.getText().toString();
        if (Integer.parseInt(str_age) <= 12) return false;
        return true;
    }

    private boolean valid_weight(){
        str_weight = weight.getText().toString();
        if (Integer.parseInt(str_weight) <= 20) return false;
        return true;
    }

    private boolean valid_height(){
        str_height = height.getText().toString();
        if (Double.parseDouble(str_height) <= 50) return false;
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

    public boolean free_email(){ // בדיקת דריסת המייל
        str_email = email.getText().toString();
        if  (usersEmails.contains(str_email)){
            return false;
        }
        return true;
    }

    public void submitRegister(View view) {
        String error = "";
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

            next_id = usersEmails.size(); // 0, 1, 2...

            FBRefs.refUsers.child(Integer.toString(next_id)).setValue("");
            FBRefs.refUsers.child(Integer.toString(next_id)).child("username").setValue(str_uname);
            FBRefs.refUsers.child(Integer.toString(next_id)).child("email").setValue(str_email);
            FBRefs.refUsers.child(Integer.toString(next_id)).child("pass").setValue(str_pass);
            FBRefs.refUsers.child(Integer.toString(next_id)).child("age").setValue(str_age);
            FBRefs.refUsers.child(Integer.toString(next_id)).child("weight").setValue(str_weight);
            FBRefs.refUsers.child(Integer.toString(next_id)).child("height").setValue(str_height);
            FBRefs.refUsers.child(Integer.toString(next_id)).child("gender").setValue(int_gender);
            FBRefs.refUsers.child(Integer.toString(next_id)).child("activityLevel").setValue(int_activity_level);
            FBRefs.refUsers.child(Integer.toString(next_id)).child("location").setValue(str_location);
            FBRefs.refUsers.child(Integer.toString(next_id)).child("recipes").setValue("");
            FBRefs.refUsers.child(Integer.toString(next_id)).child("favorites").setValue("");

            if (null != selectedImageUri) {
                FBRefs.storageRef.child(str_email).putFile(selectedImageUri);
            }

            editor.putBoolean("logged_in",true); // זיכרון פנימי
            editor.putInt("key_id",next_id);
            editor.commit();

            startActivity(main_screen);
        }
        else{
            editor.putBoolean("logged_in",false);
            editor.putInt("key_id",-1);
            editor.commit();
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
}