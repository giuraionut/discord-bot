package com.discordbot.babybot.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

public class GuildMusicManager {
    public final AudioPlayer audioPlayer;
    public final TrackScheduler trackScheduler;
    private final AudioPlayerHandler playerHandler;

    public GuildMusicManager(AudioPlayerManager audioPlayerManager) {
        this.audioPlayer = audioPlayerManager.createPlayer();
        this.trackScheduler = new TrackScheduler(this.audioPlayer);
        this.audioPlayer.addListener(trackScheduler);
        this.playerHandler = new AudioPlayerHandler(this.audioPlayer);
    }

    public AudioPlayerHandler getPlayerHandler()
    {
        return this.playerHandler;
    }
}
