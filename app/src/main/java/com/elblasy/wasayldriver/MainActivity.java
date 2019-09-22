package com.elblasy.wasayldriver;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.elblasy.wasayldriver.adapter.ActiveOrderAdapter;
import com.elblasy.wasayldriver.fragments.HomeFragment;
import com.elblasy.wasayldriver.fragments.MyActiveOrder;
import com.elblasy.wasayldriver.fragments.OrdersFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;


public class MainActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener,
        OrdersFragment.OnFragmentInteractionListener {


    private ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = getSupportActionBar();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FirebaseApp.initializeApp(this);


        Intent intent = getIntent();
        //String subscribeToTopic = intent.getStringExtra("city").trim();

        FirebaseMessaging.getInstance().subscribeToTopic("portsaid");

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this,
                instanceIdResult -> {
                    String fcmToken = instanceIdResult.getToken();
                    Log.e("token", fcmToken);

                });


        // load the store fragment by default
        toolbar.setTitle("HomeFragment");
        loadFragment(new HomeFragment());

    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    toolbar.setTitle("Home");
                    loadFragment(new HomeFragment());
                    return true;
                case R.id.navigation_dashboard:
                    toolbar.setTitle("Orders");
                    loadFragment(new OrdersFragment());
                    return true;
                case R.id.navigation_notifications:
                    toolbar.setTitle("Active Order");
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


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
