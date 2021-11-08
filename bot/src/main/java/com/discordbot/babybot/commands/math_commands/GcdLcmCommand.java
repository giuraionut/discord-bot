package com.discordbot.babybot.commands.math_commands;

import com.discordbot.babybot.commands.command_logic.GuildCommand;
import com.discordbot.babybot.commands.command_logic.ICommand;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GcdLcmCommand implements ICommand {
    @Override
    public void handle(GuildCommand guildCommand) {
        TextChannel channel = guildCommand.getGuildChannel();
        List<String> commandArgs = guildCommand.getArgs();
        if (commandArgs.size() != 0) {
            try {
                List<Integer> integerArgs = commandArgs.stream().map(Integer::parseInt).sorted(Integer::compare).collect(Collectors.toList());
                int product = integerArgs.stream().reduce(1, (a, b) -> a * b);
                Integer cmmdc = integerArgs.stream().reduce(this::gcd).orElse(1);
                channel.sendMessageFormat("GCD for **`%s`** is: **`%s`**.\n" +
                        "LCM for **`%s`** is: **`%s`**.", integerArgs, cmmdc, integerArgs, product / cmmdc).queue();

            } catch (NumberFormatException ex) {
                channel.sendMessageFormat("Sorry, but you can't find cmmdc with any other characters than **`numbers`**").queue();
            }
        } else {
            channel.sendMessage("This guildCommand needs some arguments, for example: **`8 4 9 2**`").queue();
        }
    }

    int gcd(Integer a, Integer b) {
        return IntStream.rangeClosed(1,
                        Math.min(Math.abs(a)
                                , Math.abs(b)))
                .filter(r -> a % r == 0 && b % r == 0)
                .max().orElse(1);
    }

    @Override
    public String getName() {
        return "gcd";
    }

    @Override
    public String getCategory() {
        return "math";
    }

    @Override
    public List<String> getAliases() {
        List<String> aliases = new ArrayList<>();
        aliases.add("lcm");
        aliases.add("cmmdc");
        aliases.add("cmmmc");
        return aliases;
    }

    @Override
    public String getHelp() {
        return "```\n" +
                "This command calculates GCD and LCM\n" +
                "Use it as !gcd args, where args are numbers like 5 8 4, etc" +
                "\n```";
    }
}
