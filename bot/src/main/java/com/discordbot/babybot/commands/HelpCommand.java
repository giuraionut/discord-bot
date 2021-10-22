package com.discordbot.babybot.commands;

import com.discordbot.babybot.command_logic.Command;
import com.discordbot.babybot.command_logic.CommandsCollection;
import com.discordbot.babybot.command_logic.ICommand;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.List;

public class HelpCommand implements ICommand {
    private final CommandsCollection commandsCollection;

    public HelpCommand(CommandsCollection commandsCollection) {
        this.commandsCollection = commandsCollection;
    }

    @Override
    public void handle(Command command) {
        List<String> commandArgs = command.getArgs();
        TextChannel channel = command.getChannel();

        if (commandArgs.isEmpty()) {
            List<String> commandsName = new ArrayList<>();
            commandsCollection.getCommandList().forEach(iCommand -> commandsName.add(iCommand.getName()));
            channel.sendMessageFormat("```\n" +
                    "The list of commands is: %s" +
                    "\n```", commandsName).queue();
            return;
        }

        String input = commandArgs.get(0);
        ICommand iCommand = commandsCollection.getCommand(input);

        if (iCommand == null) {
            channel.sendMessageFormat("```\n" +
                    "Command %s does not exists" +
                    "\n```", input).queue();
        }

        channel.sendMessage(iCommand.getHelp()).queue();
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getHelp() {
        return "```\n" +
                "This command returns all the available commands\n" +
                "Use it as : !help command_name" +
                "\n```";
    }
}
