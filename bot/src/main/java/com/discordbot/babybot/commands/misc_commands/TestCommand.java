package com.discordbot.babybot.commands.misc_commands;

import com.discordbot.babybot.commands.command_logic.Command;
import com.discordbot.babybot.commands.command_logic.ICommand;

public class TestCommand implements ICommand {

    @Override
    public void handle(Command command) {
    }

    @Override
    public String getName() {
        return "test";
    }

    @Override
    public String getHelp() {
        return null;
    }
}
