package com.example.github_users.Adapter;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.github_users.R;
import com.example.github_users.template.UserTemp;

import java.util.ArrayList;

public class RepoAdapter extends RecyclerView.Adapter<RepoAdapter.ViewHolder> {
    Context context;
    ArrayList<UserTemp> Repos;

    public RepoAdapter(Context context, ArrayList<UserTemp> repos) {
        this.context = context;
        Repos = repos;
    }

    @NonNull
    @Override
    public RepoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.repo,parent,false);

        return new ViewHolder(view);
//        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RepoAdapter.ViewHolder holder, int position) {
        UserTemp userTemp = Repos.get(position);
        holder.created.setText(userTemp.created_at);
        holder.name.setText(userTemp.name);
        holder._decription.setText(userTemp.description);
        holder.updated.setText(userTemp.updated_at);
        holder.context = context;
        holder.url = userTemp.url;
        holder.clone_url = userTemp.clone_url;
    }

    @Override
    public int getItemCount() {
        return Repos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        Context context;
        TextView name;
        TextView _decription;
        TextView created;
        TextView updated;
        ImageView open;
        ImageView copy;
        String url;
        String clone_url;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.reponame);
            _decription = itemView.findViewById(R.id.Repo_Decreption);
            created = itemView.findViewById(R.id.RepoCreated);
            updated = itemView.findViewById(R.id.RepoUpdated);
            open =itemView.findViewById(R.id.repoOpen);
            copy = itemView.findViewById(R.id.repoCopy);
            open.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent1= new Intent(Intent.ACTION_VIEW);
                            intent1.setData(Uri.parse(url));
                            context.startActivity(intent1);
                        }

                    }
            );
            copy.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("label", clone_url);
                            clipboardManager.setPrimaryClip(clip);
                            Toast.makeText(context, "url copied to clipbord", Toast.LENGTH_SHORT).show();
                        }
                    }
            );



        }
    }
}
