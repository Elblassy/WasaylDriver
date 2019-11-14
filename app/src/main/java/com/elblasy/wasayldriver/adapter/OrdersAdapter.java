package com.elblasy.wasayldriver.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.elblasy.wasayldriver.R;
import com.elblasy.wasayldriver.model.Orders;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class OrdersAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Orders> listforview;
    private LayoutInflater inflator;
    private View v;
    private ViewHolder holder;

    private EditText time, cost;

    public OrdersAdapter(Context context, ArrayList<Orders> listforview) {
        super();
        this.context = context;
        this.listforview = listforview;
        inflator = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listforview.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        v = convertView;
        Orders orders = listforview.get(position);

        if (convertView == null) {
            //inflate the view for each row of listview
            v = inflator.inflate(R.layout.list_item, parent, false);
            //ViewHolder object to contain myadapter.xml elements
            holder = new ViewHolder();

            holder.address = v.findViewById(R.id.address);
            holder.details = v.findViewById(R.id.place_name);
            holder.card = v.findViewById(R.id.card);

            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        holder.address.setText(orders.getTo());
        holder.details.setText(orders.getPlaceName());
        holder.card.setOnClickListener(v -> {
//            String token = orders.getToken();
//            Intent intent = new Intent(context, SpeakToClient.class);
//            intent.putExtra("token", token);
//            intent.putExtra("userName",orders.getUserName());
//            intent.putExtra("phoneNumber",orders.getPhoneNumber());
//            intent.putExtra("placeName",orders.getPlaceName());
//            context.startActivity(intent);

            myCustomAlertDialog(orders.getPlaceName(), orders.getPhoneNumber());
        });

        return v;
    }

    private class ViewHolder {
        TextView address;
        TextView details;
        CardView card;

    }

    private void myCustomAlertDialog(String placeName, String phoneNumber) {

        Dialog myDialog;
        //implement custom verify_dialog
        myDialog = new Dialog(context);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(R.layout.take_odrder_dialog);


        time = myDialog.findViewById(R.id.time);
        cost = myDialog.findViewById(R.id.cost);

        Button button = myDialog.findViewById(R.id.send);

        button.setOnClickListener(v -> {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("driversAgree")
                    .child(phoneNumber)
                    .child(placeName);

            HashMap<String, Object> hashMap = new HashMap<>();

            reference.push().setValue(hashMap);

            myDialog.dismiss();
        });

        Objects.requireNonNull(myDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        myDialog.show();
    }
}
