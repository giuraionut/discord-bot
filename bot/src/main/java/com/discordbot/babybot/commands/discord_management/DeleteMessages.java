package com.discordbot.babybot.commands.discord_management;

import com.discordbot.babybot.commands.command_logic.GuildCommand;
import com.discordbot.babybot.commands.command_logic.ICommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

public class DeleteMessages implements ICommand {
    @Override
    public void handle(GuildCommand guildCommand) {
        final User guildMessageAuthor = guildCommand.getGuildMessageAuthor();
        final Member owner = guildCommand.getGuild().getOwner();
        final TextChannel guildChannel = guildCommand.getGuildChannel();

        if (owner == null) {
            guildChannel.sendMessage("You can't use this command").queue();
            return;
        }
        final User user = owner.getUser();
        if (!guildMessageAuthor.equals(user)) {
            guildChannel.sendMessage("You can't use this command").queue();
            return;
        }
        final List<String> commandArgs = guildCommand.getArgs();
        if (commandArgs.size() == 0) {
            guildChannel.sendMessage("This command needs arguments. Please use !help delete_message for more info.").queue();
            return;
        }
        if (commandArgs.size() == 2) {
            final String s = commandArgs.get(0);
            final String s1 = commandArgs.get(1);

            int amount = Integer.parseInt(s1);

            if (s.equals("last") && amount < 100) {
                try {
                    new Thread(() -> {
                        final List<Message> messages = guildChannel.getHistory().retrievePast(amount + 1)
                                .complete();
                        messages.forEach(m -> m.delete().complete());
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }) {{
                        start();
                    }}.join();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                guildChannel.sendMessage("Last " + amount + " messages deleted!").queue();
                return;
            }
        }
        if (commandArgs.size() == 1) {
            final String s = commandArgs.get(0);
            if (s.equals("last")) {
                guildChannel.getHistory().retrievePast(2).complete().forEach(message -> message.delete().queue());
            }
        }
    }

    @Override
    public String getName() {
        return "delete_message";
    }

    @Override
    public String getHelp() {
        return "```\n" +
                "This command is used to delete messages from a Text Channel\n" +
                "Currently only the owner can use this command\n\n" +
                "Use it as following:\n" +
                "-delete_message last -> deletes last message\n" +
                "-delete_message last x -> deletes last x messages\n" +
                "-delete_message all -> deletes all messages\n" +
                "\n```";
    }

    @Override
    public List<String> getAliases() {
        return List.of("dmsg");
    }

    @Override
    public String getCategory() {
        return "discord management";
    }
}
