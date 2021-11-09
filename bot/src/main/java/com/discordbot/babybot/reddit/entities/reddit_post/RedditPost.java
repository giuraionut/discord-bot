package com.discordbot.babybot.reddit.entities.reddit_post;

import com.discordbot.babybot.reddit.RedditGallery;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class RedditPost implements Serializable, RedditGallery {
    @JsonProperty("title")
    private String title;

    @JsonProperty("selftext")
    private String selftext;

    @JsonProperty("url")
    private String url;

    @JsonProperty("type")
    private String type;

    @JsonProperty("is_video")
    private boolean is_video;

    @JsonProperty("is_gallery")
    private boolean is_gallery;

    @JsonProperty("subreddit")
    private String subreddit;

    @JsonProperty("author")
    private String author;

    @JsonProperty("media_metadata")
    private HashMap<Integer,String> galleryLinks;


    @Override
    public String toString() {
        return "RedditPost{\n" +
                "title=" + title +
                "\nurl=" + url +
                "\nselftext=" + selftext +
                "\ntype=" + type +
                "\nis_video=" + is_video +
                "\nis_gallery=" + is_gallery +
                "\nsubreddit=" + subreddit +
                "\nauthor=" + author +
                "\ngallery=" + galleryLinks +
                "\n}";
    }


    public List<RedditPost> getPostFromNode(JsonNode node) {

        List<RedditPost> posts = new ArrayList<>();
        node.forEach(n -> {
            RedditPost post = this.getPostFromData(n);
            if (post.is_gallery) {
                post.galleryLinks = getGalleryFromNode(n);
            }
            posts.add(post);
        });
        return posts;
    }

    public RedditPost getPostFromData(JsonNode data) {
        JsonNode node = data.get("data");
        Gson gson = new Gson();
        return gson.fromJson(node.toString(), RedditPost.class);
    }

    @JsonProperty("is_video")
    public boolean getIs_Video() {
        return is_video;
    }

    @JsonProperty("is_video")
    public void setIs_Video(boolean video) {
        this.is_video = video;
    }

    @JsonProperty("is_gallery")
    public boolean getIs_Gallery() {
        return is_gallery;
    }

    @JsonProperty("is_gallery")
    public void setIs_gallery(boolean is_gallery) {
        this.is_gallery = is_gallery;
    }

}
