package com.elblasy.wasayldriver;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.elblasy.wasayldriver.utiles.LocaleUtils;
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
    RadioGroup radioGroup;
    RadioButton radioButton;

    public Sign() {
        LocaleUtils.updateConfig(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        auth = FirebaseAuth.getInstance();
        phoneNumber = findViewById(R.id.phone_number);
        username = findViewById(R.id.user_name);
        button = findViewById(R.id.signin);
        spinnerCities = findViewById(R.id.spinnerCities);
        radioGroup = findViewById(R.id.radio);

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
                Toast.makeText(getApplicationContext(), "من فضلك ادخل اسمك صحيح", Toast.LENGTH_LONG).show();
                return;
            }

            if (TextUtils.isEmpty(mPhoneNumber) || mPhoneNumber.length() < 11) {
                Toast.makeText(getApplicationContext(), "من فضلك ادخل رقم موبيلك بشكل صحيح", Toast.LENGTH_LONG).show();
                return;
            }

            if (TextUtils.isEmpty(mCity)) {
                Toast.makeText(getApplicationContext(), "من فضلك اختر المدينة التي ستعمل بها", Toast.LENGTH_LONG).show();
                return;
            }

            if (radioGroup.getCheckedRadioButtonId() == -1) {
                Toast.makeText(getApplicationContext(), "من فضلك اختار نوع وسيلة النقل", Toast.LENGTH_LONG).show();
                return;
            }

            int radioID = radioGroup.getCheckedRadioButtonId();
            radioButton = findViewById(radioID);

            Intent intent1 = new Intent(Sign.this, VerifyPhoneNumber.class);
            intent1.putExtra("mobile", mPhoneNumber);
            intent1.putExtra("user", mUserName);
            intent1.putExtra("city", mCity);
            intent1.putExtra("driver", radioButton.getText());

            startActivity(intent1);

        });


    }
}

