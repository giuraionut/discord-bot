package com.discordbot.babybot.commands.discord_management;

import com.discordbot.babybot.commands.command_logic.GuildCommand;
import com.discordbot.babybot.commands.command_logic.ICommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DeleteMessages implements ICommand {

    private final Logger logger = LoggerFactory.getLogger(DeleteMessages.class);

    @Override
    public void handle(GuildCommand guildCommand) throws InterruptedException {
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
        if (commandArgs.isEmpty()) {
            guildChannel.sendMessage("This command needs arguments. Please use !help delete_message for more info.").queue();
            return;
        }
        if (commandArgs.size() == 2) {
            final String s = commandArgs.get(0);
            final String s1 = commandArgs.get(1);

            int amount = Integer.parseInt(s1);

            if (s.equals("last") && amount < 100) {

                new Thread(() -> {
                    final List<Message> messages = guildChannel.getHistory().retrievePast(amount + 1)
                            .complete();
                    messages.forEach(m -> m.delete().complete());
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        logger.trace("Thread for deleting messages", e);
                        Thread.currentThread().interrupt();
                    }
                    guildChannel.sendMessage("Last " + amount + " messages deleted!").queue();
                }).start();
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
        return """
                This command is used to delete messages from a Text Channel
                Currently only the owner can use this command
                Use it as following:
                -delete_message last -> deletes last message
                -delete_message last x -> deletes last x messages
                -delete_message all -> deletes all messages
                """;
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
