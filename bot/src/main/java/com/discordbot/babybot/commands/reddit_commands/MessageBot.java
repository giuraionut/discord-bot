package com.discordbot.babybot.commands.reddit_commands;

import com.discordbot.babybot.commands.command_logic.GuildCommand;
import com.discordbot.babybot.commands.command_logic.ICommand;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

public class MessageBot implements ICommand {
    @Override
    public void handle(GuildCommand guildCommand) {
        final User author = guildCommand.getGuildMessageAuthor();

        author.openPrivateChannel().queue(c -> {
            c.sendMessage("Hello, this is our private conversation.\n" +
                    "Here you can use special commands, for example you can log in with your reddit account").queue();
        });

    }

    @Override
    public String getName() {
        return "dm_baby";
    }

    @Override
    public String getHelp() {
        return "```\n" +
                "This command opens a DM with me.\n" +
                "Use it as !dm_baby" +
                "\n```";
    }

    @Override
    public String getCategory() {
        return "general";
    }
}
