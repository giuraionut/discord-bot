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
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FetchSavedPost implements ICommand {
    @Override
    public void handle(GuildCommand guildCommand) {
        RedditAPI redditAPI = new RedditAPI();
        SelfHttpReq selfHttpReq = new SelfHttpReq();
        final User guildMessageAuthor = guildCommand.getGuildMessageAuthor();
        final Guild guild = guildCommand.getGuild();
        final TextChannel guildChannel = guildCommand.getGuildChannel();
        final MyUser user = (MyUser) selfHttpReq.get("user/", guildMessageAuthor.getId());

        final RedditInfoAPI redditInfoAPI = user.getRedditInfoAPI();
        final RedditLoginInfo redditLoginInfo = user.getRedditLoginInfo();
        redditAPI.setRedditInfoAPI(redditInfoAPI);
        redditAPI.setRedditLoginInfo(redditLoginInfo);
        final List<String> commandArgs = guildCommand.getArgs();
        final int chunkSize = Integer.parseInt(commandArgs.get(0));
        final int chunks = Integer.parseInt(commandArgs.get(1));
        if (chunks > 50) {
            guildChannel.sendMessageFormat("You made too many requests to reddit.\n" +
                    "That is ok, but it may take some time.\n" +
                    "Estimated time: %s minutes", (chunks / 50)).queue();
        }
        final List<RedditPost> redditPosts = redditAPI.requestSelfSavedPosts(chunkSize, chunks);

        final Map<String, List<RedditPost>> map = redditPosts.stream().collect(Collectors.groupingBy(RedditPost::getSubreddit));

        guild.getCategories().stream().filter(c -> c.getName().equals("Reddit")).collect(Collectors.toList()).forEach(c -> {
            c.getTextChannels().forEach(t -> t.delete().queue());
            c.delete().queue();
        });

        map.forEach((key, value) ->
        {
            guild.createTextChannel(key).queue(textChannel ->
                    value.forEach(post -> {
                        textChannel.sendMessage(
                                        formatMessage(post))
                                .queue();
                    }));
        });
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
            post.getGalleryLinks().forEach((key, value) -> {
                message.append(value).append("\n");
            });
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
