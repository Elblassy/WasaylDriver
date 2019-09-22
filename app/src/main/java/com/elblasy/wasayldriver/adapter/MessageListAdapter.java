package com.elblasy.wasayldriver.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.elblasy.wasayldriver.R;
import com.elblasy.wasayldriver.model.Message;

import java.util.List;

public class MessageListAdapter extends BaseAdapter {

    private Context context;
    private List<Message> messagesItems;

    public MessageListAdapter(Context context, List<Message> navDrawerItems) {
        this.context = context;
        this.messagesItems = navDrawerItems;
    }

    @Override
    public int getCount() {
        return messagesItems.size();
    }

    @Override
    public Object getItem(int position) {
        return messagesItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Message m = messagesItems.get(position);

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (messagesItems.get(position).isSelf()) {
            convertView = mInflater.inflate(R.layout.list_item_message_right,
                    parent, false);
        } else {
            convertView = mInflater.inflate(R.layout.list_item_message_left,
                    parent, false);
        }

        TextView lblFrom = convertView.findViewById(R.id.lblMsgFrom);
        TextView txtMsg = convertView.findViewById(R.id.txtMsg);
        txtMsg.setText(m.getMessage());
        lblFrom.setText(m.getTitle());
        return convertView;
    }

}
