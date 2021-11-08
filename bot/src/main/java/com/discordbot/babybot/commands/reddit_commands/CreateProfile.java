package com.discordbot.babybot.commands.reddit_commands;

import com.discordbot.babybot.commands.command_logic.ICommand;
import com.discordbot.babybot.commands.command_logic.PrivateCommand;
import com.discordbot.babybot.database.entities.my_user.MyUser;
import com.discordbot.babybot.http.SelfHttpReq;
import com.discordbot.babybot.reddit.reddit_configuration.RedditInfoAPI;
import com.discordbot.babybot.reddit.reddit_configuration.RedditLoginInfo;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

public class CreateProfile implements ICommand {
    @Override
    public void handle(PrivateCommand privateCommand) {
        final List<String> commandArgs = privateCommand.getArgs();
        final User author = privateCommand.getPrivateChannel().getUser();
        SelfHttpReq selfHttpReq = new SelfHttpReq();

        final MyUser user = (MyUser) selfHttpReq.get("user/", author.getId());

        final String apiUsername = commandArgs.get(0);
        final String apiPassword = commandArgs.get(1);
        final String userAgent = commandArgs.get(2);
        if (commandArgs.size() == 5) {
            final String username = commandArgs.get(3);
            final String password = commandArgs.get(4);
            user.setRedditLoginInfo(new RedditLoginInfo(username, password));

        }

        user.setRedditInfoAPI(new RedditInfoAPI(apiUsername, apiPassword, userAgent));
        selfHttpReq.post("user", user);

        final PrivateChannel channel = privateCommand.getPrivateChannel();
        channel.sendMessage("You Reddit profile was created successfully").queue();

    }

    @Override
    public String getName() {
        return "reddit_profile";
    }

    @Override
    public String getHelp() {
        return "```\n" +
                "This command creates a reddit profile for you\n" +
                "Use it as !reddit_profile scriptUsername scriptSecret user-agent username password " +
                "You can obtain a scriptUserName and scriptSecret by creating a script on https://www.reddit.com/prefs/apps" +
                "Your user agent needs to be something unique and identifiable" +
                "\n```";
    }

    @Override
    public String getCategory() {
        return "reddit";
    }
}
