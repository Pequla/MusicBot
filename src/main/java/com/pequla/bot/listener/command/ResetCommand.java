package com.pequla.bot.listener.command;

import com.pequla.bot.data.GuildDataService;
import com.pequla.bot.listener.CommandListener;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class ResetCommand extends GuildCommand {

    private final GuildDataService service;

    @Autowired
    public ResetCommand(CommandListener listener, GuildDataService service) {
        super(listener);
        this.service = service;
    }

    @Override
    public void execute(@NotNull GuildMessageReceivedEvent event, String[] args) {
        TextChannel channel = event.getChannel();
        // Member must have Manage Server permission
        Member member = event.getMember();
        if (member != null && member.getPermissions().contains(Permission.MANAGE_SERVER)) {
            service.deleteGuildData(event.getGuild().getIdLong());
            channel.sendMessage("Successfully reset all properties").queue();
            return;
        }
        channel.sendMessage("You can't use this, you need to have the Manage Server permission").queue();
    }

    @Override
    public String getDescription() {
        return "Resets server properties";
    }

    @Override
    public List<String> getTriggers() {
        return Collections.singletonList(".reset");
    }
}
