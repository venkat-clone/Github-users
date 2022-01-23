package com.example.github_users;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.github_users.Adapter.RepoAdapter;
import com.example.github_users.template.UserTemp;
import com.example.github_users.template.UserTemplate;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Browser;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.github_users.databinding.ActivityUserScreenBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class User_Screen extends AppCompatActivity {

    private ActivityUserScreenBinding binding;
    String _title;
    TextView Uname ;
    TextView followers;
    TextView following;
    TextView RepoCount;
    RecyclerView Repositories;
    ArrayList<UserTemp> userTemps = new ArrayList<UserTemp>();
    RepoAdapter repoAdapter;
    ImageView avathar;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityUserScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        Intent intent =  getIntent();
        _title = intent.getStringExtra("login");
        toolBarLayout.setCollapsedTitleTextColor(R.color.blue_shade);


        toolBarLayout.setTitle(_title);



        Uname = findViewById(R.id.user_name);
        followers = findViewById(R.id.followers);
        following = findViewById(R.id.following);
        RepoCount = findViewById(R.id.repocount);
        avathar = findViewById(R.id.user_avathar);
        getUserData(_title);

        Toolbar toolbar = binding.toolbar;

        setSupportActionBar(toolbar);
        FloatingActionButton fab = binding.fab;

        Repositories = findViewById(R.id.repoList);
//        arrayAdapter = new ArrayAdapter(getBaseContext(),R.layout.repo);

        getrepositories(_title);
        repoAdapter = new RepoAdapter(this,userTemps);
        Repositories.setAdapter(repoAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        Repositories.setLayoutManager(layoutManager);



        ;


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1= new Intent(Intent.ACTION_VIEW);
                intent1.setData(Uri.parse("https://github.com/"+_title));
                startActivity(intent1);
            }
        });
        findViewById(R.id.backbutton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onBackPressed();
                    }
                }
        );



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    void getUserData(String Uname_){
        Log.v("Network","freching");
        String url = "https://api.github.com/users/"+Uname_;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.start();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Uname.setText(response.getString("name"));
                            followers.setText(response.getString("followers"));
                            following.setText(response.getString("following"));
                            RepoCount.setText(response.getString("public_repos"));
                            Glide.with(getBaseContext()).load(response.getString("avatar_url")).centerCrop().into(avathar);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(jsonObjectRequest);
    }

    void getrepositories(String name){
        String url = "https://api.github.com/users/"+name+"/repos";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.start();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.v("responce","responce came");
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                userTemps.add(new UserTemp(response.getJSONObject(i)));
                            }
                            repoAdapter.notifyDataSetChanged();
//                            arrayAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error",error.toString());
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }








}