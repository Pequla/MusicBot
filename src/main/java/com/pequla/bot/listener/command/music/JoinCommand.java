package com.pequla.bot.listener.command.music;

import com.pequla.bot.listener.CommandListener;
import com.pequla.bot.listener.command.music.utils.PlayerManager;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class JoinCommand extends MusicCommand {

    @Autowired
    public JoinCommand(CommandListener listener, PlayerManager manager) {
        super(listener, manager);
    }

    @Override
    public void execute(@NotNull GuildMessageReceivedEvent event, String[] args) {
        final TextChannel channel = event.getChannel();

        // Checking if bot is in a channel
        final Guild guild = event.getGuild();
        final GuildVoiceState selfState = guild.getSelfMember().getVoiceState();
        if (selfState != null && selfState.inVoiceChannel()) {
            channel.sendMessage("I am already in a channel").queue();
            return;
        }

        final Member member = event.getMember();
        if (member == null) {
            return;
        }

        final GuildVoiceState memberState = member.getVoiceState();
        if (memberState == null) {
            return;
        }

        // Checking if member is in a channel
        if (!memberState.inVoiceChannel()) {
            channel.sendMessage("Please join a voice channel first").queue();
            return;
        }

        final AudioManager manager = guild.getAudioManager();
        final VoiceChannel voice = memberState.getChannel();
        if (voice == null) {
            return;
        }
        manager.openAudioConnection(voice);
        channel.sendMessage("Connecting to " + voice.getAsMention()).queue();
    }

    @Override
    public String getDescription() {
        return "Joins a voice channel";
    }

    @Override
    public List<String> getTriggers() {
        return Collections.singletonList(".join");
    }
}
