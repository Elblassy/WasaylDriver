package com.elblasy.wasayldriver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SplashScreen extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener mAuthListner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        auth = FirebaseAuth.getInstance();


        mAuthListner = firebaseAuth -> {
            if (firebaseAuth.getCurrentUser()!=null)
            {
                startActivity(new Intent(SplashScreen.this, MainActivity.class));
                finish();
            }else {
                startActivity(new Intent(SplashScreen.this, Sign.class));
                finish();
            }
        };

        new Handler().postDelayed(() -> auth.addAuthStateListener(mAuthListner),3000);
    }
}
