package com.discordbot.babybot.commands.command_logic;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class GuildCommand implements ICommandJDA {
    private final GuildMessageReceivedEvent event;
    private final List<String> args;

    public GuildCommand(GuildMessageReceivedEvent event, List<String> args) {
        this.event = event;
        this.args = args;
    }

    @Override
    public Guild getGuild() {
        return this.getGuildEvent().getGuild();
    }

    @Override
    public GuildMessageReceivedEvent getGuildEvent() {
        return this.event;
    }

    public List<String> getArgs() {
        return this.args;
    }
}
