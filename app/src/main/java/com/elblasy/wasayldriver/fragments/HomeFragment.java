package com.elblasy.wasayldriver.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.elblasy.wasayldriver.R;
import com.elblasy.wasayldriver.TrackingOrder;
import com.elblasy.wasayldriver.model.User;
import com.elblasy.wasayldriver.utiles.SharedPref;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeFragment extends Fragment {

    private DatabaseReference databaseReference;
    private int rate, orders;
    private View view;

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
        view = inflater.inflate(R.layout.fragment_home, container, false);

        TextView textDays = view.findViewById(R.id.days);
        TextView textOrders = view.findViewById(R.id.orders);
        RatingBar ratingBar = view.findViewById(R.id.rating);

        databaseReference = FirebaseDatabase.getInstance().getReference("users").
                child("Drivers")
                .child(SharedPref.getSessionValue("City"))
                .child(SharedPref.getSessionValue("PhoneNumber"));


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                rate = user.getRating();
                orders = user.getOrdersCompleted();
                String days = user.getExpiredDate();


                textDays.setText(String.valueOf(days));
                textOrders.setText(String.valueOf(orders));
                ratingBar.setRating(rate);

                SimpleDateFormat sdf = new SimpleDateFormat("dd MM yyyy", new Locale("en"));
                Date strDate = null;
                try {
                    strDate = sdf.parse(days);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (new Date().after(strDate)) {
                    alertDialog();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


        Bitmap decodedByte;
        CircleImageView profileImage = view.findViewById(R.id.profile_image);
        if (!SharedPref.getSessionValue("Image").matches("ar")) {
            byte[] decodedString = Base64.decode(SharedPref.getSessionValue("Image"), Base64.DEFAULT);
            decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } else {
            decodedByte = BitmapFactory.decodeResource(view.getContext().getResources(), R.drawable.ic_person_black);
        }
        profileImage.setImageBitmap(Bitmap.createScaledBitmap(decodedByte, 100, 120, false));

        CardView cardView = view.findViewById(R.id.card);
        cardView.setOnClickListener(v -> {
            Intent intent = new Intent(view.getContext(), TrackingOrder.class);
            startActivity(intent);
        });
        return view;
    }

    private void alertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("يجب عليك تجديد اشتراكك");
        builder.setMessage("انتهت فترة اشتراكك يرجى الاتصال بنا لكي نبلغك بطرق تجديد الاشتراك");
        builder.setCancelable(false);

        AlertDialog alert = builder.create();
        alert.setCancelable(false);
        alert.show();
    }

}
