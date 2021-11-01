package com.discordbot.babybot.commands.math_commands;

import com.discordbot.babybot.commands.command_logic.Command;
import com.discordbot.babybot.commands.command_logic.ICommand;
import com.discordbot.babybot.utils.Utils;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class LongestSubstringCommand implements ICommand {
    @Override
    public void handle(Command command) {
        TextChannel channel = command.getChannel();
        List<String> commandArgs = command.getArgs();
        if (commandArgs.size() == 1 && commandArgs.get(0).equals("last")) {

            String lastMessage = channel.getHistory()
                    .retrievePast(2)
                    .complete().get(1)
                    .getContentRaw();
            Result result = calculateLongestSubString(List.of(lastMessage));
            channel.sendMessageFormat("Longest substring is: **`%s`** with length: **`%s`**", result.getContent(), result.getLength()).queue();
            return;
        }
        if (commandArgs.isEmpty()) {
            channel.sendMessage("You must provide some arguments for this command.\n" +
                    "The arguments can be any number or word, sentences, etc").queue();
            return;
        }
        Result result = calculateLongestSubString(commandArgs);
        channel.sendMessageFormat("Longest substring is: **`%s`** with length: **`%s`**", result.getContent(), result.getLength()).queue();
    }

    private Result calculateLongestSubString(List<String> array) {
        String string = Utils.formatCommandArgsString(array.toString());

        List<String> listOfLetters = new ArrayList<>(Arrays.asList(string.split("")));
        List<String> tempList = new ArrayList<>();
        while (!listOfLetters.isEmpty()) {
            StringBuilder temp = new StringBuilder();
            for (String letter : listOfLetters) {
                if (temp.toString().contains(letter)) {
                    break;
                }
                temp.append(letter);
            }
            tempList.add(temp.toString());
            listOfLetters.remove(0);
        }
        String longestSubString = tempList.stream().max(Comparator.comparingInt(String::length)).get();
        int length = longestSubString.length();

        return new Result(length, longestSubString);
    }

    private static class Result {
        int length;
        String content;

        public Result(int length, String content) {
            this.length = length;
            this.content = content;
        }

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }


    @Override
    public String getName() {
        return "longestSubstring";
    }

    @Override
    public String getHelp() {
        return "```\n" +
                "Returns the longest substring from a string\n" +
                "Use it as !longestSubstring string, where string can by any combination of characters and numbers." +
                "\n```";
    }

    @Override
    public String getCategory() {
        return "math";
    }

    @Override
    public List<String> getAliases() {
        return new ArrayList<>(Arrays.asList("ls", "longestSub"));
    }
}
