package com.pequla.bot.listener.command.music;

import com.pequla.bot.listener.CommandListener;
import com.pequla.bot.listener.command.music.utils.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PlayCommand extends MusicCommand {

    @Autowired
    public PlayCommand(CommandListener listener, PlayerManager manager) {
        super(listener, manager);
    }

    @Override
    public void execute(GuildMessageReceivedEvent event, String[] args) {
        final TextChannel channel = event.getChannel();
        if (args.length == 0) {
            channel.sendMessage("Usage: `" + getTriggers().get(0) + " <youtube-link>`").queue();
            return;
        }

        final GuildVoiceState selfState = event.getGuild().getSelfMember().getVoiceState();
        if (selfState == null) {
            return;
        }

        if (!selfState.inVoiceChannel()) {
            channel.sendMessage("I need to be in a voice channel").queue();
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

        if (!memberState.inVoiceChannel()) {
            channel.sendMessage("You need to be in a voice channel").queue();
            return;
        }

        if (!Objects.equals(memberState.getChannel(), selfState.getChannel())) {
            channel.sendMessage("You need to be in the same channel as me").queue();
            return;
        }

        String link = args[0];
        Pattern pattern = Pattern.compile("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]?");
        Matcher matcher = pattern.matcher(link);

        if (matcher.find()) {
            manager.loadAndPlay(channel, args[0], false);
            return;
        }
        manager.loadAndPlay(channel, "ytsearch:" + String.join(" ", args), true);
    }

    @Override
    public String getDescription() {
        return "Play a song from Youtube";
    }

    @Override
    public List<String> getTriggers() {
        return Arrays.asList(".play", ".queue");
    }
}
