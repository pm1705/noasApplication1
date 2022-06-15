package com.example.noasApplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class login_activity extends AppCompatActivity {

    EditText email, pass;
    TextView errors; // הערות למשתמש

    ArrayList usersEmails,usersPasswords; // השוואה לענן
    boolean email_exist;
    String str_email, str_pass, currentMail, currentPass;
    int userId;

    Intent main_screen;

    SharedPreferences.Editor editor; // זיכרון פנימי

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);

        main_screen = new Intent(this, com.example.noasApplication.main_screen_activity.class);

        SharedPreferences logged_information = getSharedPreferences("INFO",MODE_PRIVATE);
        editor = logged_information.edit();

        email = (EditText)findViewById(R.id.emailInput);
        pass = (EditText)findViewById(R.id.passInput);
        errors = (TextView)findViewById(R.id.error);

        usersEmails = new ArrayList();
        usersPasswords = new ArrayList();

        com.example.noasApplication.FBRefs.refUsers.addListenerForSingleValueEvent(new ValueEventListener() { // מוציא מידע מהענן
            @Override
            public void onDataChange(@NonNull DataSnapshot dS) {
                usersEmails.clear();
                usersPasswords.clear();
                for(DataSnapshot data : dS.getChildren()) {
                    currentMail = "" + data.child("email").getValue();
                    currentPass = "" + data.child("pass").getValue();
                    usersEmails.add(currentMail);
                    usersPasswords.add(currentPass);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }


    public void submitLogin(View view) {
        str_email = email.getText().toString();
        str_pass = pass.getText().toString();
        email_exist = false;

        for (int i = 0; i < usersEmails.size() && !email_exist; i++){
            if (str_email.equals(usersEmails.get(i))){
                email_exist = true;
                userId = i;
            }
        }
        if (email_exist){
            if (str_pass.equals(usersPasswords.get(userId))){

                errors.setText("succesful login!");

                editor.putBoolean("logged_in",true); // זיכרון פנימי
                editor.putInt("key_id",userId);
                editor.commit();

                startActivity(main_screen);
            }
            else{
                errors.setText("Password isn't correct.");
                editor.putBoolean("logged_in",false);
                editor.putInt("key_id",-1);
                editor.commit();
            }
        }
        else {
            errors.setText("There is no user associated with this email address.");
            editor.putBoolean("logged_in",false);
            editor.putInt("key_id",-1);
            editor.commit();
        }

    }


}