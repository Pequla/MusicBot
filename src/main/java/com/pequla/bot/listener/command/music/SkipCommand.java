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
public class SkipCommand extends MusicCommand {

    @Autowired
    public SkipCommand(CommandListener listener, PlayerManager manager) {
        super(listener, manager);
    }

    @Override
    public void execute(@NotNull GuildMessageReceivedEvent event, String[] args) {
        manager.getMusicManager(event.getGuild()).getScheduler().nextTrack();
        event.getChannel().sendMessageEmbeds(AppUtils.createEmbed("Track skipped")
                .setDescription("Skipping the current track")
                .build()).queue();
    }

    @Override
    public String getDescription() {
        return "Skips the current playing track";
    }

    @Override
    public List<String> getTriggers() {
        return Collections.singletonList(".skip");
    }
}
