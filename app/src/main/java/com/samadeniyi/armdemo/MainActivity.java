package com.samadeniyi.armdemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {
    ListView usersList;
    TextView noUsersText;
    ArrayList<String> al = new ArrayList<>();
    int totalUsers = 0;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usersList = (ListView)findViewById(R.id.usersList);
        noUsersText = (TextView)findViewById(R.id.noUsersText);

        fetchOnlineUsers();

        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserDetails.chatWith = al.get(position);
                startActivity(new Intent(MainActivity.this, ChatActivity.class));
            }
        });
        CheckNetworkConnection.isNetworkAvailable(this);
    }

    public void fetchOnlineUsers(){
        pd = new ProgressDialog(MainActivity.this, R.style.AppTheme_Dark_Dialog);
        pd.setMessage("Fetching online users...");
        pd.show();

        String url = "https://armdemo-b8b6f.firebaseio.com/users.json";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                doOnSuccess(s);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(MainActivity.this);
        rQueue.add(request);
    }



    public void doOnSuccess(String s){
        System.out.println("Is ArrayList Empty 1: "+al.isEmpty());
        if(!al.isEmpty()){
            al.clear();
            System.out.println("Is ArrayList Empty 2: "+al.isEmpty());
        }
        try {
            JSONObject obj = new JSONObject(s);

            Iterator i = obj.keys();
            String key = "";

            while(i.hasNext()){
                key = i.next().toString();
                String loggedInUsers = (String) obj.getJSONObject(key).getString("isLoggedIn");
                if(loggedInUsers == "true"){
                    if(!key.equals(UserDetails.username)) {
                        al.add(key);
                    }
                }
                totalUsers++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(totalUsers <=1){
            noUsersText.setVisibility(View.VISIBLE);
            usersList.setVisibility(View.GONE);
        }
        else{
            noUsersText.setVisibility(View.GONE);
            usersList.setVisibility(View.VISIBLE);
            usersList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, al));
        }
        pd.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_logout:
                if(Logout.logout()){
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    Toast.makeText(getBaseContext(), "You have been logged out", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getBaseContext(), "Could not log you out at the moment", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.action_refresh:
                fetchOnlineUsers();
                break;
        }
        return true;
    }

}
