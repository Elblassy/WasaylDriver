package com.elblasy.wasayldriver;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.elblasy.wasayldriver.model.User;
import com.elblasy.wasayldriver.utiles.LocaleUtils;
import com.elblasy.wasayldriver.utiles.SharedPref;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashScreen extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener mAuthListner;
    DatabaseReference databaseReference;
    int verified;


    public SplashScreen() {
        LocaleUtils.updateConfig(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        auth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference("users").
                child("Drivers")
                .child(SharedPref.getSessionValue("City"))
                .child(SharedPref.getSessionValue("PhoneNumber"));
        if (auth.getCurrentUser() != null) {
            // Attach a listener to read the data at your profile reference
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    verified = user.getVerified();
                    System.out.println("Splashhhhhhhhhhh22 " + user.getVerified());

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });
        }

        mAuthListner = firebaseAuth -> {
            if (firebaseAuth.getCurrentUser() != null) {


                System.out.println("Splashhhhhhhhhhh " + verified);
                if (verified == 1) {
                    startActivity(new Intent(SplashScreen.this, MainActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashScreen.this, VerifyDeriver.class));
                    finish();
                }
            } else {
                startActivity(new Intent(SplashScreen.this, Sign.class));
                finish();
            }
        };

        new Handler().postDelayed(() -> auth.addAuthStateListener(mAuthListner), 3000);
    }
}
