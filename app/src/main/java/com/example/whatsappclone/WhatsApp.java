package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class WhatsApp extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private ArrayList<String> arrayList;
    private ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_app);

        listView = findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        arrayList = new ArrayList<>();
        arrayAdapter =new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList);


        final SwipeRefreshLayout mySwipeRefreshLayout = findViewById(R.id.swipeContainer);

        try {
            ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
            parseQuery.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
            parseQuery.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if (objects.size() > 0 && e == null){
                        for (ParseUser user : objects){
                            arrayList.add(user.getUsername());
                        }
                        listView.setAdapter(arrayAdapter);
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

        mySwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
                    parseQuery.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
                    parseQuery.whereNotContainedIn("username",arrayList);
                    parseQuery.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> objects, ParseException e) {
                            if (objects.size() > 0 ){
                                if (e == null){
                                    for (ParseUser user : objects){
                                        arrayList.add(user.getUsername());
                                    }
                                    arrayAdapter.notifyDataSetChanged();
                                    if (mySwipeRefreshLayout.isRefreshing()){
                                        mySwipeRefreshLayout.setRefreshing(false);
                                    }
                                }
                            }else{
                                if (mySwipeRefreshLayout.isRefreshing()){
                                    mySwipeRefreshLayout.setRefreshing(false);
                                }
                            }
                        }
                    });

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.my_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.logoutItem:
                FancyToast.makeText(WhatsApp.this, ParseUser.getCurrentUser().getUsername() + " is logged out.",FancyToast.LENGTH_SHORT,FancyToast.INFO,false).show();
                ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Intent intent = new Intent(WhatsApp.this, Login.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(WhatsApp.this,WhatsAppChatActivity.class);
        intent.putExtra("selectedUser",arrayList.get(position));
        startActivity(intent);
    }
}
