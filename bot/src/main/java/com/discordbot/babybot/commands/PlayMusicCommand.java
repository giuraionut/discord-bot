package com.discordbot.babybot.commands;

import com.discordbot.babybot.command_logic.Command;
import com.discordbot.babybot.command_logic.ICommand;
import com.discordbot.babybot.music.PlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class PlayMusicCommand implements ICommand {
    @Override
    public void handle(Command command) {
        TextChannel textChannel = command.getChannel();

        Member bot = command.getSelfMember();
        Member member = command.getMember();
        GuildVoiceState botVoiceState = bot.getVoiceState();
        GuildVoiceState memberVoiceState = member.getVoiceState();
        AudioManager audioManager = command.getGuild().getAudioManager();
        if (!memberVoiceState.inVoiceChannel()) {
            textChannel.sendMessage("```\n" +
                    "You have to be in a voice channel to use this command" +
                    "\n```").queue();
            return;
        }
        VoiceChannel memberVoiceChannel = member.getVoiceState().getChannel();
        if (!botVoiceState.inVoiceChannel()) {
            audioManager.openAudioConnection(memberVoiceChannel);
        } else if (botVoiceState.getChannel() != memberVoiceChannel) {
            textChannel.sendMessage("```\n" +
                    "I'm in another voice channel right now.\n" +
                    "We have to be in the same voice channel if you want to listen to music" +
                    "\n```").queue();
            return;
        }
        PlayerManager.getInstance().loadAndPlayTrack(textChannel, command.getArgs().get(0));
        ;
    }

    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String getHelp() {
        return "```\n" +
                "This command will play a song\n" +
                "Use it as: !play youtube_link" +
                "\n```";
    }
}
