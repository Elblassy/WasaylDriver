package com.elblasy.wasayldriver;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.toolbox.JsonObjectRequest;
import com.elblasy.wasayldriver.adapter.MessageListAdapter;
import com.elblasy.wasayldriver.fragments.HomeFragment;
import com.elblasy.wasayldriver.model.Message;
import com.elblasy.wasayldriver.model.MySingleton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SpeakToClient extends AppCompatActivity {

    final String TAG = "NOTIFICATION TAG";
    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAMgUYmxc:APA91bHLBfsU1MbJwOCcpW-6g5cg11cQZ901bbPVY83Dh2Td_Q79B7V-YDchmdg4qC9S92TRLPjRnxArXCySyZ1Pw4QNQlZHI4q_iNVeHlMcGltltCrMjEZFqBpblE1J_ECPkDiS0AiP";
    final private String contentType = "application/json";


    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;
    String token, phoneNumber, myToken;

    EditText editText;
    ListView listView;
    private MessageListAdapter mAdapter;
    private ArrayList<Message> messageList;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speak_to_client);


        editText = findViewById(R.id.edit_text);
        ImageView send = findViewById(R.id.send);
        listView = findViewById(R.id.list_view);

        send.setVisibility(View.GONE);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (count == 0)
                    send.setVisibility(View.GONE);
                else
                    send.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        messageList = new ArrayList<>();

        mAdapter = new MessageListAdapter(this, messageList);
        listView.setAdapter(mAdapter);

        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        phoneNumber = intent.getStringExtra("phoneNumber");
        String placeName = intent.getStringExtra("placeName");

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this,
                instanceIdResult -> {
                    myToken = instanceIdResult.getToken();
                    Log.e("myToken", myToken);

                });

        reference = FirebaseDatabase.getInstance().getReference("chats").child(phoneNumber).child(placeName);

        loadMessages();

        send.setOnClickListener(v -> {
            if (!editText.getText().toString().matches("")) {
                NOTIFICATION_MESSAGE = editText.getText().toString();
                NOTIFICATION_TITLE = placeName;

                sendMessage("Driver", "Client", editText.getText().toString());
                //TOPIC = "/topics/wasayldrivers"; //topic must match with what the receiver subscribed to

                JSONObject notification = new JSONObject();
                JSONObject notifcationBody = new JSONObject();
                try {
                    notifcationBody.put("title", NOTIFICATION_TITLE);
                    notifcationBody.put("message", NOTIFICATION_MESSAGE);

                    notification.put("to", token);
                    notification.put("data", notifcationBody);

                } catch (JSONException e) {
                    Log.e(TAG, "onCreate: " + e.getMessage());
                }

                System.out.println(notification);
                sendNotification(notification);
            }
        });

    }

    private void sendMessage(String sender, final String receiver, String message) {
        Message message1 = new Message();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("token", myToken);

        reference.push().setValue(hashMap);

        message1.setTitle(sender);
        message1.setMessage(message);
        message1.setSelf(true);

        editText.setText("");

    }

    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                response -> {
                    Log.i(TAG, "onResponse: " + response.toString());
//                    Intent intent = new Intent(Main.this, HomeFragment.class);
//                    startActivity(intent);
//                    finish();
                },
                error -> {
                    Toast.makeText(SpeakToClient.this, "Request error", Toast.LENGTH_LONG).show();
                    Log.i(TAG, "onErrorResponse: Didn't work");
                }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                System.out.println("TRUE");
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void loadMessages() {

        Query messageQuery = reference.limitToLast(10);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String userKey = user.getPhoneNumber();

        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Message messages = dataSnapshot.getValue(Message.class);

                String message = dataSnapshot.child("Order").child("driverNumber").getValue(String.class);
                if (message != null) {
                    if (message.matches(userKey)) {
                        Intent intent = new Intent(SpeakToClient.this, HomeFragment.class);
                        startActivity(intent);
                        finish();
                    }
                }

                if (messages.getSender().matches("Driver")) {
                    messages.setSelf(true);
                }else {
                    token = messages.getToken();
                }


                messages.setTitle(messages.getSender());
                messageList.add(messages);
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
