package com.example.topandroidrepositories;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.topandroidrepositories.util.Util;

public class RepoDetailActivity extends AppCompatActivity {
    TextView repoProjectNameTextView, repoDescriptionTextView;
    TextView repoOwnerNameTextView;
    ImageView repoAvatarImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_repo_description);

        repoProjectNameTextView = (TextView) findViewById(R.id.repo_name);
        repoDescriptionTextView = (TextView) findViewById(R.id.repo_description);
        repoAvatarImageView = (ImageView) findViewById(R.id.owner_avatar_image);
        repoOwnerNameTextView = (TextView) findViewById(R.id.owner_name);

        String repoName = "";
        String repoDescription = "";
        String repoOwner = "";
        String repoAvatar = "";

        Bundle extras = getIntent().getExtras();
         if (extras != null) {
             repoName = extras.getString("repoName");
             repoDescription = extras.getString("repoDescription");
             repoOwner = extras.getString("repoOwnerName");
             repoAvatar = extras.getString("ownerAvatarUrl");
         }

        repoProjectNameTextView.setText(repoName);
        repoDescriptionTextView.setText(repoDescription);
        repoOwnerNameTextView.setText(repoOwner);
        //a task to load avatar image of the owner
        new Util.DownloadImageTask(repoAvatarImageView)
                .execute(repoAvatar);


    }
}
