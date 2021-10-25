package com.discordbot.babybot.commands.music_commands;

import com.discordbot.babybot.commands.command_logic.Command;
import com.discordbot.babybot.commands.command_logic.ICommand;
import com.discordbot.babybot.music.GuildMusicManager;
import com.discordbot.babybot.music.MilliToTime;
import com.discordbot.babybot.music.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class ResumeTrackCommand implements ICommand {
    @Override
    public void handle(Command command) {
        TextChannel textChannel = command.getChannel();

        Member member = command.getMember();
        GuildVoiceState memberVoiceState = member.getVoiceState();

        if (memberVoiceState != null && !memberVoiceState.inVoiceChannel()) {
            textChannel.sendMessage("Sorry, but you have to be in a **`voice channel`** to **`pause a track`**.").queue();
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
                            "We have to be in the same voice channel if you want to pause a track.").queue();
            return;
        }
        GuildMusicManager guildMusicManager = PlayerManager.getInstance().getGuildMusicManager(command.getGuild());
        if (guildMusicManager.trackScheduler.player.getPlayingTrack() == null) {
            textChannel.sendMessage("I'm not playing anything at this moment.").queue();
            return;
        }
        if (!guildMusicManager.trackScheduler.player.isPaused()) {
            textChannel.sendMessage("The track is already playing.\n" +
                    "If you would like to pause it type **`!pause`**.").queue();
            return;
        }
        guildMusicManager.trackScheduler.player.setPaused(false);
        String duration = MilliToTime.convert(guildMusicManager.trackScheduler.player.getPlayingTrack().getDuration());
        String position = MilliToTime.convert(guildMusicManager.trackScheduler.player.getPlayingTrack().getPosition());
        String remaining = MilliToTime.convert(guildMusicManager.trackScheduler.player.getPlayingTrack().getDuration() -
                guildMusicManager.trackScheduler.player.getPlayingTrack().getPosition());
        textChannel.sendMessage("Ok, I resumed the current track at **`" + position + "`**.\n" +
                "If you would like to pause it type **`!pause`**.\n" +
                "Time left until track ends: **`" + remaining + "`**.").queue();
    }

    @Override
    public String getName() {
        return "resume";
    }

    @Override
    public String getHelp() {
        return "```\n" +
                "This command will resume the current track\n" +
                "Use it as: !resume\n" +
                "Prerequisites: Be in a voice channel" +
                "\n```";
    }
}