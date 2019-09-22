package com.elblasy.wasayldriver.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.elblasy.wasayldriver.R;
import com.elblasy.wasayldriver.SpeakToClient;
import com.elblasy.wasayldriver.model.Orders;

import java.util.ArrayList;

public class ActiveOrderAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Orders> listforview;
    private LayoutInflater inflator;
    private View v;
    private ActiveOrderAdapter.ViewHolder holder;

    private EditText name, message, rate;

    public ActiveOrderAdapter(Context context, ArrayList<Orders> listforview) {
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
            holder = new ActiveOrderAdapter.ViewHolder();

            holder.address = v.findViewById(R.id.address);
            holder.details = v.findViewById(R.id.place_name);
            holder.card = v.findViewById(R.id.card);

            v.setTag(holder);
        } else {
            holder = (ActiveOrderAdapter.ViewHolder) v.getTag();
        }

        holder.address.setText(orders.getTo());
        holder.details.setText(orders.getPlaceName());
        holder.card.setOnClickListener(v -> {
            String token = orders.getToken();
            Intent intent = new Intent(context, SpeakToClient.class);
            intent.putExtra("token", token);
            intent.putExtra("userName",orders.getUserName());
            intent.putExtra("phoneNumber",orders.getPhoneNumber());
            intent.putExtra("placeName",orders.getPlaceName());
            context.startActivity(intent);

        });

        return v;
    }

    private class ViewHolder {
        TextView address;
        TextView details;
        CardView card;

    }
}