package com.discordbot.babybot.commands.reddit_commands;

import com.discordbot.babybot.commands.command_logic.GuildCommand;
import com.discordbot.babybot.commands.command_logic.ICommand;
import com.discordbot.babybot.database.entities.my_user.MyUser;
import com.discordbot.babybot.http.SelfHttpReq;
import com.discordbot.babybot.reddit.RedditAPI;
import com.discordbot.babybot.reddit.entities.reddit_post.RedditPost;
import com.discordbot.babybot.reddit.reddit_configuration.RedditInfoAPI;
import com.discordbot.babybot.reddit.reddit_configuration.RedditLoginInfo;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class FetchSavedPost implements ICommand {
    @Override
    public void handle(GuildCommand guildCommand) {
        RedditAPI redditAPI = new RedditAPI();
        SelfHttpReq selfHttpReq = new SelfHttpReq();
        final User guildMessageAuthor = guildCommand.getGuildMessageAuthor();
        final TextChannel guildChannel = guildCommand.getGuildChannel();
        final Guild guild = guildCommand.getGuild();

        final Member owner = guildCommand.getGuild().getOwner();
        if (owner == null) {
            guildChannel.sendMessage("Sorry, you can't use this command!").queue();
            return;
        }
        if (!guildMessageAuthor.equals(owner.getUser()))
        {
            guildChannel.sendMessage("Sorry, you can't use this command!").queue();
            return;
        }

        final MyUser user = (MyUser) selfHttpReq.get("user/", guildMessageAuthor.getId());
        final RedditInfoAPI redditInfoAPI = user.getRedditInfoAPI();
        final RedditLoginInfo redditLoginInfo = user.getRedditLoginInfo();
        redditAPI.setRedditInfoAPI(redditInfoAPI);
        redditAPI.setRedditLoginInfo(redditLoginInfo);

        final List<String> commandArgs = guildCommand.getArgs();
        final int chunkSize = Integer.parseInt(commandArgs.get(0));
        final int chunks = Integer.parseInt(commandArgs.get(1));

        if (chunks > 50) {
            guildChannel.sendMessageFormat("""
                    You made too many requests to reddit.
                    That is ok, but it may take some time.
                    Estimated time: %s minutes
                    """, (chunks / 50)).queue();
        }

        new Thread(() -> {
            final List<RedditPost> redditPosts = redditAPI.requestSelfSavedPosts(chunkSize, chunks);
            final Map<String, List<RedditPost>> map = redditPosts.stream().collect(Collectors.groupingBy(RedditPost::getSubreddit));
            TreeMap<String, List<RedditPost>> treeMap = new TreeMap<>(map);
            final List<SortedMap<String, List<RedditPost>>> listOfMaps = splitMap(treeMap, 50);
            AtomicInteger count = new AtomicInteger();

            listOfMaps.forEach(m ->
                    guild.createCategory("Reddit" + count.getAndIncrement()).queue(category ->
                            m.forEach((key, value) ->
                                    category.createTextChannel(key).queue(tc ->
                                            value.forEach(post ->
                                                    tc.sendMessage(formatMessage(post)).queue())))));

            guildChannel.sendMessage("Your reddit posts have been fetched!").queue();
        }).start();
    }

    private List<SortedMap<String, List<RedditPost>>> splitMap(TreeMap<String, List<RedditPost>> map, long quantity) {
        List<SortedMap<String, List<RedditPost>>> listOfMaps = new ArrayList<>();
        while (!map.isEmpty()) {
            if (quantity >= map.size()) {
                listOfMaps.add(map);
                break;
            }
            final String nthValue = map.keySet().stream().skip(quantity).findFirst().orElse(null);
            final String firstKey = map.firstKey();
            final String nthKey = map.ceilingKey(nthValue);
            final SortedMap<String, List<RedditPost>> subMap = map.subMap(firstKey, nthKey);
            TreeMap<String, List<RedditPost>> newMap = new TreeMap<>();
            newMap.putAll(subMap);
            listOfMaps.add(newMap);
            map.headMap(nthKey).clear();
        }
        return listOfMaps;
    }


    private String formatMessage(RedditPost post) {
        StringBuilder message = new StringBuilder();

        final String title = post.getTitle();
        final String url = post.getUrl();

        message.append("**`").append(title).append("`**");
        message.append("\n");
        message.append(url);
        message.append("\n");
        if (post.getIs_Gallery()) {
            post.getGalleryLinks().forEach((key, value) ->
                    message.append(value).append("\n"));
        }
        return message.toString();
    }

    @Override
    public String getName() {
        return "fetch_saved_posts";
    }

    @Override
    public String getHelp() {
        return null;
    }

    @Override
    public String getCategory() {
        return ICommand.super.getCategory();
    }
}
