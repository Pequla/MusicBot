package com.pequla.bot.listener.command;

import com.pequla.bot.AppUtils;
import com.pequla.bot.listener.CommandListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class PingCommand extends GuildCommand {

    @Autowired
    public PingCommand(CommandListener listener) {
        super(listener);
    }

    @Override
    public void execute(@NotNull GuildMessageReceivedEvent event, String[] args) {
        JDA jda = event.getJDA();
        jda.getRestPing().queue(ping -> event.getChannel().sendMessageEmbeds(AppUtils.createEmbed("Discord response ping")
                .setThumbnail("https://discord.com/assets/2c21aeda16de354ba5334551a883b481.png")
                .addField("REST", String.valueOf(ping), true)
                .addField("WebSocket", String.valueOf(jda.getGatewayPing()), true)
                .build()).queue());
    }

    @Override
    public String getDescription() {
        return "Displays Discord response ping";
    }

    @Override
    public List<String> getTriggers() {
        return Collections.singletonList(".ping");
    }
}
