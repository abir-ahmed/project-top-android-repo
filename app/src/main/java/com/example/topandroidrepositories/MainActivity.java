package com.example.topandroidrepositories;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.topandroidrepositories.adapter.RepoAdapter;
import com.example.topandroidrepositories.loader.RepoLoader;
import com.example.topandroidrepositories.model.Repo;
import com.example.topandroidrepositories.util.Util;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Repo>> {
    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = Util.class.getSimpleName();

    private TextView noInternetTextMessage;
    private View mLoadingProgress;
    private Button searchButton;

    private RecyclerView mRecyclerView;
    private RepoAdapter mRepoAdapter;
    private ArrayList<Repo> mRepos;
    private LoaderManager mLoaderManager;

    private int mPage = 1;
    private static String mGithubUrl = "https://api.github.com/search/repositories?q=created:>2017-10-22&sort=stars&order=desc";
    private static final int REPO_LOADER_ID = 1;

    private RepoAdapter.RecyclerViewClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.repos_recycler_view);

        noInternetTextMessage = (TextView) findViewById(R.id.tv_no_internet);
        mLoadingProgress = findViewById(R.id.loading_indicator);

        searchButton = (Button) findViewById(R.id.btnSearch);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initialize();
            }
        });

    }

    private void initialize() {
        setAdapter();
        mLoadingProgress.setVisibility(View.VISIBLE);

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            mLoaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for the bundle.
            // Pass in this activity for the LoaderCallbacks parameter (which is valid because this activity implements the LoaderCallbacks interface).
            mLoaderManager.initLoader(REPO_LOADER_ID, null, this);

        } else {
            mLoadingProgress = findViewById(R.id.loading_indicator);
            mLoadingProgress.setVisibility(View.GONE);

            noInternetTextMessage.setText(R.string.no_internet_connection);
        }
    }

    private void setAdapter() {
        try {
            setOnClickListener();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mRepos = new ArrayList<>();

        mRepoAdapter = new RepoAdapter(this, mRepos, listener);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mRecyclerView.setAdapter(mRepoAdapter);
    }

    private void setOnClickListener() {
        listener = new RepoAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getApplicationContext(), RepoDetailActivity.class);
                intent.putExtra("repoName", mRepos.get(position).getRepoName());
                intent.putExtra("repoDescription", mRepos.get(position).getRepoDescription());
                intent.putExtra("repoOwnerName", mRepos.get(position).getRepoOwnerName());
                intent.putExtra("ownerAvatarUrl", mRepos.get(position).getOwnerAvatarUrl());
                startActivity(intent);
            }
        };
    }

    @Override
    public Loader<List<Repo>> onCreateLoader(int i, Bundle bundle) {
        Uri baseUri = Uri.parse(mGithubUrl);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        return new RepoLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Repo>> loader, List<Repo> repos) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No repos found."
        noInternetTextMessage.setText(R.string.no_repos);

        // If there is a valid list of {@link Repo}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (repos != null && !repos.isEmpty()) {
            mRepoAdapter.addAll(repos);

        }
    }

    @Override
    public void onLoaderReset(Loader<List<Repo>> loader) {

    }

    public void loadNextDataFromApi() {
        mGithubUrl = "https://api.github.com/search/repositories?q=created:>2017-10-22&sort=stars&order=desc";
        mGithubUrl += "&page=" + (++mPage);

        mLoaderManager.restartLoader(REPO_LOADER_ID, null, this);

    }

    public void loadPreviousDataFromApi() {
        mGithubUrl = "https://api.github.com/search/repositories?q=created:>2017-10-22&sort=stars&order=desc";
        String suffix = "";

        if (mPage > 1)
            suffix = "&page=" + (--mPage);

        mGithubUrl += suffix;

        mLoaderManager.restartLoader(REPO_LOADER_ID, null, this);

    }
}