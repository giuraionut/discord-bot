package com.discordbot.babybot.commands.discord_management;

import com.discordbot.babybot.commands.command_logic.GuildCommand;
import com.discordbot.babybot.commands.command_logic.ICommand;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.List;
import java.util.stream.Collectors;

public class DeleteCategory implements ICommand {
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
        final String categoryName = commandArgs.get(0);
        final List<Category> categories = guildCommand.getGuild()
                .getCategories()
                .stream().filter(category -> category.getName().equals(categoryName))
                .toList();
        if (categories.isEmpty()) {
            guildChannel.sendMessage("Sorry, but category " + categoryName + " does not exists!").queue();
            return;
        }
        categories.forEach(category -> {
            category.getTextChannels().forEach(textChannel -> textChannel.delete().queue());
            category.delete().queue();
        });
    }

    @Override
    public String getName() {
        return "delete_category";
    }

    @Override
    public String getHelp() {
        return """
                This command is used to delete CATEGORIES AND TEXT CHANNELS INSIDE THEM
                BE CAREFUL WHILE USING THIS COMMAND
                Use it as following:
                -delete_category my_category -> deletes my_category and all text channels inside it
                """;
    }

    @Override
    public String getCategory() {
        return "discord management";
    }

    @Override
    public List<String> getAliases() {
        return List.of("dcat");
    }
}
