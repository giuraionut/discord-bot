package com.discordbot.babybot.commands.command_logic;

import java.util.List;

public interface ICommand {
    default void handle(GuildCommand guildCommand) {}

    default void handle(PrivateCommand privateCommand) {}

    String getName();

    String getHelp();

    default String getCategory() {
        return "";
    }

    default List<String> getAliases() {
        return List.of();
    }
}
