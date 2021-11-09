package com.discordbot.babybot.commands.discord_management;

import com.discordbot.babybot.commands.command_logic.GuildCommand;
import com.discordbot.babybot.commands.command_logic.ICommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.List;
import java.util.stream.Collectors;

public class DeleteTextChannel implements ICommand {
    @Override
    public void handle(GuildCommand guildCommand) {
        final List<String> commandArgs = guildCommand.getArgs();
        final TextChannel guildChannel = guildCommand.getGuildChannel();

        final User guildMessageAuthor = guildCommand.getGuildMessageAuthor();
        final Member owner = guildCommand.getGuild().getOwner();

        if (owner == null) {
            guildChannel.sendMessage("You can't use this command").queue();
            return;
        }

        final User user = owner.getUser();
        if (!guildMessageAuthor.equals(user)) {
            guildChannel.sendMessage("You can't use this command").queue();
            return;
        }

        if (commandArgs.isEmpty()) {
            guildChannel.sendMessage("This command needs arguments. Please use !help delete_category for more info.").queue();
            return;
        }

        final String textChannelName = commandArgs.get(0);
        final List<TextChannel> textChannels = guildCommand.getGuild()
                .getTextChannels().stream().filter(textChannel -> textChannel.getName().equals(textChannelName))
                .collect(Collectors.toList());
        if (textChannels.isEmpty()) {
            guildChannel.sendMessage("No Text Channel " + textChannelName + "found!").queue();
            return;
        }
        textChannels.forEach(tc -> tc.delete().queue());
    }

    @Override
    public String getName() {
        return "delete_text_channel";
    }

    @Override
    public String getHelp() {
        return "```\n" +
                "This command is used to delete A TEXT CHANNEL\n" +
                "BE CAREFUL WHILE USING THIS COMMAND\n\n" +
                "Use it as following:\n" +
                "-delete_text_channel my_text_channel -> deletes my_text_channel\n" +
                "\n```";
    }

    @Override
    public String getCategory() {
        return "discord management";
    }

    @Override
    public List<String> getAliases() {
        return List.of("dtc");
    }
}
