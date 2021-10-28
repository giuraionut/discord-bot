package com.discordbot.babybot.music;

import com.discordbot.babybot.utils.Utils;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerManager {
    private static PlayerManager instance;

    private final Map<Long, GuildMusicManager> musicManagers;
    private final AudioPlayerManager audioPlayerManager;

    public PlayerManager() {
        this.musicManagers = new HashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
    }

    public GuildMusicManager getGuildMusicManager(Guild guild) {
        return this.musicManagers.computeIfAbsent(guild.getIdLong(), guildId -> {
            final GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager);
            guild.getAudioManager().setSendingHandler(guildMusicManager.getPlayerHandler());
            return guildMusicManager;
        });
    }

    public void loadAndPlayTrack(TextChannel channel, String trackURL) {
        GuildMusicManager guildMusicManager = this.getGuildMusicManager(channel.getGuild());
        this.audioPlayerManager.loadItemOrdered(guildMusicManager, trackURL, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                guildMusicManager.trackScheduler.addTrackToQueue(audioTrack);
                long duration = audioTrack.getDuration();

                channel.sendMessage("Adding to playlist: ")
                        .append(audioTrack.getInfo().title)
                        .append(" by ")
                        .append(audioTrack.getInfo().author).append(", length: ")
                        .append(Utils.milliToTime(duration)).queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                List<AudioTrack> tracks = audioPlaylist.getTracks();
                if (audioPlaylist.isSearchResult()) {
                    guildMusicManager.trackScheduler.addTrackToQueue(tracks.get(0));
                    long duration = tracks.get(0).getDuration();

                    channel.sendMessage("Adding to playlist: \n")
                            .append("**`").append(tracks.get(0).getInfo().title).append("`**")
                            .append(" by ")
                            .append("**`").append(tracks.get(0).getInfo().author).append("`**;\n")
                            .append("Length: ")
                            .append("**`").append(Utils.milliToTime(duration)).append("`**.").queue();
                } else {
                    tracks.forEach(guildMusicManager.trackScheduler::addTrackToQueue);
                    Long totalDuration = tracks.stream().map(AudioTrack::getDuration).reduce(Long::sum).orElse(0L);
                    channel.sendMessage("Adding to playlist: \n")
                            .append("**`").append(audioPlaylist.getName()).append("`**")
                            .append(" with ")
                            .append("**`").append(String.valueOf(tracks.size())).append("`**")
                            .append(" tracks;\n")
                            .append("Length: ")
                            .append("**`").append(Utils.milliToTime(totalDuration)).append("`**.").queue();
                }
            }

            @Override
            public void noMatches() {

            }

            @Override
            public void loadFailed(FriendlyException e) {

            }
        });
    }

    public static PlayerManager getInstance() {
        if (instance == null) {
            instance = new PlayerManager();
        }
        return instance;
    }
}
