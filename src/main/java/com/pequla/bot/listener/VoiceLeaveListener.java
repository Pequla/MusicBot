package com.pequla.bot.listener;

import com.pequla.bot.DiscordBot;
import com.pequla.bot.data.GuildDataService;
import com.pequla.bot.listener.command.music.utils.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class VoiceLeaveListener extends DiscordListener {

    private final PlayerManager manager;

    @Autowired
    public VoiceLeaveListener(DiscordBot bot, GuildDataService service, PlayerManager manager) {
        super(bot, service);
        this.manager = manager;
    }

    @Override
    public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event) {
        final Guild guild = event.getGuild();
        final Member self = guild.getSelfMember();
        final GuildVoiceState state = self.getVoiceState();
        final AudioPlayer player = manager.getMusicManager(guild).getAudioPlayer();

        // Bot leaves (ex. admin disconnects it)
        if (event.getMember().equals(self)) {
            player.destroy();
            return;
        }

        if (state != null && state.inVoiceChannel()) {
            // Bot stays alone in channel
            VoiceChannel voice = event.getChannelLeft();
            if (Objects.equals(state.getChannel(), voice) && voice.getMembers().size() == 1) {
                // Stop playback and leave
                player.destroy();
                final AudioManager manager = guild.getAudioManager();
                manager.closeAudioConnection();
            }
        }

        VoiceChannel voice = event.getChannelLeft();
        List<Member> members = voice.getMembers();
        if (members.size() == 1 && members.contains(self)) {
            manager.getMusicManager(guild).getAudioPlayer().destroy();
        }
    }

}
