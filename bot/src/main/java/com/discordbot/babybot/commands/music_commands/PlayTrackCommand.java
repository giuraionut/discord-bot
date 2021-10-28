package com.discordbot.babybot.commands.music_commands;

import com.discordbot.babybot.commands.command_logic.Command;
import com.discordbot.babybot.commands.command_logic.ICommand;
import com.discordbot.babybot.music.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import java.net.URI;
import java.net.URISyntaxException;

public class PlayTrackCommand implements ICommand {
    @Override
    public void handle(Command command) {
        TextChannel textChannel = command.getChannel();

        String arg = String.join(" ", command.getArgs());
        if (arg.isBlank()) {
            textChannel.sendMessage("Please tell me, what would you like to listen?\n" +
                    "I can play you a track from a **`youtube link`**.\n" +
                    "I can also search for a track if you give me it's **`name`**.").queue();
            return;
        }

        Member member = command.getMember();
        GuildVoiceState memberVoiceState = member.getVoiceState();


        if (memberVoiceState != null && !memberVoiceState.inVoiceChannel()) {
            textChannel.sendMessage("Sorry, but you have to be in a **`voice channel`** if you want to **`listen to a track`**.").queue();
            return;
        }

        Member bot = command.getSelfMember();
        GuildVoiceState botVoiceState = bot.getVoiceState();
        AudioManager audioManager = command.getGuild().getAudioManager();
        VoiceChannel memberVoiceChannel = member.getVoiceState().getChannel();

        if (botVoiceState != null && !botVoiceState.inVoiceChannel()) {
            audioManager.openAudioConnection(memberVoiceChannel);
        } else if (botVoiceState != null && botVoiceState.getChannel() != memberVoiceChannel) {
            textChannel.sendMessage(
                    "I'm in another voice channel right now.\n" +
                            "We have to be in the **`same voice channel`** if you want to **`listen to a track`**.").queue();
            return;
        }

        if (!urlIsValid(arg)) {
            PlayerManager.getInstance().loadAndPlayTrack(textChannel, "ytsearch:" + arg);

        } else {
            PlayerManager.getInstance().loadAndPlayTrack(textChannel, arg);
        }
    }

    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String getCategory() {
        return "music";
    }

    @Override
    public String getHelp() {
        return "```\n" +
                "This command will play a track\n" +
                "Use it as: !play youtube_link or !play track_name\n" +
                "Prerequisites: Be in a voice channel" +
                "\n```";
    }

    public boolean urlIsValid(String url) {
        try {
            new URI(url);
            return true;
        } catch (URISyntaxException ex) {
            return false;
        }
    }
}
