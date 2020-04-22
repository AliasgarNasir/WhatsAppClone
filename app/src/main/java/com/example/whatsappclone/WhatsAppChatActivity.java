package com.example.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class WhatsAppChatActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView chatListView;
    private ArrayList<String> chatsList;
    private ArrayAdapter arrayAdapter;
    private String selectedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_app_chat);

        selectedUser = getIntent().getStringExtra("selectedUser");
        FancyToast.makeText(this,"Chat with " + selectedUser ,FancyToast.LENGTH_SHORT,FancyToast.INFO,false).show();

        findViewById(R.id.btnSend).setOnClickListener(this);

        chatListView = findViewById(R.id.chatListView);
        chatsList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter(this , android.R.layout.simple_list_item_1,chatsList);
        chatListView.setAdapter(arrayAdapter);

        try {
            ParseQuery<ParseObject> firstUserChatQuery = ParseQuery.getQuery("chat");
            ParseQuery<ParseObject> secondUserChatQuery = ParseQuery.getQuery("chat");

            firstUserChatQuery.whereEqualTo("waSender", ParseUser.getCurrentUser().getUsername());
            secondUserChatQuery.whereEqualTo("waTargetRecipient", selectedUser);

            firstUserChatQuery.whereEqualTo("waSender", selectedUser);
            secondUserChatQuery.whereEqualTo("waTargetRecipient", ParseUser.getCurrentUser().getUsername());

            ArrayList<ParseQuery<ParseObject>> allQueries = new ArrayList<>();
            allQueries.add(firstUserChatQuery);
            allQueries.add(secondUserChatQuery);

            ParseQuery<ParseObject> myQuery = ParseQuery.or(allQueries);
            myQuery.orderByAscending("createdAt");

            myQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (objects.size() > 0 && e == null) {
                        for (ParseObject chatObject : objects) {
                            String waMessage = chatObject.get("waMessage") + "";
                            if (chatObject.get("waSender").equals(ParseUser.getCurrentUser().getUsername())) {
                                waMessage = ParseUser.getCurrentUser().getUsername() + ": " + waMessage;
                            }
                            if (chatObject.get("waSender").equals(selectedUser)) {
                                waMessage = selectedUser + ": " + waMessage;
                            }
                            chatsList.add(waMessage);
                        }
                        arrayAdapter.notifyDataSetChanged();

                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {

        final EditText edtMessage = findViewById(R.id.edtSend);

        ParseObject chat = new ParseObject("chat");
        chat.put("waSender", ParseUser.getCurrentUser().getUsername());
        chat.put("waTargetRecipient",selectedUser);
        chat.put("waMessage",edtMessage.getText().toString());
        chat.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null){
                    FancyToast.makeText(WhatsAppChatActivity.this,
                            "Message from " + ParseUser.getCurrentUser().getUsername() + " send to " + selectedUser,
                            FancyToast.LENGTH_SHORT,FancyToast.INFO,false).show();
                    chatsList.add(ParseUser.getCurrentUser().getUsername() + ": " + edtMessage.getText().toString());
                    arrayAdapter.notifyDataSetChanged();
                    edtMessage.setText("");
                }
            }
        });
    }
}
