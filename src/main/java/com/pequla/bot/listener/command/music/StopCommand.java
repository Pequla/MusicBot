package com.pequla.bot.listener.command.music;

import com.pequla.bot.AppUtils;
import com.pequla.bot.listener.CommandListener;
import com.pequla.bot.listener.command.music.utils.PlayerManager;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class StopCommand extends MusicCommand {

    @Autowired
    public StopCommand(CommandListener listener, PlayerManager manager) {
        super(listener, manager);
    }

    @Override
    public void execute(@NotNull GuildMessageReceivedEvent event, String[] args) {
        manager.getMusicManager(event.getGuild()).getAudioPlayer().destroy();
        event.getChannel().sendMessageEmbeds(AppUtils.createEmbed("Playback stopped")
                .setDescription("You have successfully stopped the playback")
                .build()).queue();
    }

    @Override
    public String getDescription() {
        return "Stop music playback";
    }

    @Override
    public List<String> getTriggers() {
        return Collections.singletonList(".stop");
    }
}
