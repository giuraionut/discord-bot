package com.discordbot.babybot.commands.command_logic;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.sharding.ShardManager;

public interface ICommandJDA {

    default Guild getGuild() {
        return this.getGuildEvent().getGuild();
    }

    default GuildMessageReceivedEvent getGuildEvent() {
        return null;
    }

    default PrivateMessageReceivedEvent getPrivateEvent() {
        return null;
    }

    default PrivateChannel getPrivateChannel() {
        return this.getPrivateEvent().getChannel();
    }

    default TextChannel getGuildChannel() {
        return this.getGuildEvent().getChannel();
    }

    default Message getGuildMessage() {
        return this.getGuildEvent().getMessage();
    }

    default User getGuildMessageAuthor() {
        return this.getGuildEvent().getAuthor();
    }

    default Member getGuildMember() {
        return this.getGuildEvent().getMember();
    }

    default JDA getJDA() {
        if (this.getGuildEvent() == null)
            return this.getPrivateEvent().getJDA();
        return this.getGuildEvent().getJDA();
    }

    default ShardManager getShardManager() {
        return this.getJDA().getShardManager();
    }

    default User getSelfUser() {
        return this.getJDA().getSelfUser();
    }

    default Member getGuildSelfMember() {
        return this.getGuild().getSelfMember();
    }
}
