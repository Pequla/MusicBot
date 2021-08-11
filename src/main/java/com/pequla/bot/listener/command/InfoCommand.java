package com.pequla.bot.listener.command;

import com.pequla.bot.AppUtils;
import com.pequla.bot.data.GuildData;
import com.pequla.bot.data.GuildDataService;
import com.pequla.bot.listener.CommandListener;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Component
public class InfoCommand extends GuildCommand {

    private final GuildDataService service;

    @Autowired
    public InfoCommand(CommandListener listener, GuildDataService service) {
        super(listener);
        this.service = service;
    }

    @Override
    public void execute(@NotNull GuildMessageReceivedEvent event, String[] args) {
        Guild guild = event.getGuild();
        Member self = guild.getSelfMember();
        GuildData data = service.retrieveGuildData(guild.getIdLong());

        String time = self.getTimeJoined().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        EmbedBuilder builder = AppUtils.createEmbed("Bot information")
                .setThumbnail(self.getUser().getEffectiveAvatarUrl())
                .addField("Joined at:", time, true)
                .addField("Developer:", "Pequla#3038", true);

        Role role = guild.getRoleById(data.getModeratorRoleId());
        if (role != null) {
            builder.addField("Moderator role:", role.getName(), false);
        }

        TextChannel bot = guild.getTextChannelById(data.getBotChannelId());
        if (bot != null) {
            builder.addField("Bot channel:", bot.getAsMention(), false);
        }

        TextChannel join = guild.getTextChannelById(data.getJoinLeaveChannelId());
        if (join != null) {
            builder.addField("Join leave channel:", join.getAsMention(), false);
        }

        event.getChannel().sendMessageEmbeds(builder.build()).queue();
    }

    @Override
    public String getDescription() {
        return "Get current bot configuration";
    }

    @Override
    public List<String> getTriggers() {
        return Arrays.asList(".info", ".config");
    }
}
