package com.discordbot.babybot.commands.math_commands;

import com.discordbot.babybot.commands.command_logic.GuildCommand;
import com.discordbot.babybot.commands.command_logic.ICommand;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FibonacciCommand implements ICommand {
    @Override
    public void handle(GuildCommand guildCommand) {
        TextChannel channel = guildCommand.getGuildChannel();

        try {
            String arg = guildCommand.getArgs().get(0);
            int limit = Integer.parseInt(arg);
            if (limit > 20) {
                limit = 20;
                channel.sendMessage("I limited the generated numbers to 20, because it will take too long else.").queue();
            }
            List<Integer> fibonacci = Stream.iterate(new Integer[]{0, 1}, s -> new Integer[]{s[1], s[0] + s[1]})
                    .limit(limit)
                    .map(s -> s[0]).collect(Collectors.toList());
            channel.sendMessageFormat("First **`%s`** numbers of fibonacci sequence are:\n" +
                    " **`%s`**.", limit, fibonacci).queue();
        } catch (NumberFormatException | IndexOutOfBoundsException ex) {
            channel.sendMessage("Sorry, the argument must be a **`number`**.").queue();
        }
    }

    @Override
    public String getName() {
        return "fibonacci";
    }

    @Override
    public String getCategory() {
        return "math";
    }

    @Override
    public List<String> getAliases() {
        List<String> aliases = new ArrayList<>();
        aliases.add("fib");
        return aliases;
    }

    @Override
    public String getHelp() {
        return "```\n" +
                "This command calculates first n numbers from fibonacci sequence\n" +
                "Use it as !fibonacci n, where n is a number higher than 0." +
                "\n```";
    }
}
