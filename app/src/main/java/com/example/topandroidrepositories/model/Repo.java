package com.example.topandroidrepositories.model;

public class Repo {
    private String repoName;
    private String repoDescription;
    private String repoOwnerName;
    private String ownerAvatarUrl;
    private int stargazersCount;

    public Repo(String repoName, String repoDescription, String repoOwnerName, int stargazersCount, String ownerAvatarUrl) {
        this.repoName = repoName;
        this.repoDescription = repoDescription;
        this.repoOwnerName = repoOwnerName;
        this.stargazersCount = stargazersCount;
        this.ownerAvatarUrl = ownerAvatarUrl;
    }

    public String getRepoName() {
        return repoName;
    }

    public String getRepoDescription() {
        return repoDescription;
    }

    public String getRepoOwnerName() {
        return repoOwnerName;
    }

    public int getStargazersCount() {
        return stargazersCount;
    }

    public String getOwnerAvatarUrl() {
        return ownerAvatarUrl;
    }
}
