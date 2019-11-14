package com.elblasy.wasayldriver;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.elblasy.wasayldriver.fragments.HomeFragment;
import com.elblasy.wasayldriver.fragments.MyActiveOrder;
import com.elblasy.wasayldriver.fragments.OrdersFragment;
import com.elblasy.wasayldriver.model.User;
import com.elblasy.wasayldriver.utiles.LocaleUtils;
import com.elblasy.wasayldriver.utiles.SharedPref;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {


    private Toolbar toolbar;
    SharedPref sharedPref;
    DatabaseReference databaseReference;
    int verifyFromDataBase, verified;
    Dialog myDialog;


    public MainActivity() {
        LocaleUtils.updateConfig(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDialog = new Dialog(MainActivity.this);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(R.layout.verify_dialog);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FirebaseApp.initializeApp(this);
        databaseReference = FirebaseDatabase.getInstance().getReference("users").
                child("Drivers")
                .child(SharedPref.getSessionValue("City"))
                .child(SharedPref.getSessionValue("PhoneNumber"));

        //set start date every time open app
        if (isTimeZoneAutomatic(this)) {
            Date currentDate = new Date();
            System.out.println("Current time => " + currentDate);

            SimpleDateFormat df = new SimpleDateFormat("dd MM yyyy", new Locale("en"));
            String startDate = df.format(currentDate);
            databaseReference.child("startDate").setValue(startDate);

        } else {
            alertDialog(this);
        }


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                verifyFromDataBase = user.getVerificationCode();
                verified = user.getVerified();
                System.out.println("Mainnnnn " + verifyFromDataBase);

                if (verified == 2) {
                    myDialog.dismiss();
                } else {
                    myCustomAlertDialog();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        String subscribeToTopic = SharedPref.getSessionValue("City").replaceAll("\\s+","");
        sharedPref = new SharedPref(this);

        String topic = subscribeToTopic + "light";

        FirebaseMessaging.getInstance().subscribeToTopic(topic);
        System.out.println("Tobic " + topic);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this,
                instanceIdResult -> {
                    String fcmToken = instanceIdResult.getToken();
                    sharedPref.setPrefToken(fcmToken);
                    Log.e("token", fcmToken);

                });

        System.out.println("mainnnianini" + verified);


        // load the store fragment by default
        toolbar.setTitle(getResources().getString(R.string.title_home));
        loadFragment(new HomeFragment());

    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    toolbar.setTitle(getResources().getString(R.string.title_home));
                    loadFragment(new HomeFragment());
                    return true;
                case R.id.navigation_dashboard:
                    toolbar.setTitle(getResources().getString(R.string.title_dashboard));
                    loadFragment(new OrdersFragment());
                    return true;
                case R.id.navigation_notifications:
                    toolbar.setTitle(getResources().getString(R.string.title_Active));
                    loadFragment(new MyActiveOrder());
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void myCustomAlertDialog() {

        EditText editText = myDialog.findViewById(R.id.verify_code);

        Button button = myDialog.findViewById(R.id.button);

        button.setOnClickListener(v -> {
            if (TextUtils.isEmpty(editText.getText().toString())) {
                Toast.makeText(getApplicationContext(), "من فضلك ادخل الكود الذى بلغناك به او قم بالاتصال بنا",
                        Toast.LENGTH_LONG).show();
                return;
            }
            int verifyCode = Integer.valueOf(editText.getText().toString());
            if (verifyCode == verifyFromDataBase) {
                databaseReference.child("verified").setValue(2);
                myDialog.dismiss();
            } else {
                Toast.makeText(getApplicationContext(), "الكود الى دخلته غلط اتأكد تاني",
                        Toast.LENGTH_LONG).show();
            }

        });

        Objects.requireNonNull(myDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        myDialog.setCancelable(false);
        myDialog.show();
    }

    private boolean isTimeZoneAutomatic(Context c) {
        return Settings.Global.getInt(c.getContentResolver(), Settings.Global.AUTO_TIME_ZONE, 0) == 1;
    }

    private void alertDialog(Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("التوقيت المحلي");
        builder.setMessage("يجب عليك ان تجعل توقيت الهاتف تلقائى حسب المنطفة");
        builder.setCancelable(false);

        AlertDialog alert = builder.create();
        alert.setCancelable(false);
        alert.show();
    }
}
