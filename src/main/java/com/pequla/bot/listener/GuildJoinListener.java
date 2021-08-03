package com.pequla.bot.listener;

import com.pequla.bot.AppUtils;
import com.pequla.bot.DiscordBot;
import com.pequla.bot.data.GuildData;
import com.pequla.bot.data.GuildDataService;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GuildJoinListener extends DiscordListener {

    @Autowired
    public GuildJoinListener(DiscordBot bot, GuildDataService service) {
        super(bot,service);
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        long id = event.getGuild().getIdLong();
        GuildData data = service.generateGuildData(id);
        logger.info("Joined guild " + id + " with internal id " + data.getId());
    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        User user = event.getUser();
        sendMessageWithEmbed(event.getGuild(), AppUtils.createEmbed("Welcome")
                .setAuthor(user.getAsTag(), null, user.getEffectiveAvatarUrl())
                .setDescription("Hey, " + user.getName() + " welcome on board"));

    }

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        User user = event.getUser();
        sendMessageWithEmbed(event.getGuild(), AppUtils.createEmbed("Goodbye")
                .setAuthor(user.getAsTag(), null, user.getEffectiveAvatarUrl())
                .setDescription(user.getName() + " left us"));

    }

    private void sendMessageWithEmbed(Guild guild, EmbedBuilder builder) {
        GuildData data = service.generateGuildData(guild.getIdLong());
        TextChannel channel = guild.getTextChannelById(data.getJoinLeaveChannelId());
        if (channel != null) {
            channel.sendMessageEmbeds(builder.build()).queue();
        }
    }
}
