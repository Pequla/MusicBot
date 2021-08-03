package com.pequla.bot.listener.command.music.utils;

import com.pequla.bot.AppUtils;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PlayerManager {

    private final Map<Long, GuildMusicManager> musicManagers;
    private final AudioPlayerManager audioPlayerManager;

    private PlayerManager() {
        this.musicManagers = new HashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(audioPlayerManager);
        AudioSourceManagers.registerLocalSource(audioPlayerManager);
    }

    public GuildMusicManager getMusicManager(Guild guild) {
        return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            final GuildMusicManager guildMusicManager = new GuildMusicManager(audioPlayerManager);
            guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());
            return guildMusicManager;
        });
    }

    public void loadAndPlay(TextChannel channel, String trackUrl, boolean search) {
        Guild guild = channel.getGuild();
        final GuildMusicManager musicManager = getMusicManager(guild);
        this.audioPlayerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                musicManager.getScheduler().queue(track);
                AppUtils.sendTrackInfoEmbed(channel, "Track added to queue", track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                // Youtube search
                if (search) {
                    AudioTrack track = playlist.getTracks().get(0);
                    musicManager.getScheduler().queue(track);
                    AppUtils.sendTrackInfoEmbed(channel, "Track added to queue", track);
                    return;
                }

                // Youtube playlist
                for (AudioTrack track : playlist.getTracks()) {
                    musicManager.getScheduler().queue(track);
                }
                AudioTrack selected = playlist.getSelectedTrack();
                if (selected != null) {
                    AudioTrackInfo info = selected.getInfo();
                    channel.sendMessageEmbeds(AppUtils.createEmbed("Playlist loaded")
                            .addField("Name:", playlist.getName(), false)
                            .addField("Selected:", info.title, false)
                            .addField("Author:", info.author, false)
                            .setThumbnail(AppUtils.generateYoutubeThumbnail(info.uri))
                            .build()).queue();
                }
            }

            @Override
            public void noMatches() {
                channel.sendMessageEmbeds(AppUtils.createEmbed("Track not found")
                        .setDescription("Requested track could not be found")
                        .build()).queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                channel.sendMessageEmbeds(AppUtils.createEmbed("Playback failed")
                        .setDescription("There was an error playing this track")
                        .build()).queue();
            }
        });
    }
}
