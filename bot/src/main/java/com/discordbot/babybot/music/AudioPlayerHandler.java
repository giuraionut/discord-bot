package com.discordbot.babybot.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;
import net.dv8tion.jda.api.audio.AudioSendHandler;

import java.nio.ByteBuffer;

public class AudioPlayerHandler implements AudioSendHandler {

    private final AudioPlayer audioPlayer;
    private final ByteBuffer byteBuffer;
    private final MutableAudioFrame audioFrame;

    public AudioPlayerHandler(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
        this.byteBuffer = ByteBuffer.allocate(1024);
        this.audioFrame = new MutableAudioFrame();
        this.audioFrame.setBuffer(byteBuffer);
    }

    @Override
    public boolean canProvide() {
        return this.audioPlayer.provide(this.audioFrame);
    }

    @Override
    public ByteBuffer provide20MsAudio() {
        return this.byteBuffer.flip();
    }

    @Override
    public boolean isOpus() {
        return true;
    }
}
