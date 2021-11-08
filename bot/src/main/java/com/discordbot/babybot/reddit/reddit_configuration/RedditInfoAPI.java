package com.discordbot.babybot.reddit.reddit_configuration;

public class RedditInfoAPI {
    private String apiUsername;
    private String apiPassword;
    private String userAgent;

    public RedditInfoAPI() {
    }

    public RedditInfoAPI(String apiUsername, String apiPassword, String userAgent) {
        this.apiUsername = apiUsername;
        this.apiPassword = apiPassword;
        this.userAgent = userAgent;
    }

    public String getApiUsername() {
        return apiUsername;
    }

    public void setApiUsername(String apiUsername) {
        this.apiUsername = apiUsername;
    }

    public String getApiPassword() {
        return apiPassword;
    }

    public void setApiPassword(String apiPassword) {
        this.apiPassword = apiPassword;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}
