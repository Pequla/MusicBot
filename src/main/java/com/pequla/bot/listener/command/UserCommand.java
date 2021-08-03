package com.pequla.bot.listener.command;

import com.pequla.bot.AppUtils;
import com.pequla.bot.listener.CommandListener;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Component
public class UserCommand extends GuildCommand {

    @Autowired
    public UserCommand(CommandListener listener) {
        super(listener);
    }

    @Override
    public void execute(GuildMessageReceivedEvent event, String[] args) {
        TextChannel channel = event.getChannel();
        if (args.length == 0) {
            send(channel, event.getAuthor());
            return;
        }

        if (args.length == 1) {
            event.getJDA().retrieveUserById(args[0]).queue(u -> send(channel, u));
        }
    }

    @Override
    public String getDescription() {
        return "Returns user data";
    }

    @Override
    public List<String> getTriggers() {
        return Collections.singletonList(".user");
    }

    private void send(TextChannel channel, User user) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm");
        channel.sendMessageEmbeds(AppUtils.createEmbed(user.getName())
                .addField("Name:", user.getAsTag(), true)
                .addField("Bot account:", String.valueOf(user.isBot()), true)
                .addField("Created at:", user.getTimeCreated().format(format), false)
                .setImage(user.getEffectiveAvatarUrl())
                .build()).queue();
    }
}
