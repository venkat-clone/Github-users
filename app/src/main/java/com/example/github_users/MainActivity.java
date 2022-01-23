package com.example.github_users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsoluteLayout;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ViewTarget;
import com.example.github_users.Adapter.userListAdapter;
import com.example.github_users.template.UserTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    UserTemplate userTemplate;
    RecyclerView result_view ;
    AbsoluteLayout _view;
    SearchView _searchView;
    Context context;
    userListAdapter arrayAdapter;
    ProgressBar _progressBar;
    ProgressBar _reloadingBar;
    String Old="";
    boolean results_completed = false ;

    AbsoluteLayout _retry;
    boolean scrooling;
    int visibleItemCount, totalItemCount, pastVisiblesItems,cout=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _searchView = findViewById(R.id.searchView);
        _view = findViewById(R.id.home_text);
        result_view = findViewById(R.id.result_list_);
        _progressBar = findViewById(R.id.progress_circular);
        _progressBar.setProgress(70);
        _reloadingBar = findViewById(R.id.loading);
        _retry = findViewById(R.id.retry_search_view);

        context = this;

        arrayAdapter = new userListAdapter(context, new ArrayList<UserTemplate.Item>(),context);


        result_view.setAdapter(arrayAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        result_view.setLayoutManager(layoutManager);




        result_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    scrooling = true;
                    if(results_completed){
                        if(layoutManager.findLastCompletelyVisibleItemPosition() == layoutManager.getItemCount()-1){
                            findViewById(R.id.end_results).setVisibility(View.VISIBLE);
                        }
                        else{
                            findViewById(R.id.end_results).setVisibility(View.GONE);
                        }
                    }
                }

                Log.v("Scroll","started");


            }

           @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) { //check for scroll down


                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                    if (scrooling) {

                        if(userTemplate.total_count == userTemplate.items.size()){
                            results_completed = true;
                            if(layoutManager.findLastCompletelyVisibleItemPosition() == layoutManager.getItemCount()-1){
                                findViewById(R.id.end_results).setVisibility(View.VISIBLE);
                            }
                            else{
                                findViewById(R.id.end_results).setVisibility(View.GONE);
                            }
                        }
                        else if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            _reloadingBar.setVisibility(View.VISIBLE);
                            quary_user(Old);
                            scrooling = false;
                            Log.v("Scroll","end");
                        }




                        Log.v("scroll",userTemplate.total_count+"::"+userTemplate.items.size());

                    }



                }
            }
        });




        // search quary listiner

        _searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        if(!Old.equals(query) ){
                            arrayAdapter.items = new ArrayList<>();
                            arrayAdapter.notifyDataSetChanged();
                            _progressBar.setVisibility(View.VISIBLE);
                            Log.v("_search","submited");
                            _view.setVisibility(View.GONE);
                            closeOptionsMenu();
                            quary_user(query);
                            Old = query;
                            cout =1;
                        }


                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {

                        return false;
                    }
                }
        );
        // search view on click
        _searchView.setOnCloseListener(
                new SearchView.OnCloseListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public boolean onClose() {
                        closeOptionsMenu();
                        Log.v("Searchview","closed");
                        _view.setVisibility(View.VISIBLE);
                        _progressBar.setVisibility(View.GONE);
                        arrayAdapter.items = new ArrayList<>();
                        arrayAdapter.notifyDataSetChanged();
                        return false;
                    }
                }
        );

        _retry.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        view.setVisibility(View.GONE);
                        _progressBar.setVisibility(View.VISIBLE);
                        quary_user(Old);

                    }
                }
        );


    }

    @Override
    protected void onResume() {
        super.onResume();
        closeOptionsMenu();
        _searchView.clearFocus();
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
        super.onOptionsMenuClosed(menu);
        _searchView.clearFocus();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.v("manu","prepared");
        return super.onPrepareOptionsMenu(menu);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        ConnectivityManager wi = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }

    void quary_user(String q) {

//        if(!isInternetAvailable()){
//            Toast.makeText(this,"internet not connected",Toast.LENGTH_LONG).show();
//            _progressBar.hide();
//
//        }
        String url = "https://api.github.com/search/users?q=" + q +"&page="+cout;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.start();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onResponse(JSONObject response) {

                        if(cout==1){
                            userTemplate = new UserTemplate(response);

                            Log.v("quary",userTemplate.items.size()+"");

                            arrayAdapter.items  = userTemplate.items;
                            arrayAdapter.notifyDataSetChanged();
                            _progressBar.setVisibility(View.GONE);
                        }
                        else{
                            userTemplate.UserTemplate_add(response);
                            for (int i = arrayAdapter.items.size(); i < userTemplate.items.size(); i++) {
                                arrayAdapter.items.add(userTemplate.items.get(i));
                                Log.v("Network","fetting data");
                            }
                            arrayAdapter.notifyDataSetChanged();
                            _reloadingBar.setVisibility(View.GONE);
                            Log.v("Frech","datafited");
                        }
                        Log.v("quary>",userTemplate.items.size()+":");

                        cout++;
                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if(cout==1){
                            _progressBar.setVisibility(View.GONE);
                            _retry.setVisibility(View.VISIBLE);
                        }
                        else {
                            _reloadingBar.setVisibility(View.GONE);

                        }


                        Log.e("Responce", error.toString());
                    }
                });
        requestQueue.add(jsonObjectRequest);

    }


}