package com.discordbot.babybot.commands.command_logic;
import java.util.List;

public interface ICommand {
    void handle(Command command);

    String getName();

    String getHelp();

    default List<String> getAliases() {
        return List.of();
    }
}