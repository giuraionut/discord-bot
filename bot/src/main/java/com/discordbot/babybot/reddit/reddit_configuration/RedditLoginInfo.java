package com.discordbot.babybot.reddit.reddit_configuration;


public class RedditLoginInfo {
    private String username;
    private String password;

    public RedditLoginInfo(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public RedditLoginInfo() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "RedditLoginInfo{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
