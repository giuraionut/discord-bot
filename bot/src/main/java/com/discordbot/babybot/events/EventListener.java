package com.discordbot.babybot.events;

import com.discordbot.babybot.command_logic.CommandsCollection;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventListener extends ListenerAdapter {
    private static final Logger log = LoggerFactory.getLogger(EventListener.class);
    private final CommandsCollection fromCollection = new CommandsCollection();

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        log.info("{} is online", event.getJDA().getSelfUser());
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        User user = event.getAuthor();

        if (user.isBot() || event.isWebhookMessage()) {
            return;
        }
        String prefix = "!";
        String raw = event.getMessage().getContentRaw();

        if (raw.startsWith(prefix)) {
            fromCollection.handle(event);
        }

    }
}
