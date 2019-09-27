package com.elblasy.wasayldriver;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.elblasy.wasayldriver.model.Orders;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TrackingOrder extends AppCompatActivity {

    Button deliveredButton;
    DatabaseReference reference;
    String value = "nothing";
    DatabaseReference db;
    ValueEventListener valueEventListener;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_order);

        Intent intent = getIntent();
        key = intent.getStringExtra("key");
        View onWayToPlace = findViewById(R.id.circle_way_to_place);
        View onWayToHome = findViewById(R.id.on_way_to_home);
        View delivered = findViewById(R.id.delivered);


        db = FirebaseDatabase.getInstance().getReference("ClientOrders").child("+201660279222");

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Orders orders = dataSnapshot1.getValue(Orders.class);
                    if (orders != null) {
                        if (orders.getPlaceName().equals("home")) {
                            dataSnapshot1.getRef().child("status").setValue(value);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        };

        db.addValueEventListener(valueEventListener);

        deliveredButton = findViewById(R.id.change_status);
        deliveredButton.setOnClickListener(v -> {
            switch (value) {
                case "nothing":
                    value = "way_to_place";
                    updateStatus(value);
                    onWayToPlace.setBackground(getResources().getDrawable(R.drawable.solid_circle));
                    onWayToHome.setBackground(getResources().getDrawable(R.drawable.strock_circle));
                    delivered.setBackground(getResources().getDrawable(R.drawable.strock_circle));
                    deliveredButton.setText("On Way to Home");
                    break;
                case "way_to_place":
                    value = "way_to_home";
                    updateStatus(value);
                    onWayToPlace.setBackground(getResources().getDrawable(R.drawable.solid_circle));
                    onWayToHome.setBackground(getResources().getDrawable(R.drawable.solid_circle));
                    delivered.setBackground(getResources().getDrawable(R.drawable.strock_circle));
                    deliveredButton.setText("Delivered");
                    break;
                case "way_to_home":
                    value = "delivered";
                    updateStatus(value);
                    onWayToPlace.setBackground(getResources().getDrawable(R.drawable.solid_circle));
                    onWayToHome.setBackground(getResources().getDrawable(R.drawable.solid_circle));
                    delivered.setBackground(getResources().getDrawable(R.drawable.solid_circle));
                    deliveredButton.setText("Done");
                    break;

            }
        });


    }

    private void updateStatus(String value) {
        db.child("-LopkxMkmOYvSTVZqgJ6").child("status").setValue(value);
    }


}

