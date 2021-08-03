package com.pequla.bot.listener.command;

import com.pequla.bot.AppUtils;
import com.pequla.bot.listener.CommandListener;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class ServerCommand extends GuildCommand {

    @Autowired
    public ServerCommand(CommandListener listener) {
        super(listener);
    }

    @Override
    public void execute(GuildMessageReceivedEvent event, String[] args) {
        TextChannel channel = event.getChannel();
        // Channel is from the same guild
        if (args.length == 0) {
            send(channel, channel.getGuild());
            return;
        }

        // Guild info from guild id
        if (args.length == 1) {
            Guild guild = event.getJDA().getGuildById(args[0]);
            if (guild == null) {
                channel.sendMessage("I am not a member of that server").queue();
                return;
            }
            send(channel, guild);
        }
    }

    @Override
    public String getDescription() {
        return "Returns server data";
    }

    @Override
    public List<String> getTriggers() {
        return Collections.singletonList(".server");
    }

    private void send(TextChannel channel, Guild guild) {
        guild.retrieveOwner().queue(owner -> channel.sendMessageEmbeds(AppUtils.createEmbed(guild.getName())
                .addField("Members:", String.valueOf(guild.getMemberCount()), true)
                .addField("Nitro Boosts:", String.valueOf(guild.getBoostCount()), true)
                .addField("Owner:", owner.getUser().getAsTag(), false)
                .setImage(guild.getIconUrl())
                .build()).queue());
    }
}
