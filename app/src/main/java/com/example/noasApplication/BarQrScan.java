package com.example.noasApplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.Attribution;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import java.util.ArrayList;

public class BarQrScan extends AppCompatActivity {
    private CodeScanner mCodeScanner;

    TextView result_set;
    String result_str;

    Intent recieved_intent;

    ValueEventListener stuListener;
    ArrayList<String> product_ids, product_names;

    Button add_button;

    int indexx;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bar_qr_scan);
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        setUpPermissions();
        mCodeScanner = new CodeScanner(this, scannerView);

        result_set = (TextView) findViewById(R.id.result_set);
        add_button = (Button) findViewById(R.id.add_button);
        add_button.setVisibility(View.INVISIBLE);
        recieved_intent = getIntent();

        product_ids = new ArrayList<String>();
        product_names = new ArrayList<String>();
        indexx = -1;

        //FB
        stuListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dS) {

                product_ids.clear();
                product_names.clear();

                for(DataSnapshot data : dS.getChildren()) {
                    product_ids.add(data.getKey());
                    product_names.add(data.child("name").getValue().toString());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        };
        FBRefs.refProducts.addValueEventListener(stuListener);

        // ALL ADDED FROM WEBSITE - https://github.com/yuriy-budiyev/code-scanner
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        result_str = "" + result;
                        for (int i=0; i<product_ids.size(); i++){
                            System.out.println(""+result_str + " +++ " + product_ids.get(i));
                            if (result_str.equals(product_ids.get(i))){
                                indexx = i;
                            }
                        }
                        if (indexx == -1){
                            result_set.setText("Id Found: " + result + "\nBut no matching product.");
                            add_button.setVisibility(View.INVISIBLE);
                        }
                        else{
                            result_set.setText("Id Found:\n" + result + "\n Name: " + product_names.get(indexx));
                            add_button.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }


    // ALL ADDED FROM WEBSITE - https://github.com/yuriy-budiyev/code-scanner
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setUpPermissions(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
        }
    }

    public void add_product(View view) {
        System.out.println("-----" + result_str);
        recieved_intent.putExtra("n", result_str);
        setResult(RESULT_OK, recieved_intent);
        finish();
    }

    public void back(View view) {
        recieved_intent.putExtra("n", "-1");
        setResult(RESULT_OK, recieved_intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
}