package com.elblasy.wasayldriver.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.elblasy.wasayldriver.R;
import com.elblasy.wasayldriver.TrackingOrder;
import com.elblasy.wasayldriver.adapter.OrdersAdapter;
import com.elblasy.wasayldriver.model.Orders;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class OrdersFragment extends Fragment {

    private ListView listView;
    private ArrayList<Orders> ordersList;
    DatabaseReference databaseReference;
    OrdersAdapter ordersAdapter;
    ArrayList<String> pushedKeys;



    public OrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ordersList = new ArrayList<>();
        pushedKeys = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("orders");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_orders, container, false);

        listView = rootView.findViewById(R.id.list_item);
        ordersAdapter = new OrdersAdapter(rootView.getContext(), ordersList);
        listView.setAdapter(ordersAdapter);


        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Orders orders = dataSnapshot1.getValue(Orders.class);
                    if (orders != null) {
                        if (orders.isActive() && orders.getDriversID().equals("unknown")) {
                            ordersList.add(orders);
                            pushedKeys.add(dataSnapshot1.getKey());
                        }

                    }
                    ordersAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        databaseReference.addValueEventListener(valueEventListener);

//        listView.setOnItemClickListener((parent, view, position, id) -> {
//
//            Intent intent = new Intent(rootView.getContext(), TrackingOrder.class);
//            intent.putExtra("key",pushedKeys.get(position));
//            startActivity(intent);
//        });
        return rootView;
    }


}
