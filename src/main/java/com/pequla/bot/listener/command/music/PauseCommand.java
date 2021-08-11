package com.pequla.bot.listener.command.music;

import com.pequla.bot.AppUtils;
import com.pequla.bot.listener.CommandListener;
import com.pequla.bot.listener.command.music.utils.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class PauseCommand extends MusicCommand {

    @Autowired
    public PauseCommand(CommandListener listener, PlayerManager manager) {
        super(listener, manager);
    }

    @Override
    public void execute(@NotNull GuildMessageReceivedEvent event, String[] args) {
        TextChannel channel = event.getChannel();
        AudioPlayer player = manager.getMusicManager(event.getGuild()).getAudioPlayer();
        if (player.isPaused()) {
            player.setPaused(false);
            channel.sendMessageEmbeds(AppUtils.createEmbed("Playback continued")
                    .setDescription("Playback successfully continued")
                    .build()).queue();
            return;
        }
        player.setPaused(true);
        channel.sendMessageEmbeds(AppUtils.createEmbed("Playback paused")
                .setDescription("Playback successfully paused")
                .build()).queue();
    }

    @Override
    public String getDescription() {
        return "Pauses the music playback";
    }

    @Override
    public List<String> getTriggers() {
        return Arrays.asList(".pause", ".continue");
    }
}
