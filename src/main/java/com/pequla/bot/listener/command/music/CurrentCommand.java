package com.pequla.bot.listener.command.music;

import com.pequla.bot.AppUtils;
import com.pequla.bot.listener.CommandListener;
import com.pequla.bot.listener.command.music.utils.PlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class CurrentCommand extends MusicCommand {

    @Autowired
    public CurrentCommand(CommandListener listener, PlayerManager manager) {
        super(listener, manager);
    }

    @Override
    public void execute(@NotNull GuildMessageReceivedEvent event, String[] args) {
        AudioTrack track = manager.getMusicManager(event.getGuild()).getAudioPlayer().getPlayingTrack();
        AppUtils.sendTrackInfoEmbed(event.getChannel(), "Current track info", track);
    }

    @Override
    public String getDescription() {
        return "Displays the current playing track";
    }

    @Override
    public List<String> getTriggers() {
        return Collections.singletonList(".current");
    }
}
