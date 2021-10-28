package com.discordbot.babybot.events;

import com.discordbot.babybot.commands.command_logic.CommandsCollection;
import com.discordbot.babybot.database.utils.CollectData;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class EventListener extends ListenerAdapter {
    private static final Logger log = LoggerFactory.getLogger(EventListener.class);
    private final CommandsCollection fromCollection = new CommandsCollection();
    private final CollectData collectData = new CollectData();

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        log.info("{} is online", event.getJDA().getSelfUser());
        List<Guild> guilds = event.getJDA().getGuilds();

        new Thread(() -> {
            collectData.collect(guilds);
        }).start();
    }

    @SneakyThrows
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
//            new Thread(() ->{
//            }){{start();}}.join();
        }
    }
}
