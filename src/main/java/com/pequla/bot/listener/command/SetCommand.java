package com.pequla.bot.listener.command;

import com.pequla.bot.data.GuildDataService;
import com.pequla.bot.listener.CommandListener;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class SetCommand extends GuildCommand {

    private final GuildDataService service;

    @Autowired
    public SetCommand(CommandListener listener, GuildDataService service) {
        super(listener);
        this.service = service;
    }

    @Override
    public void execute(GuildMessageReceivedEvent event, String[] args) {
        TextChannel channel = event.getChannel();

        // Member must have Manage Server permission
        Member member = event.getMember();
        if (member != null && member.getPermissions().contains(Permission.MANAGE_SERVER)) {

            // Command must have 2 args
            if (args.length == 2) {
                Guild guild = event.getGuild();
                if (args[0].equalsIgnoreCase("role")) {

                    Role role = guild.getRoleById(args[1]);
                    if (role == null) {
                        channel.sendMessage("Invalid role id").queue();
                        return;
                    }
                    service.updateModeratorRoleId(guild.getIdLong(), role.getIdLong());
                    channel.sendMessage("Successfully updated staff role to " + role.getName()).queue();
                    return;
                }

                if (args[0].equalsIgnoreCase("bot")) {

                    TextChannel target = guild.getTextChannelById(args[1]);
                    if (target == null) {
                        channel.sendMessage("Invalid channel id").queue();
                        return;
                    }
                    service.updateBotChannelId(guild.getIdLong(), target.getIdLong());
                    channel.sendMessage("Successfully updated bot channel to " + target.getAsMention()).queue();
                    return;
                }

                if (args[0].equalsIgnoreCase("join")) {

                    TextChannel target = guild.getTextChannelById(args[1]);
                    if (target == null) {
                        channel.sendMessage("Invalid channel id").queue();
                        return;
                    }
                    service.updateJoinLeaveChannelId(guild.getIdLong(), target.getIdLong());
                    channel.sendMessage("Successfully updated join leave channel to " + target.getAsMention()).queue();
                    return;
                }
            }

            // Command usage
            String command = getTriggers().get(0);
            channel.sendMessage("Usage: `" + command +
                    " role <role-id>` or `" + command +
                    " bot <channel-id>` or `" + command +
                    " join <channel-id>`").queue();

            return;
        }
        channel.sendMessage("You can't use this, you need to have the Manage Server permission").queue();
    }

    @Override
    public String getDescription() {
        return "Sets staff role and bot channel id";
    }

    @Override
    public List<String> getTriggers() {
        return Collections.singletonList(".set");
    }
}
