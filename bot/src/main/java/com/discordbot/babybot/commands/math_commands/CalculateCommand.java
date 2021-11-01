package com.discordbot.babybot.commands.math_commands;

import com.discordbot.babybot.commands.command_logic.Command;
import com.discordbot.babybot.commands.command_logic.ICommand;
import com.discordbot.babybot.math.Node;
import com.discordbot.babybot.utils.Utils;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Arrays;
import java.util.List;

public class CalculateCommand implements ICommand {
    @Override
    public void handle(Command command) {
        final TextChannel channel = command.getChannel();

        List<String> commandArgs = command.getArgs();
        final String commandString = commandArgs.toString();
        final String equation = Utils.formatCommandArgsString(commandString);
        final String regex = "[-.+/*0-9^]+";
        if (!equation.matches(regex)) {
            channel.sendMessage("Sorry, but the expression is wrong.\n" +
                    "Use **`!help calculate`** for more details on how to use this command.").queue();
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
                "Calculates basic arithmetic expressions. Currently it supports the following operations: +, -, *, /, ^\n" +
                "Use it as !calculate expression, for eg: -10/2-3+4*5^2." +
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
