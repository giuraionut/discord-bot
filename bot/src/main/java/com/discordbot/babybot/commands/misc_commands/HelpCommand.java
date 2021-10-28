package com.discordbot.babybot.commands.misc_commands;

import com.discordbot.babybot.commands.command_logic.Command;
import com.discordbot.babybot.commands.command_logic.CommandsCollection;
import com.discordbot.babybot.commands.command_logic.ICommand;
import com.discordbot.babybot.utils.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class HelpCommand implements ICommand {
    private final CommandsCollection commandsCollection;

    public HelpCommand(CommandsCollection commandsCollection) {
        this.commandsCollection = commandsCollection;
    }

    @Override
    public void handle(Command command) {
        List<String> commandArgs = command.getArgs();
        TextChannel channel = command.getChannel();
        User selfUser = command.getSelfUser();
        if (commandArgs.isEmpty()) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setAuthor(selfUser.getName() + " - help").setThumbnail(selfUser.getAvatarUrl())
                    .setDescription("This is the list of all commands that you can currently use.")
                    .setColor(Utils.randomColor());

            List<ICommand> commandList = commandsCollection.getCommandList();

            Set<String> commandCategories = commandList.stream().map(ICommand::getCategory)
                    .filter(category -> !category.isEmpty())
                    .collect(Collectors.toSet());

            commandCategories.forEach(category ->
                    embedBuilder.addField(
                            category.toUpperCase(),
                            commandList.stream().filter(command1 -> command1.getCategory().equals(category))
                                    .map(ICommand::getName)
                                    .collect(Collectors.toList())
                                    .toString()
                                    .replace("[", "")
                                    .replace("]", "")
                                    .replace(",", "\n"), true));

            embedBuilder.setFooter("You can type !help command_name for more details about any command");
            channel.sendMessageEmbeds(embedBuilder.build()).queue();
            return;
        }

        String input = commandArgs.get(0);
        ICommand iCommand = commandsCollection.getCommand(input);

        if (iCommand == null) {
            channel.sendMessageFormat("Sorry, I don't recognize the **`%s`** command, can you try another one?", input).queue();
            return;
        }
        if (iCommand.getHelp() == null) {
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
    public String getCategory() {
        return "general";
    }

    @Override
    public String getHelp() {
        return "```\n" +
                "This command returns all the available commands if you use it as: !help\n" +
                "If you use this command as : !help command_name, you can get more info about any command" +
                "\n```";
    }
}
