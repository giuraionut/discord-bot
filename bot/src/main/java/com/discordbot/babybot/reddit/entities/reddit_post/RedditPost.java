package com.discordbot.babybot.reddit.entities.reddit_post;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class RedditPost {
    @JsonProperty("title")
    private String title;

    @JsonProperty("selftext")
    private String selftext;

    @JsonProperty("url")
    private String url;

    @JsonProperty("type")
    private String type;

    @JsonProperty("is_video")
    private boolean isVideo;

    @JsonProperty("subreddit")
    private String subreddit;

    @JsonProperty("author")
    private String author;

    @Override
    public String toString() {
        return "RedditPost{\n" +
                "title=" + title +
                "\nurl=" + url +
                "\nselftext=" + selftext +
                "\ntype=" + type +
                "\nisVideo=" + isVideo +
                "\nsubreddit=" + subreddit +
                "\nauthor=" + author +
                "\n}";
    }

    public RedditPost getPostFromNode(JsonNode node) {
        Gson gson = new Gson();
        return gson.fromJson(node.toString(), RedditPost.class);
    }

}
