package com.example.topandroidrepositories.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.topandroidrepositories.R;
import com.example.topandroidrepositories.model.Repo;
import com.example.topandroidrepositories.util.Util;

import java.util.ArrayList;
import java.util.List;

public class RepoAdapter extends RecyclerView.Adapter<RepoAdapter.ViewHolder> {

    private Context mContext;
    private List<Repo> mRepoList = new ArrayList<>();
    private static RecyclerViewClickListener listener;

    public RepoAdapter(Context context, List<Repo> reposList, RecyclerViewClickListener listener) {
        this.mContext = context;
        this.mRepoList = reposList;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView repoName, repoDescription;
        ImageView repoAvatarImage;
        TextView repoStarsNumber;
        TextView repoOwnerNameTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            repoName = (TextView) itemView.findViewById(R.id.repo_name);
            repoDescription = (TextView) itemView.findViewById(R.id.repo_description);
            repoAvatarImage = (ImageView) itemView.findViewById(R.id.owner_avatar_image);
            repoStarsNumber =(TextView) itemView.findViewById(R.id.repo_stargazers);
            repoOwnerNameTextView = (TextView) itemView.findViewById(R.id.owner_name);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (view == null) {
                return;
            } else {
                try {
                    listener.onClick(view, getAdapterPosition());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.repository_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Repo currentRepo = mRepoList.get(position);
        holder.repoName.setText(currentRepo.getRepoName());
        holder.repoDescription.setText(currentRepo.getRepoDescription());
        holder.repoStarsNumber.setText(currentRepo.getStargazersCount()+"");
        holder.repoOwnerNameTextView.setText(currentRepo.getRepoOwnerName());

        //a task to load avatar image of the owner
        new Util.DownloadImageTask(holder.repoAvatarImage)
                .execute(currentRepo.getOwnerAvatarUrl());

    }

    @Override
    public int getItemCount() {
        return mRepoList.size();
    }

    public void addAll(List<Repo> repoList) {
        mRepoList = repoList;
        this.notifyDataSetChanged();
    }

    public interface RecyclerViewClickListener {
        void onClick (View view, int position);
    }

}
