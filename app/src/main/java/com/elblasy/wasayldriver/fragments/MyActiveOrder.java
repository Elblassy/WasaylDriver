package com.elblasy.wasayldriver.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.elblasy.wasayldriver.R;
import com.elblasy.wasayldriver.adapter.ActiveOrderAdapter;
import com.elblasy.wasayldriver.adapter.OrdersAdapter;
import com.elblasy.wasayldriver.model.Orders;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MyActiveOrder extends Fragment {


    private ListView listView;
    private ArrayList<Orders> ordersList;
    DatabaseReference databaseReference;
    ActiveOrderAdapter ordersAdapter;

    public MyActiveOrder() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ordersList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("orders");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_active_order, container, false);

        listView = rootView.findViewById(R.id.list_item);
        ordersAdapter = new ActiveOrderAdapter(rootView.getContext(), ordersList);
        listView.setAdapter(ordersAdapter);


        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Orders orders = dataSnapshot1.getValue(Orders.class);
                    if (orders != null) {
                        if (orders.isActive() && orders.getDriversID().equals("010"))
                            ordersList.add(orders);

                    }
                    ordersAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        databaseReference.addValueEventListener(valueEventListener);
        return rootView;
    }

}
