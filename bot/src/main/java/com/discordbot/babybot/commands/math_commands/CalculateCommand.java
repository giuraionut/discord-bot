package com.discordbot.babybot.commands.math_commands;

import com.discordbot.babybot.commands.command_logic.GuildCommand;
import com.discordbot.babybot.commands.command_logic.ICommand;
import com.discordbot.babybot.math.Node;
import com.discordbot.babybot.utils.Utils;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Arrays;
import java.util.List;

public class CalculateCommand implements ICommand {
    @Override
    public void handle(GuildCommand guildCommand) {
        final TextChannel channel = guildCommand.getGuildChannel();

        List<String> commandArgs = guildCommand.getArgs();
        final String commandString = commandArgs.toString();
        final String equation = Utils.formatCommandArgsString(commandString);
        final String regex = "[-.+/*0-9^(sqrt)]+";
        if (!equation.matches(regex)) {
            channel.sendMessage("Sorry, but the expression is wrong.\n" +
                    "Use **`!help calculate`** for more details on how to use this guildCommand.").queue();
            return;
        }
        Node tree = Node.parse(equation);
        final Node result = Node.solve(tree);
        channel.sendMessageFormat("I did it!\n" +
                "%s = **`%s`**", equation, result.toString()).queue();
    }

    @Override
    public String getName() {
        return "calculate";
    }

    @Override
    public String getHelp() {
        return "```\n" +
                "Calculates basic arithmetic expressions. Currently it supports the following operations: +, -, *, /, ^, sqrt\n" +
                "Use it as !calculate expression, for eg: -10/2-3+4*5^2+sqrt9." +
                "\n```";
    }

    @Override
    public String getCategory() {
        return "math";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("c", "calc");
    }
}
