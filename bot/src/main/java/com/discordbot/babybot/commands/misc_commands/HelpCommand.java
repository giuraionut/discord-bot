package com.discordbot.babybot.commands.misc_commands;

import com.discordbot.babybot.commands.command_logic.Command;
import com.discordbot.babybot.commands.command_logic.CommandsCollection;
import com.discordbot.babybot.commands.command_logic.ICommand;
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
            commandsCollection.getCommandList().forEach(iCommand -> commandsName.add("\n"+iCommand.getName()));
            channel.sendMessageFormat("```\n" +
                    "Hello, I'll show you a list of commands that you can use.\n" +
                            "You can use the following commands: %s",
                    commandsName.toString()
                            .replace("[","")
                            .replace("]","")
                            .replace(",",""))
                    .append("\nType !help command_name for more details about a particular command.")
                    .append("\n```").queue();
            return;
        }

        String input = commandArgs.get(0);
        ICommand iCommand = commandsCollection.getCommand(input);

        if (iCommand == null) {
            channel.sendMessageFormat("Sorry, I don't recognize the **`%s`** command, can you try another one?", input).queue();
            return;
        }
        if(iCommand.getHelp() == null) {
            channel.sendMessage("Right now I don't know what this command is doing, check later.").queue();
            return;
        }
        channel.sendMessage(iCommand.getHelp()).queue();

    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public List<String> getAliases() {
        List<String> aliases = new ArrayList<>();
        aliases.add("baby");
        aliases.add("babybot");
        aliases.add("bot");
        return aliases;
    }

    @Override
    public String getHelp() {
        return "```\n" +
                "This command returns all the available commands if you use it as: !help\n" +
                "If you use this command as : !help command_name, you can get more info about any command" +
                "\n```";
    }
}
