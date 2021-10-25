package com.discordbot.babybot.commands.music_commands;

import com.discordbot.babybot.commands.command_logic.Command;
import com.discordbot.babybot.commands.command_logic.ICommand;
import com.discordbot.babybot.music.GuildMusicManager;
import com.discordbot.babybot.music.PlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class SkipTrackCommand implements ICommand {
    @Override
    public void handle(Command command) {
        TextChannel textChannel = command.getChannel();

        Member member = command.getMember();
        GuildVoiceState memberVoiceState = member.getVoiceState();

        if (memberVoiceState != null && !memberVoiceState.inVoiceChannel()) {
            textChannel.sendMessage("Sorry, but you have to be in a voice channel to skip a track.").queue();
            return;
        }

        Member bot = command.getSelfMember();
        GuildVoiceState botVoiceState = bot.getVoiceState();
        VoiceChannel memberVoiceChannel = member.getVoiceState().getChannel();

        if (botVoiceState != null && !botVoiceState.inVoiceChannel()) {
            textChannel.sendMessage("I'm not playing anything at this moment.").queue();
            return;
        } else if (botVoiceState != null && botVoiceState.getChannel() != memberVoiceChannel) {
            textChannel.sendMessage(
                    "I'm in another voice channel right now.\n" +
                            "We have to be in the **`same voice channel`** if you want to **`skip a track`**.").queue();
            return;
        }
        GuildMusicManager guildMusicManager = PlayerManager.getInstance().getGuildMusicManager(command.getGuild());
        AudioTrack playingTrack = guildMusicManager.trackScheduler.player.getPlayingTrack();
        if (playingTrack == null) {
            textChannel.sendMessage("I'm not playing anything at this moment.").queue();
            return;
        }
        String oldTrackAuthor = playingTrack.getInfo().author;
        String oldTrackTitle = playingTrack.getInfo().title;
        guildMusicManager.trackScheduler.nextTrack();
        AudioTrack newPlayingTrack = guildMusicManager.trackScheduler.player.getPlayingTrack();
        String newTrackAuthor = newPlayingTrack.getInfo().author;
        String newTrackTitle = newPlayingTrack.getInfo().title;
        textChannel.sendMessage("Ok, I skipped track:\n" +
                "**`" + oldTrackTitle + "`** by **`" + oldTrackAuthor + "`**.\n" +
                "Now i'm playing:\n" +
                "**`" + newTrackTitle + "`** by **`" + newTrackAuthor + "`**.").queue();
    }

    @Override
    public String getName() {
        return "skip";
    }

    @Override
    public String getHelp() {
        return "```\n" +
                "This command will stop the current track and will play the next one\n" +
                "Use it as: !skip\n" +
                "Prerequisites: Be in a voice channel"+
                "\n```";
    }
}
