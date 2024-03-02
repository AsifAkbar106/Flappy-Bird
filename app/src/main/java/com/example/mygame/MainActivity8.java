package com.example.mygame;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;



public class MainActivity8 extends AppCompatActivity {

    private ListView listView;
    public static ArrayList<String> names = new ArrayList<String>() ;
    public static ArrayList<Integer> ratings = new ArrayList<Integer>() ;
    public static ArrayList<String> comments = new ArrayList<String>() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main8); // Set your layout here

        listView = findViewById(R.id.listview2); // Replace with your ListView ID

        extract();
    }

    public void extract() {
        RequestQueue req = Volley.newRequestQueue(this);
        String url = "https://api.myjson.online/v1/records/1fdd0363-a99a-44af-8a69-eb424d610258";
        StringRequest strReq = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        names.clear();
                        comments.clear();
                        ratings.clear();
                        parseJson(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity8.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
        req.add(strReq);
    }

    private void parseJson(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject dataObject = jsonObject.getJSONObject("data");
            JSONArray reviewArray = dataObject.getJSONArray("review");

            String name, comment;
            int rating;

            for (int i = 0; i < reviewArray.length(); i++) {
                JSONObject jsonObject1 = reviewArray.getJSONObject(i);
                name = jsonObject1.getString("username");
                rating = jsonObject1.getInt("rating");
                comment = jsonObject1.getString("comment");

                names.add(name);
                comments.add(comment);
                ratings.add(rating);
            }

            // Create a custom adapter using the custom layout
            ReviewAdapter arrayAdapter = new ReviewAdapter(this, R.layout.list_item_review, names, ratings, comments);
            listView.setAdapter(arrayAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void onBackPressed() {
        // Start MainActivity5 using an Intent
        Intent intent = new Intent(MainActivity8.this, MainActivity5.class);
        startActivity(intent);
        finish();
    }
}

