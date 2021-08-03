package com.pequla.bot.listener.command;

import com.pequla.bot.AppUtils;
import com.pequla.bot.listener.CommandListener;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class HelpCommand extends GuildCommand {

    @Autowired
    public HelpCommand(CommandListener listener) {
        super(listener);
    }

    @Override
    public void execute(GuildMessageReceivedEvent event, String[] args) {
        EmbedBuilder builder = AppUtils.createEmbed("Command list");
        for (GuildCommand command : listener.getCommands()) {
            builder.addField(command.getTriggers().get(0), command.getDescription(), false);
        }
        event.getChannel().sendMessageEmbeds(builder.build()).queue();
    }

    @Override
    public String getDescription() {
        return "Lists all available commands";
    }

    @Override
    public List<String> getTriggers() {
        return Arrays.asList(".help", ".h");
    }
}
