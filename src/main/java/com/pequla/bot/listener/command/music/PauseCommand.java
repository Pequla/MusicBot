package com.pequla.bot.listener.command.music;

import com.pequla.bot.listener.CommandListener;
import com.pequla.bot.listener.command.music.utils.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
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
    public void execute(GuildMessageReceivedEvent event, String[] args) {
        TextChannel channel = event.getChannel();
        AudioPlayer player = manager.getMusicManager(event.getGuild()).getAudioPlayer();
        if (player.isPaused()) {
            player.setPaused(false);
            channel.sendMessage("Playback continued").queue();
            return;
        }
        player.setPaused(true);
        channel.sendMessage("Playback paused").queue();
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
