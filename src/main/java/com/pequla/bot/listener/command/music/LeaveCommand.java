package com.pequla.bot.listener.command.music;

import com.pequla.bot.listener.CommandListener;
import com.pequla.bot.listener.command.music.utils.PlayerManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class LeaveCommand extends MusicCommand {

    @Autowired
    public LeaveCommand(CommandListener listener, PlayerManager manager) {
        super(listener, manager);
    }

    @Override
    public void execute(@NotNull GuildMessageReceivedEvent event, String[] args) {
        final TextChannel channel = event.getChannel();
        final Guild guild = event.getGuild();
        final GuildVoiceState selfState = guild.getSelfMember().getVoiceState();
        if (selfState == null) {
            return;
        }

        // Bot is not in a voice channel
        if (!selfState.inVoiceChannel()) {
            channel.sendMessage("I am not in a voice channel").queue();
            return;
        }

        // Stop playback
        manager.getMusicManager(guild).getAudioPlayer().destroy();

        // Leave
        final AudioManager manager = guild.getAudioManager();
        manager.closeAudioConnection();

        final VoiceChannel voice = selfState.getChannel();
        if (voice != null) {
            channel.sendMessage("Disconnected from " + voice.getAsMention()).queue();
        }

    }

    @Override
    public String getDescription() {
        return "Leaves a voice channel";
    }

    @Override
    public List<String> getTriggers() {
        return Collections.singletonList(".leave");
    }
}
