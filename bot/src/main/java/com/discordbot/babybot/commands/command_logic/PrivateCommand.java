package com.discordbot.babybot.commands.command_logic;

import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

import java.util.List;

public class PrivateCommand implements ICommandJDA {
    private final PrivateMessageReceivedEvent event;
    private final List<String> args;

    public PrivateCommand(PrivateMessageReceivedEvent event, List<String> args) {
        this.event = event;
        this.args = args;
    }

    @Override
    public PrivateChannel getPrivateChannel() {
        return this.event.getChannel();
    }

    public PrivateMessageReceivedEvent getPrivateEvent() {
        return this.event;
    }

    public List<String> getArgs() {
        return this.args;
    }
}
