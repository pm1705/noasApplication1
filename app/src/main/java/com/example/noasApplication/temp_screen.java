package com.example.noasApplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;


public class temp_screen extends AppCompatActivity {

    Intent login_intent, register_intent, main_screen_intent;

    SharedPreferences.Editor editor; // שהמחשב יזכור שאני מחובר

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temporay_activity);

        login_intent = new Intent(this, login_activity.class);
        register_intent = new Intent(this, register_activity.class);
        main_screen_intent = new Intent(this, com.example.noasApplication.main_screen_activity.class);

        SharedPreferences logged_information = getSharedPreferences("INFO",MODE_PRIVATE);
        editor = logged_information.edit();

        if (logged_information.getBoolean("logged_in",false)){
            startActivity(main_screen_intent);
        }
    }

    public void login_submit(View view) {
        startActivity(login_intent);
    }

    public void register_submit(View view) {
        startActivity(register_intent);
    }
}

