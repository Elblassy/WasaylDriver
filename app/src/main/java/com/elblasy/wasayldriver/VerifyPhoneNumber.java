package com.elblasy.wasayldriver;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.elblasy.wasayldriver.model.User;
import com.elblasy.wasayldriver.utiles.LocaleUtils;
import com.elblasy.wasayldriver.utiles.SharedPref;
import com.goodiebag.pinview.Pinview;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class VerifyPhoneNumber extends AppCompatActivity {

    private String mVerificationId;
    private Pinview codeInputView;
    private FirebaseAuth mAuth;
    private DatabaseReference mdatabase;
    private String mobile, name, city, driver;
    private SharedPref sharedPref;
    private int driverVichel;

    public VerifyPhoneNumber() {
        LocaleUtils.updateConfig(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone_number);

        mAuth = FirebaseAuth.getInstance();
        mdatabase = FirebaseDatabase.getInstance().getReference("users");
        codeInputView = findViewById(R.id.code);

        sharedPref = new SharedPref(this);

        Intent intent = getIntent();
        mobile = intent.getStringExtra("mobile");
        name = intent.getStringExtra("user");
        city = intent.getStringExtra("city");
        driver = intent.getStringExtra("driver");

        assert driver != null;
        if (driver.matches("نقل خفيف")) {
            driverVichel = 1;
        } else {
            driverVichel = 0;
        }

        final TextView mobileText = findViewById(R.id.mobile);
        mobileText.setText(mobile);

        sendVerificationCode(mobile);

        Button sign = findViewById(R.id.sign);

        sign.setOnClickListener(v -> {
            String code = codeInputView.getValue();

            if (code.isEmpty() || code.length() < 6) {
                Toast.makeText(VerifyPhoneNumber.this, "Enter valid code", Toast.LENGTH_LONG).show();
                return;
            }
            //verifying the code entered manually
            verifyVerificationCode(code);

        });
    }

    //the method is sending verification code
    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+2" + mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
        Log.e("Mobile", mobile);
    }

    //the callback to detect the verification status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                Log.e("Code", code);
                codeInputView.setValue(code);
                //verifying the code
                verifyVerificationCode(code);
            }

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(VerifyPhoneNumber.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            mVerificationId = s;
        }
    };

    private void verifyVerificationCode(String code) {
        try {
            //creating the credential
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

            //signing the user
            signInWithPhoneAuthCredential(credential);
        } catch (Exception e) {
            Toast toast = Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(VerifyPhoneNumber.this, task -> {

                    if (task.isSuccessful()) {

                        //set start date and expired date
                        Date currentDate = new Date();
                        System.out.println("Current time => " + currentDate);

                        SimpleDateFormat df = new SimpleDateFormat("dd MM yyyy");
                        String startDate = df.format(currentDate);

                        Calendar c2 = Calendar.getInstance();
                        c2.setTime(currentDate);
                        c2.add(Calendar.DATE, 7);

                        Date currentDatePlusOne = c2.getTime();

                        String expiredDate = df.format(currentDatePlusOne);


                        User users = new User(name, mobile, city, startDate, expiredDate, 0,
                                0, driverVichel, 0);

                        mdatabase.child("Drivers").child(city).child(mobile).setValue(users);

                        //verification successful we will start the profile activity
                        Intent intent = new Intent(VerifyPhoneNumber.this, VerifyDeriver.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("city", city);
                        sharedPref.setPreferName(name);
                        sharedPref.setPrefPhoneNumber(mobile);
                        sharedPref.setPrefCity(city);
                        startActivity(intent);
                        finish();

                    } else {

                        //verification unsuccessful.. display an error message
                        String message = "Somthing is wrong, we will fix it soon...";

                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            message = "Invalid code entered...";
                        }

                        Toast.makeText(VerifyPhoneNumber.this, message, Toast.LENGTH_LONG).show();

                    }
                });
    }

}
