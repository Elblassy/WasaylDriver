package com.elblasy.wasayldriver;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Sign extends AppCompatActivity {

    Button button;
    EditText phoneNumber, username;
    FirebaseAuth auth;
    DatabaseReference users;
    MaterialBetterSpinner spinnerCities;
    List<String> citiesList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        auth = FirebaseAuth.getInstance();
        phoneNumber = findViewById(R.id.phone_number);
        username = findViewById(R.id.user_name);
        button = findViewById(R.id.signin);
        spinnerCities = findViewById(R.id.spinnerCities);

        citiesList = new ArrayList<>();

        citiesList = Arrays.asList(getResources().getStringArray(R.array.Cities));

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(Sign.this,
                android.R.layout.simple_dropdown_item_1line, citiesList);

        spinnerCities.setAdapter(adapter1);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        users = database.getReference("users");


        button.setOnClickListener(v -> {

            String mPhoneNumber = phoneNumber.getText().toString().trim();
            String mUserName = username.getText().toString();
            String mCity = spinnerCities.getText().toString();

            if (TextUtils.isEmpty(mUserName) || mUserName.length() < 2) {
                Toast.makeText(getApplicationContext(), "Please enter a valid User Name", Toast.LENGTH_LONG).show();
                return;
            }

            if (TextUtils.isEmpty(mPhoneNumber) || mPhoneNumber.length() < 11) {
                Toast.makeText(getApplicationContext(), "Please enter a valid mobile", Toast.LENGTH_LONG).show();
                return;
            }

            if (TextUtils.isEmpty(mCity)) {
                Toast.makeText(getApplicationContext(), "Please select your city", Toast.LENGTH_LONG).show();
                return;
            }


            Intent intent1 = new Intent(Sign.this, VerifyPhoneNumber.class);
            intent1.putExtra("mobile", mPhoneNumber);
            intent1.putExtra("user", mUserName);
            intent1.putExtra("city", mCity);

            startActivity(intent1);

        });


    }
}

