package com.discordbot.babybot.reddit.reddit_configuration;


import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
public class RedditRequestResponse {
    private String after;
    private String before;
    private JsonNode data;

    public void setAfter(JsonNode after) {
        this.after = after.toString().replace("\"", "").trim();
    }

    public void setBefore(JsonNode before) {
        this.before = before.toString().replace("\"", "").trim();
    }
}
