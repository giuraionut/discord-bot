package com.discordbot.babybot.commands.reddit_commands;

import com.discordbot.babybot.commands.command_logic.GuildCommand;
import com.discordbot.babybot.commands.command_logic.ICommand;
import com.discordbot.babybot.database.entities.my_user.MyUser;
import com.discordbot.babybot.http.SelfHttpReq;
import com.discordbot.babybot.reddit.entities.reddit_post.RedditPost;
import com.discordbot.babybot.reddit.reddit_apps.RandomMemeGenerator;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class RandomMeme implements ICommand {
    @Override
    public void handle(GuildCommand guildCommand) {
        final TextChannel channel = guildCommand.getGuildChannel();

        final User author = guildCommand.getGuildMessageAuthor();
        SelfHttpReq selfHttpReq = new SelfHttpReq();
        final MyUser user = (MyUser) selfHttpReq.get("user/", author.getId());
        if (user.getRedditInfoAPI() == null) {
            channel.sendMessageFormat("You must create a Reddit Profile in order to use this command").queue();
            return;
        }
        RandomMemeGenerator randomMemeGenerator = new RandomMemeGenerator(user.getRedditInfoAPI());
        final RedditPost randomMeme = randomMemeGenerator.getRandomMeme();
        final String memeTitle = randomMeme.getTitle();
        final String memeURL = randomMeme.getUrl();
        channel.sendMessageFormat("Meme Title: %s\n" +
                "%s", memeTitle, memeURL).queue();
    }

    @Override
    public String getName() {
        return "meme";
    }

    @Override
    public String getHelp() {
        return "```\n" +
                "This command will fetch a random meme from reddit\n" +
                "Use it as !meme" +
                "Important, you have to set a reddit profile to use this command" +
                "\n```";
    }

    @Override
    public String getCategory() {
        return "reddit";
    }
}
