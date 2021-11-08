package com.discordbot.babybot.reddit.reddit_apps;

import com.discordbot.babybot.reddit.RedditAPI;
import com.discordbot.babybot.reddit.reddit_configuration.RedditInfoAPI;
import com.discordbot.babybot.reddit.reddit_configuration.RedditLoginInfo;
import com.discordbot.babybot.reddit.entities.reddit_post.RedditPost;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomMemeGenerator {
    private final RedditAPI redditAPI;

    public RandomMemeGenerator(RedditInfoAPI redditInfoAPI) {
        redditAPI = new RedditAPI();
        redditAPI.setRedditInfoAPI(redditInfoAPI);
    }

    public RedditPost getRandomMeme() {
        final List<RedditPost> memes = redditAPI.requestSubRedditPosts("memes", 20, 1);
        final int i = ThreadLocalRandom.current().nextInt(0, 20);
        return memes.get(i);
    }
}
