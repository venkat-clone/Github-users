package com.example.github_users.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Binder;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.HttpResponse;
import com.bumptech.glide.Glide;
import com.example.github_users.MainActivity;
import com.example.github_users.R;
import com.example.github_users.User_Screen;
import com.example.github_users.template.UserTemplate;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class userListAdapter extends RecyclerView.Adapter<userListAdapter.viewHolder> {

    public ArrayList<UserTemplate.Item> items ;
    public Context context;
    public Context baseContext;

    public userListAdapter(Context context,ArrayList<UserTemplate.Item> items,Context baseContext){
        this.items = items;
        this.context = context;
        this.baseContext = baseContext;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.listitem,parent,false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {


//        holder.imageView.setImageURI(Uri.parse(items.get(position).avatar_url));

        Glide.with(context).load(items.get(position).avatar_url).circleCrop().into(holder.imageView);
        holder.name.setText(items.get(position).login);
        holder.decreption.setText(items.get(position).login);
        holder.item = items.get(position);
        holder.context = context;
    }


    @Override
    protected void finalize() throws Throwable {
        Log.v("me","finialized");
        super.finalize();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class viewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView name;
        TextView decreption;
        UserTemplate.Item item;
        Context context;

        public viewHolder(@NonNull View itemView) {

            super(itemView);
            imageView = itemView.findViewById(R.id.user_logo);
            name = itemView.findViewById(R.id.user_name);
            decreption = itemView.findViewById(R.id.user_desreption);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            Log.v("Adapter","clicked");
            Intent intent = new Intent(context, User_Screen.class);
            intent.putExtra("login",item.login);
            context.startActivity(intent);
        }
    }
}
