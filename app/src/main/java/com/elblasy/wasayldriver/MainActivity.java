package com.elblasy.wasayldriver;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.elblasy.wasayldriver.fragments.HomeFragment;
import com.elblasy.wasayldriver.fragments.MyActiveOrder;
import com.elblasy.wasayldriver.fragments.OrdersFragment;
import com.elblasy.wasayldriver.utiles.LocaleUtils;
import com.elblasy.wasayldriver.utiles.SharedPref;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;


public class MainActivity extends AppCompatActivity  {


    private ActionBar toolbar;
    SharedPref sharedPref;

    public MainActivity() {
        LocaleUtils.updateConfig(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = getSupportActionBar();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FirebaseApp.initializeApp(this);

        String subscribeToTopic = SharedPref.getSessionValue("city").trim();
        sharedPref = new SharedPref(this);

        FirebaseMessaging.getInstance().subscribeToTopic(subscribeToTopic);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this,
                instanceIdResult -> {
                    String fcmToken = instanceIdResult.getToken();
                    sharedPref.setPrefToken(fcmToken);
                    Log.e("token", fcmToken);

                });


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

}
