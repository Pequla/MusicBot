package com.pequla.bot.listener.command;

import com.pequla.bot.listener.CommandListener;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.Button;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class InviteCommand extends GuildCommand {

    @Autowired
    public InviteCommand(CommandListener listener) {
        super(listener);
    }

    @Override
    public void execute(@NotNull GuildMessageReceivedEvent event, String[] args) {
        String link = event.getJDA().getInviteUrl(Permission.ADMINISTRATOR);
        event.getChannel().sendMessage("You can invite me here:")
                .setActionRow(Button.link(link, "Invite link")).queue();
    }

    @Override
    public String getDescription() {
        return "Sends an invite link";
    }

    @Override
    public List<String> getTriggers() {
        return Collections.singletonList(".invite");
    }
}
