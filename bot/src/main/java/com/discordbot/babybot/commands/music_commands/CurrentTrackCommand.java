package com.discordbot.babybot.commands.music_commands;

import com.discordbot.babybot.commands.command_logic.GuildCommand;
import com.discordbot.babybot.commands.command_logic.ICommand;
import com.discordbot.babybot.music.GuildMusicManager;
import com.discordbot.babybot.music.PlayerManager;
import com.discordbot.babybot.utils.Utils;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class CurrentTrackCommand implements ICommand {
    @Override
    public void handle(GuildCommand guildCommand) {
        TextChannel textChannel = guildCommand.getGuildChannel();

        Member member = guildCommand.getGuildMember();
        GuildVoiceState memberVoiceState = member.getVoiceState();

        if (memberVoiceState != null && !memberVoiceState.inVoiceChannel()) {
            textChannel.sendMessage("Sorry, but you have to be in a voice channel to get info about the playing track.").queue();
            return;
        }

        Member bot = guildCommand.getGuildSelfMember();
        GuildVoiceState botVoiceState = bot.getVoiceState();
        VoiceChannel memberVoiceChannel = member.getVoiceState().getChannel();

        if (botVoiceState != null && !botVoiceState.inVoiceChannel()) {
            textChannel.sendMessage("I'm not playing anything at this moment.").queue();
            return;
        } else if (botVoiceState != null && botVoiceState.getChannel() != memberVoiceChannel) {
            textChannel.sendMessage(
                    "I'm in another voice channel right now.\n" +
                            "We have to be in the **`same voice channel`** if you want to **`get info about the playing track`**.").queue();
            return;
        }
        GuildMusicManager guildMusicManager = PlayerManager.getInstance().getGuildMusicManager(guildCommand.getGuild());
        AudioTrack playingTrack = guildMusicManager.trackScheduler.player.getPlayingTrack();
        if (playingTrack == null) {
            textChannel.sendMessage("I'm not playing anything at this moment.").queue();
            return;
        }

        long duration = playingTrack.getDuration();
        long position = playingTrack.getPosition();
        textChannel.sendMessage("I'm currently playing:\n" +
                "**`" + playingTrack.getInfo().title + "`** by **`" + playingTrack.getInfo().author + "`**\n")
                .append("Time left: ")
                .append("**`").append(Utils.milliToTime(duration-position)).append("`**.").queue();
    }

    @Override
    public String getName() {
        return "trackinfo";
    }

    @Override
    public String getCategory() {
        return "music";
    }


    @Override
    public String getHelp() {
        return "```\n" +
                "This command will show info about the current playing track\n" +
                "Use it as: !trackinfo\n" +
                "Prerequisites: Be in a voice channel"+
                "\n```";
    }
}
