package com.elblasy.wasayldriver.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.elblasy.wasayldriver.R;
import com.elblasy.wasayldriver.TrackingOrder;
import com.elblasy.wasayldriver.model.Orders;
import com.elblasy.wasayldriver.model.Track;
import com.elblasy.wasayldriver.utiles.SharedPref;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        CircleImageView profileImage = view.findViewById(R.id.profile_image);
        byte[] decodedString = Base64.decode(SharedPref.getSessionValue("Image"), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        profileImage.setImageBitmap(decodedByte);

        CardView cardView = view.findViewById(R.id.card);
        cardView.setOnClickListener(v -> {
            Intent intent = new Intent(view.getContext(), TrackingOrder.class);
            startActivity(intent);
        });
        return view;
    }


}
