package com.discordbot.babybot.commands.misc_commands;

import com.discordbot.babybot.commands.command_logic.GuildCommand;
import com.discordbot.babybot.commands.command_logic.ICommand;

public class TestCommand implements ICommand {

    @Override
    public void handle(GuildCommand guildCommand) {}

    @Override
    public String getName() {
        return "test";
    }

    @Override
    public String getHelp() {
        return null;
    }
}
